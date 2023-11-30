/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package game.model;

/**
 * Keeps track of specific negative damage values used to signify certain events
 * Almost an enum
 */
public class DamageCode 
{
    /**
     * Indicates the player defended instead of attacking or running
     */
    public static final int DEFENDED    = -1;
    /**
     * Indicates the player attacked, but missed (different than 0 damage)
     */
    public static final int MISSED      = -2;
    /**
     * Used by Player and EncounterEngine when determining if the player ran away and how successful they were.
     */
    public static final int FOUGHT      = -3;
    /**
     * Used by Player and EncounterEngine when determining if the player ran away and how successful they were.
     */
    public static final int RAN_AWAY_SUCCESSFUL = -4;
    /**
     * Used by Player and EncounterEngine when determining if the player ran away and how successful they were.
     */
    public static final int RAN_AWAY_FAILED     = -5;
    /**
     * Indicates that the player investigated (non-combat)
     */
    public static final int INVESTIGATED        = -6;
    /**
     * Indicates the player interacted (non-combat)
     */
    public static final int TOUCHED     = -7;
    /**
     * Indicates the player won the game (non-combat)
     */
    public static final int GAME_WON    = -8;
    /** 
     * Tells the GUI to print out information about status effects
     */
    public static final int STATUS_EFFECTS      = -9;
    /**
     * Indicates that the player inspected an enemy
     */
    public static final int INSPECT     = -10;
    
    
    private DamageCode(){}
}
