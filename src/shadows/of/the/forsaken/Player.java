/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

import java.util.List;

/**
 * A creature that the player controls and has an inventory.
 */
public class Player extends Creature {
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
        super("The Player", "Description of the player???", _spritePath, hp, attacks);
        hasExitKey = key;
        Inventory = inventory;
        x = 0;
        y = 0;
    }
    
    /**
     * Mutator to move the player by some `dx` tiles in the x direction and `dy` tiles in the y direction
     * @param dx
     * @param dy 
     */
    public void move(int dx, int dy){
        x += dx;
        y += dy;
    }
}
