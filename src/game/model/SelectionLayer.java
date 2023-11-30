 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package game.model;

/**
 * Keep track of all the different layers of options in an encounter.
 * Used by EncounterEngine and Hotbar
 * Easier to remember than an int
 */
public enum SelectionLayer {

    /**
     * Base combat selection
     */
    COMBAT,
    /**
     * Select physical attack
     */
    PHYSICAL,
    /**
     * Select magical attack
     */
    MAGICAL,
    /**
     * Select which enemy to attack
     */
    ATK_ENEMY,
    /**
     * Combat is over
     */
    POST_COMBAT,
    /**
     * This isn't a combat encounter
     */
    NON_COMBAT,
    /**
     * Select which enemy to inspect
     */
    INSPECT_ENEMY;
    
    /**
     * Get the next option in order. 
     * Replaces previous instances of ++ and +=1 from when this was an int
     * @return the next SelectionLayer
     */
    public SelectionLayer getNext()
    {
        var values = SelectionLayer.values();
        return values[(this.ordinal() + 1) % values.length];
    }
    
    /**
     * Get the previous option in order. 
     * Replaces previous instances of -- and -=1 from when this was an int
     * @return the previous SelectionLayer
     */
    public SelectionLayer getPrev()
    {
        var values = SelectionLayer.values();
        return values[(this.ordinal() - 1) % values.length];
    }
}
