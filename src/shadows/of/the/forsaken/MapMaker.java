/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Nathan Ainslie
 */
public final class MapMaker {
    // Map Generation Parameters
    // everything's up here for easy tweaking and so there's no magic numbers
    private static final int WIDTH = 20;
    private static final int HEIGHT = 16;
    private static final int ITERATIONS = 20; // vaguely how large the map can branch out to be
    private static final double ENTITY_PROB = 0.3;  // higher makes the ladder and key appear earlier
    private static final double CREATURE_PROB = 0.2; // higher makes more creatures
    private static final double NEIGHBOR_PROB = 0.05; // at 0, no 2x2 rooms appear. Higher makes more clusters like that
    private static final int ENTITY_START = 5; // how many iterations must be done before non-creature entities appear
    private static final int CREATURE_START = 1; // how many iterations must be done before creatures can appear
    
    
    /**
     * Populates a grid of DungeonTile objects via an infection-like simulation.
     * @param w Width of the array
     * @param h Height of the array
     * @param iterations Number of times to run the simulation
     * @param probability The chance a given tile can "infect" its neighbors
     * @return a (`w` x `h`) array of DungeonTiles
     */
    private static DungeonTile[][] coolMapGenerator(int w, int h, int iterations, double probability)
    {       
        List<Entity> Creatures = new ArrayList<>();
        Creatures.add(new Creature("Gerblin", "Nasty-looking fellow", "dummy/path"));
        Creatures.add(new Creature("Gerblin", "Nasty-looking fellow", "dummy/path"));
        
        List<Entity> Objects = new ArrayList<>();
        Objects.add(new Entity("Ladder", "The exit beckons.", "dummy/path"));
        Objects.add(new Entity("Key", "It shines with hope.", "dummy/path"));
        System.out.println("Starting new map");
        
        DungeonTile[][] map = new DungeonTile[w][h];
        ArrayList<List<Integer>> cursor = new ArrayList<>();
        cursor.add(Arrays.asList(w/2, h/2));
        
        // track which cells are filled
        ArrayList<List<Integer>> visited = new ArrayList<>();
        
        // fill the first tile
        //map[cursor.get(0).get(0)][cursor.get(0).get(1)] = new DungeonTile(Arrays.asList(), true);
        
        // modeled after influence diffusion in a network. 
        // each tile has a chance to infect its neighbors.
        for (int i = 0; i < iterations; i++)
        {
            ArrayList<List<Integer>> nextCursor = new ArrayList<>();
            ArrayList<Entity> contents = new ArrayList<>();
            
            // mark tiles beforehand to avoid conflict
            for (List<Integer> tile : cursor)
            {
                visited.add(tile);
            }
            
            for (List<Integer> tile : cursor) 
            {
                contents.clear();
                System.out.print("Starting contents = ");
                System.out.println(contents);
                // fill in dungeon tile
                if ((i > ENTITY_START) && (!Objects.isEmpty()) && (Math.random() < ENTITY_PROB))
                {
                    System.out.println(Objects.isEmpty());
                    // add an object
                    int index = (int) Math.floor(Math.random()*Objects.size());
                    contents.add(Objects.get(index));
                    Objects.remove(index);
                    System.out.println(Objects.isEmpty());
                    
                }
                else if ((i > CREATURE_START) && (Math.random() < CREATURE_PROB))
                {
                    Entity entity = Creatures.get((int) Math.floor(Math.random()*Creatures.size()));
                    contents.add(entity);
                }
                
                map[tile.get(0)][tile.get(1)] = new DungeonTile(new ArrayList<>(contents), false);               
                
                // propagate the next generation
                int[][] points = {
                {tile.get(0) + 1, tile.get(1)}, // East
                {tile.get(0), tile.get(1) -1},  // North
                {tile.get(0) -1, tile.get(1)},  // West
                {tile.get(0), tile.get(1) + 1}, // South
                };
                
                int[][] corners = {
                    {tile.get(0) + 1, tile.get(1) - 1}, // NE
                    {tile.get(0) - 1, tile.get(1) - 1}, // NW
                    {tile.get(0) - 1, tile.get(1) + 1}, // SW
                    {tile.get(0) + 1, tile.get(1) + 1}, // SE
                };
                
                
                for(int j = 0; j < 4; j++)
                {
                    int[] p = points[j];
                    if ((p[0] >= 0) && (p[0] < w) && (p[1] >= 0) && (p[1] < h) &&
                            !(visited.contains(Arrays.asList(p[0], p[1]))))
                    {
                        // reduce certain probabilities to make more linear
                        var prob = probability;
                        var ccw = corners[j];   // counter-clockwise neighbor
                        if (visited.contains(Arrays.asList(ccw[0], ccw[1])))
                        {
                            prob *= NEIGHBOR_PROB;
                        }
                        var cw = corners[j > 0 ? j - 1 : j + 3]; // clockwise neighbor
                        if (visited.contains(Arrays.asList(cw[0], cw[1])))
                        {
                            prob *= NEIGHBOR_PROB;
                        }
                        
                        
                        // infect next cell with some chance
                        if (Math.random() < prob)
                        {
                            nextCursor.add(Arrays.asList(p[0], p[1]));
                        }
                    }
                }
            }
            
            cursor = nextCursor;
        }
        return map;
    }
    
    
    /**
     * Creates a new dungeon map from scratch.
     * @return a 2d array of DungeonTile objects
     */
    public static DungeonTile[][] newMap()
    {
        DungeonTile[][] map = coolMapGenerator(WIDTH, HEIGHT, ITERATIONS, 0.6);
        while (!mapIsValid(map))
        {
            map = coolMapGenerator(20, 16, 20, 0.6);
        }
        return map;
    }
    
    private static Boolean mapIsValid(DungeonTile[][] map)
    {
        int count = 0;
        for (DungeonTile[] col : map){
            for (DungeonTile tile : col){
                if (tile != null){
                    count += 1;
                }
            }
        }
        if (count < 15)
        {
            return false;
        }
        
        // Check for key placement
        for (DungeonTile[] col : map){
            for (DungeonTile tile : col) {
                if ((tile != null) && (tile.containsKey())){
                    System.out.println("Key found!");
                    if (tile.containsEnemy()){
                        System.out.println("Enemy also found. ");
                        return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Private MapMaker constructor ensures that it will not be instantiated
     */
    private MapMaker()
    {
        throw new UnsupportedOperationException("MapMaker should not be instantiated");
    }
}
