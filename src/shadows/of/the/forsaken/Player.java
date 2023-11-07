/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

import java.io.Serializable;
import java.util.List;

/**
 * A creature that the player controls and has an inventory.
 */
public class Player extends Creature implements Serializable{
    private Boolean hasExitKey;
    private List<Entity> Inventory;
    /**
     * Location of the Player in the grid
     */
    public int x, y;
    
    /**
     * Construct a Player with specified parameters
     * 
     * @param _spritePath String filepath to the image of the entity
     * @param hp The health of the creature
     * @param attacks A list of Attack objects
     * @param key Bool whether or not the exit key is in the player's inventory
     * @param inventory List of entities that the player has with them. Currently unused.
     */
    public Player(String _spritePath, int hp, List<Attack> attacks, Boolean key, List<Entity> inventory)
    {
        super("The Player", "The FOOOLISH KNIGHT", _spritePath);
        hasExitKey = key;
        Inventory = inventory;
        x = 0;
        y = 0;
    }
    
    /**
     * Mutator to move the player by some `dx` tiles in the x direction and `dy` tiles in the y direction
     * @param dx Number of tiles to move in x direction
     * @param dy Number of tiles to move in y direction
     * @param map Collection of tiles used for movement bounds
     */
    public void move(int dx, int dy, DungeonTile[][] map){
        int nx = x + dx;
        int ny = y + dy;
        if (-1 < nx && nx < map.length && 
            -1 < ny && ny < map[0].length &&
            map[nx][ny] != null)
        {
            x = nx;
            y = ny;
        }
    }
}
