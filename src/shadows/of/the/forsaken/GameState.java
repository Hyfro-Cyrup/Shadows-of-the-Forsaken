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
    /**
     * A common GameState instance to be shared by all objects who need it.
     */
    private static GameState instance;
    
    private final Player player;
    private final DungeonTile[][] map;
    
    /**
     * Default constructor. Initializes player and map.
     */
    public GameState()
    {
        player = new Player("Dummy/file/path", 69, emptyList(), false, emptyList());
        map = MapMaker.newMap();
        
    }
    
    /**
     * Returns the common instance for all objects to observe and manipulate.
     * @return a GameState object
     */
    public static GameState getInstance()
    {
        if (instance == null)
        {
            // TODO: option for loading from file
            instance = new GameState();
        }
        return instance;
    }
    
    
    /**
     * Returns the map
     * @return 2D array of DungeonTile objects. Some will be null. 
     */
    public DungeonTile[][] getMap() { return map; }
    
    /**
     * Returns the player.
     * @return the player
     */
    public Player getPlayer() { return player; }
}
