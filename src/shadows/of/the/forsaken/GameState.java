/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Collections.emptyList;
import java.util.List;

/**
 * A common root for all of our data. 
 * GameState operates as a singleton service via its `getInstance` method.
 * 
 */
public class GameState {
    // define single instance
    private static GameState instance;
    
    private Player player;
    private DungeonTile[][] map;
    private DungeonTile[][][] multiMap;
    
    // default constructor
    public GameState()
    {
        player = new Player("Dummy/file/path", 69, emptyList(), false, emptyList());
        multiMap = coolMapGenerator(20, 16, 20, 0.6);
        map = multiMap[multiMap.length - 1];
    }
    
    public static GameState getInstance()
    {
        if (instance == null)
        {
            // TODO: option for loading from file
            instance = new GameState();
        }
        return instance;
    }
    
    private static DungeonTile[][][] coolMapGenerator(int w, int h, int iterations, double probability)
    {       
        ArrayList<Entity> entities = new ArrayList<>();
        entities.add(new Entity("Key", "It shines with hope.", "dummy/path"));
        entities.add(new Entity("Ladder", "The exit beckons", "dummy/path"));
        entities.add(new Creature("Gerblin", "Nasty-looking fellow", "dummy/path", 20, new ArrayList<>(1)));
        entities.add(new Creature("Gerblin", "Nasty-looking fellow", "dummy/path", 20, new ArrayList<>(1)));
        entities.add(new Creature("Gerblin", "Nasty-looking fellow", "dummy/path", 20, new ArrayList<>(1)));

        
        DungeonTile[][][] multimap = new DungeonTile[iterations][w][h];
        ArrayList<List<Integer>> cursor = new ArrayList<>();
        cursor.add(Arrays.asList(w/2, h/2));
        
        // track which cells are filled
        ArrayList<List<Integer>> visited = new ArrayList<>();
        
        // fill the first tile
        multimap[0][cursor.get(0).get(0)][cursor.get(0).get(1)] = new DungeonTile(Arrays.asList(), false);
        
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
                // fill in dungeon tile
                if (Math.random() < probability)
                {
                    Entity entity = entities.get((int) Math.floor(Math.random()*entities.size()));
                    contents.add(entity);
                }
                for (int j = i; j < iterations; j++)
                {
                    multimap[j][tile.get(0)][tile.get(1)] = new DungeonTile(contents, false);
                }
                
                
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
                            prob *= 0;
                        }
                        var cw = corners[j > 0 ? j - 1 : j + 3]; // clockwise neighbor
                        if (visited.contains(Arrays.asList(cw[0], cw[1])))
                        {
                            prob *= 0;
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
        return multimap;
    }
    
    public DungeonTile[][] getMap() { return map; }
    
    public DungeonTile[][][] getMultiMap() { return multiMap; }
    
    public Player getPlayer() { return player; }
}
