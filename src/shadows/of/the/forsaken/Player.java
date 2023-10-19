/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

import java.util.List;

/**
 * [[insert description of player class]]
 * @author Nathan Ainslie
 * Sonny's Note: Should probably discuss declouping Player's responsiblities with managing it's posistion in the map, with it's responisiblities in 
 * a combat Encounter. Having two seperate classes is better for that I think. Have all objects dealing with Encounters be in their own 'bubble' and same 
 * for Map-adjcent classes
 */
public class Player extends Creature {
    private Boolean hasExitKey;
    private List<Entity> Inventory;
    public int x, y;
    
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
