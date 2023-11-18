/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.model;

import game.gui.EncounterScreen;
import java.io.Serializable;
import java.util.ArrayList;
import static java.util.Collections.emptyList;
import java.util.List;

/**
 * Location the player can go to, and container for the objects found there.
 */
public class DungeonTile implements Serializable {
    private final List<Entity> Contents;
    private Boolean hasBeenSeen;
    private transient EncounterEngine engine = null;
    private transient EncounterScreen gui = null;
    private Boolean inEncounter = false;
    
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
     * Determines if this tile contains the key
     * @return True if it contains the key
     */
    public Boolean containsKey()
    {
        return Contents.stream().anyMatch(e -> "Key".equals(e.getName()));
    }
    
    /**
     * Determines if this tile contains 1 or more enemies
     * @return True if it contains an enemy
     */
    public Boolean containsEnemy()
    {
        return Contents.stream().anyMatch(e -> e instanceof Creature);
    }
    
    /**
     * Determines if this tile contains the exit
     * @return True if it contains the exit
     */
    public Boolean containsLadder()
    {
        return Contents.stream().anyMatch(e -> "Ladder".equals(e.getName()));
    }
    
    /**
     * Used to determine if this tile should be visible in the GUI
     * @return True if this tile should be displayed
     */
    public Boolean hasBeenSeen()
    {
        return this.hasBeenSeen;
    }
    
    /**
     * Indicates that this tile should be displayed in the GUI
     */
    public void markSeen()
    {
        hasBeenSeen = true;
    }
    
    /**
     * get the enemies present
     * @return Enemy array of length 3
     */
    public Enemy[] getEnemies()
    {
        Enemy[] enemies = {null, null, null};
        int i = 0;
        for (Entity e : Contents)
        {
            if (e instanceof Enemy enemy)
            {
                enemies[i] = enemy;
                i++;
                if (i > 2)
                {
                    break;
                }
            }
        }
        
        return enemies;
    }
    
    /**
     * Initializes this tile's EncounterEngine and starts combat.
     */
    public void startEncounter()
    {
        if (!this.containsEnemy())
        {
            //return;
        }
        if (engine == null)
        {
            engine = new EncounterEngine(this);
            gui = new EncounterScreen(this);
            engine.setGUI(gui);
        }
        gui.startCombat();
        inEncounter = true;
        //engine.runEncounter(this.getEnemies());
        //System.out.println("Combat Over");
    }
    
    /**
     * Resets all combat-related variables
     */
    public void endEncounter()
    {
        engine = null;
        gui = null;
        inEncounter = false;
        // clear out dead creatures
        for (Entity e : new ArrayList<>(Contents))
        {
            if (e instanceof Creature c && c.getCurrentHP() <= 0)
            {
                Contents.remove(e);
            }
        }
    }
    
    /**
     * Determines whether combat has been started in this tile.
     * @return True if combat has been started
     */
    public Boolean inEncounter()
    {
        return this.inEncounter;
    }
    
    /**
     * Determines whether this tile has a (combat or non-combat) encounter or not
     * @return True if it could spawn an encounter
     */
    public Boolean hasEncounter()
    {
        return !this.Contents.isEmpty();
    }
    
    /**
     * Get this tile's EncounterEngine
     * @return The engine. Can be null.
     */
    public EncounterEngine getEngine()
    {
        return this.engine;
    }
    
    /**
     * Get all the entities inside of this tile
     * @return List of entities
     */
    public List<Entity> getContents()
    {
        return this.Contents;
    }
    
    /**
     * Get the gui associated with this tile and its EncounterEngine
     * @return EncounterScreen object
     */
    public EncounterScreen getGUI()
    {
        return this.gui;
    }
    
    
}
