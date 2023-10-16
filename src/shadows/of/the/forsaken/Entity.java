/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

/**
 * An object that can be found in the dungeon.
 */
public class Entity {
    private String name;
    private String description;
    private String spritePath;
    
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
}
