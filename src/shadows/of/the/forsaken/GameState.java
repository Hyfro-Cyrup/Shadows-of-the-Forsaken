/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Collections.emptyList;
import java.util.List;
import java.util.Random;

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
        multiMap = coolMapGenerator(20, 16, 20, 0.25);
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
        entities.add(new Creature("Gerblin", "Nasty-looking fellow", "dummy/path"));
        entities.add(new Creature("Gerblin", "Nasty-looking fellow", "dummy/path"));
        entities.add(new Creature("Gerblin", "Nasty-looking fellow", "dummy/path"));

        
        DungeonTile[][][] multimap = new DungeonTile[iterations][w][h];
        ArrayList<int[]> cursor = new ArrayList<>();
        cursor.add(new int[]{w/2, h/2});
        
        // fill the first tile
        multimap[0][cursor.get(0)[0]][cursor.get(0)[1]] = new DungeonTile(Arrays.asList(), false);
        
        // modeled after influence diffusion in a network. 
        // each tile has a chance to infect its neighbors.
        for (int i = 0; i < iterations; i++)
        {
            ArrayList<int[]> nextCursor = new ArrayList<>();
            ArrayList<int[]> visited = new ArrayList<>();
            ArrayList<Entity> contents = new ArrayList<>();
            for (int[] tile : cursor) 
            {
                contents.clear();
                // fill in dungeon tile
                visited.add(tile);
                if (Math.random() < probability)
                {
                    Entity entity = entities.get((int) Math.floor(Math.random()*entities.size()));
                    contents.add(entity);
                }
                for (int j = i; j < iterations; j++)
                {
                    multimap[j][tile[0]][tile[1]] = new DungeonTile(contents, false);
                }
                
                
                // propagate the next generation
                int[][] points = {
                {tile[0] -1, tile[1]},
                {tile[0] + 1, tile[1]},
                {tile[0], tile[1] -1},
                {tile[0], tile[1] + 1}};
                
                for(int[] p : points)
                {
                    if ((p[0] >= 0) && (p[0] < w) && (p[1] >= 0) && (p[1] < h) &&
                            !(visited.contains(p)) && (Math.random() < probability))
                    {
                        nextCursor.add(p);
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
