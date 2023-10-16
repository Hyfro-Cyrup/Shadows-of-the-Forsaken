/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

import java.util.List;

/**
 * A type of entity that has hp and attacks. Used in combat encounters.
 */
public class Creature extends Entity {
    private int HP;
    private List<Attack> Attacks;
    
    /**
     * Construct new Creature instance with specified parameters
     * 
     * @param _name The name of the entity
     * @param _description A short description to be shown
     * @param _spritePath String filepath to the image of the entity
     * @param hp The health of the creature
     * @param attacks A list of Attack objects
     */
    public Creature(String _name, String _description, String _spritePath, int hp, List<Attack> attacks)
    {
        super(_name, _description, _spritePath);
        HP = hp;
        Attacks = attacks;
    }
    
    
    
}
