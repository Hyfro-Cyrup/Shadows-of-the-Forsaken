/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.model;

/**
 *
 * @author Son Nguyen
 * An object class that repersents a non-combat encounter, which can effect the
 * player depending on what options they choose from the GUI prompt
 * 
 * Example: Treasure chest in a room. Choose to open it or leave/go on. IF they
 * choose to open it, they gain a new magic sword, and their STR increases by 1
 * 
 * So there's not quite a 
 * 
 * This one is actually fairly tricky to implement, especially without 
 * Player Class Implemented
 * 
 * 
 */
public class EncounterObject extends Entity {
    private String encounterText; 
    private int[][] options; 
    private Attack[] attackGift[]; 
    private String[] optionText; 
    private String[] resultText;
    private String [] addInventory; 
    
    EncounterObject(String name, String description, String spriteFile){
        super(name,description, spriteFile);
        
    }
}
