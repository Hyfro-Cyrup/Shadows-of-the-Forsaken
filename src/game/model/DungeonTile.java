/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.model;

import java.io.Serializable;
import static java.util.Collections.emptyList;
import java.util.List;

/**
 * Location the player can go to, and container for the objects found there.
 */
public class DungeonTile implements Serializable {
    private final List<Entity> Contents;
    private Boolean hasBeenSeen;
    
    /**
     * Default constructor. Initializes as unseen with no contents.
     */
    public DungeonTile()
    {
        Contents = emptyList();
        hasBeenSeen = false;
    }
    
    /**
     * Constructs a dungeon tile with specified parameters.
     * 
     * @param contents A list of entity objects to be found within the room
     * @param seen Whether or not the player has seen this room yet.
     */
    public DungeonTile(List<Entity> contents, Boolean seen)
    {
        Contents = contents;
        hasBeenSeen = seen;
    }
    
    /**
     * Returns true if the key is inside the tile, false if not.
     * @return a Boolean
     */
    public Boolean containsKey()
    {
        return Contents.stream().anyMatch(e -> "Key".equals(e.getName()));
    }
    
    public Boolean containsEnemy()
    {
        return Contents.stream().anyMatch(e -> e instanceof Creature);
    }
    
    public Boolean containsLadder()
    {
        return Contents.stream().anyMatch(e -> "Ladder".equals(e.getName()));
    }
    
    public Boolean hasBeenSeen()
    {
        return this.hasBeenSeen;
    }
    
    public void markSeen()
    {
        hasBeenSeen = true;
    }
}
