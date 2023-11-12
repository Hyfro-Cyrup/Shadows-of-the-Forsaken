/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.model;

import java.io.Serializable;

/**
 * An object that can be found in the dungeon.
 */
public class Entity implements Serializable{
    /**
     * The name for the GUI
     */
    private final String name;
    /**
     * A short description for the GUI
     */
    private final String description;
    /**
     * Path to this sprite's resource file
     */
    private final String spritePath;

    
    /**
     * Construct an Entity with specified parameters
     * 
     * @param name The name of the entity
     * @param description A short description to be shown
     * @param spritePath String filepath to the image of the entity
     */
    public Entity(String name, String description, String spritePath)
    {
        this.name = name;
        this.description = description;
        this.spritePath = spritePath;
    }

    
    /**
     * Returns the entity's name as a String
     * @return the name
     */
    public String getName() { return name; }

    /**
     * Returns the path to the entity's resource file
     * @return the relative file path as a String
     */
    public String getSpriteReference(){ return spritePath; }
    
}
