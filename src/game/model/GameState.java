/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.model;

import game.gui.MainGUI;
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
    private WinState winState = WinState.PLAYING;
            
    public enum WinState
    {
        PLAYING, 
        WON,
        LOST
    }
    
    /**
     * Default constructor. Initializes player and map.
     */
    public GameState()
    {
        player = new Player();
        map = MapMaker.newMap();
        player.x = map.length / 2;
        player.y = map[0].length / 2;
    }
    
    /**
     * Copy another GameState object
     * @param gs The GameState from which to copy
     */
    private void copy(GameState gs)
    {
        this.player = gs.player;
        this.map = gs.map;
        this.winState = gs.winState;
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
     * When called, created new game
     * @return a GameState object
     */
    public static GameState newInstance()
    {
        instance.copy(new GameState());
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
            instance.loadScreen();
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
     * Checks file to ensure it exists and is valid
     * @param filename The name of the file to read from, without extension
     * @return false when file is not valid or does not exist, true when file exists and is valid 
     */
    public static Boolean checkFile(String filename)
    {
        FileInputStream file = null;
        try {
            file = new FileInputStream("saves/" + filename + ".ser");
            ObjectInputStream obStream = new ObjectInputStream(file);
            GameState temp = (GameState) obStream.readObject();
        } catch (FileNotFoundException ex) {
            return false;
        } catch (IOException | ClassNotFoundException ex) {
            return false;
        }
        finally {
            try {
                if (file != null) { file.close(); } 
            } catch (IOException ex) {
                Logger.getLogger(GameState.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   
        return true;
    }
    
    /**
     * Load the correct scene for the current GameState
     */
    public void loadScreen()
    {
        //switch (winState)
        //{
        //    case PLAYING -> 
        //    {
                player.loadFix();
                DungeonTile tile = map[player.x][player.y]; 
                if (tile.inEncounter())
                {
                    // loaded game is actively in combat
                    tile.startEncounter();
                    MainGUI.getInstance().changeScene(tile.getGUI());
                }
                else
                {
                    MainGUI.getInstance().changeScene("DUNGEON_MAP");
                }  
        //    }
        //    case WON -> MainGUI.getInstance().changeScene("WIN_SCREEN");
        //    case LOST -> MainGUI.getInstance().changeScene("LOSE_SCREEN");
        //}
        
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
    
    /**
     * Get whether the current game was won, lost, or ongoing
     * @return a WinState enum value
     */
    public WinState getWinState() { return winState; }
    
    /**
     * Indicate that the current game was won.
     */
    public void Win()
    {
        winState = WinState.WON;
    }
    
    /**
     * Indicate that the current game was lost.
     */
    public void Lose()
    {
        winState = WinState.LOST;
    }
}
