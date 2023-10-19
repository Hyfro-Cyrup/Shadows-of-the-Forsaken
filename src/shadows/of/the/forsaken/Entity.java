/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

/**
 * An object that can be found in the dungeon.
 */
public class Entity {
    private final String name;
    private final String description;
    private final String spritePath;
    
    /**
     * Construct an Entity with specified parameters
     * 
     * @param _name The name of the entity
     * @param _description A short description to be shown
     * @param _spritePath String filepath to the image of the entity
     */
    public Entity(String _name, String _description, String _spritePath)
    {
        name = _name;
        description = _description;
        spritePath = _spritePath;
    }
    
    /**
     * Returns the entity's name as a String
     * @return the name
     */
    public String getName() { return name; }
}
