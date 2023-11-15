/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.model;

import game.util.MapMaker;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A common root for all of our data. 
 * GameState operates as a singleton service via its `getInstance` method.
 * 
 */
public class GameState implements Serializable {
    /**
     * A common GameState instance to be shared by all objects who need it.
     */
    private static GameState instance;
    
    private Player player;
    private DungeonTile[][] map;
    
    /**
     * Default constructor. Initializes player and map.
     */
    public GameState()
    {
        player = new Player();
        map = MapMaker.newMap();
    }
    
    /**
     * Copy another GameState object
     * @param gs The GameState from which to copy
     */
    private void copy(GameState gs)
    {
        this.player = gs.player;
        this.map = gs.map;
    }
    
    /**
     * Returns the common instance for all objects to observe and manipulate.
     * @return a GameState object
     */
    public static GameState getInstance()
    {
        if (instance == null)
        {
            instance = new GameState();
        }
        return instance;
    }
    
    /**
     * Serializes the current GameState and write it to a save file
     * @param filename The name of the file to save to, without extension
     */
    public static void saveGame(String filename)
    {
        FileOutputStream file = null;
        try {
            file = new FileOutputStream("saves/" + filename + ".ser");
            ObjectOutputStream obStream = new ObjectOutputStream(file);
            obStream.writeObject(instance);
            obStream.flush();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GameState.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GameState.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (file != null) { file.close(); }
            } catch (IOException ex) {
                Logger.getLogger(GameState.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    /**
     * Deserializes and loads a GameState from file
     * @param filename The name of the file to read from, without extension
     */
    public static void loadGame(String filename)
    {
        FileInputStream file = null;
        try {
            file = new FileInputStream("saves/" + filename + ".ser");
            ObjectInputStream obStream = new ObjectInputStream(file);
            instance.copy((GameState) obStream.readObject());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GameState.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(GameState.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try {
                if (file != null) { file.close(); } 
            } catch (IOException ex) {
                Logger.getLogger(GameState.class.getName()).log(Level.SEVERE, null, ex);
            }
        }    
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
