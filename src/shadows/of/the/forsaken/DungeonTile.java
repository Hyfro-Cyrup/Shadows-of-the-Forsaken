/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

import static java.util.Collections.emptyList;
import java.util.List;

/**
 * [[add description of dungeon tile class]]
 * @author Nathan Ainslie
 */
public class DungeonTile {
    private List<Entity> Contents;
    private Boolean hasBeenSeen;
    
    public DungeonTile()
    {
        Contents = emptyList();
        hasBeenSeen = false;
    }
    
    public DungeonTile(List<Entity> contents, Boolean seen)
    {
        Contents = contents;
        hasBeenSeen = seen;
    }
}
