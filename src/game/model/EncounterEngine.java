/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.model;

import game.gui.EncounterScreen;

/**
 *
 * @author Son Nguyen
 */
public class EncounterEngine {
    private Enemy field[] = new Enemy[3]; 
    private int selectionLayer;
    private final Player player;
    private String eventLog; 
    private EncounterScreen gui;
    
    public EncounterEngine()
    {
    this.player = GameState.getInstance().getPlayer();
    selectionLayer = 0;
    }
    
    /**
     * Set the gui reference after constructor has been called.
     * Necessary due to gui -> hotbar -> engine -> gui circular reference
     * @param gui The EncounterScreen responsible for this encounter
     */
    public void setGUI(EncounterScreen gui)
    {
        this.gui = gui;
    }
    
    public void combatEncounter(Enemy[] enemies){
        System.out.println("confirmed starting combat");
        if (enemies.length != 3) {
            throw new IllegalArgumentException("Array 'enemies' must have length 3.");
        }
        field = enemies;
        
        
        while (!(this.combatOver())){
            System.out.println("Combat not over");
            player.beginTurn();
            gui.waitForPlayer();
            player.endTurn();
            
            for (int i = 0; i<3; i++){
                if (field[i] != null)
                {
                    System.out.println(field[i].getName() + " starting turn");
                    field[i].beginTurn();
                    CheckIfDead(i);
                    field[i].takeTurn(player);
                    field[i].endTurn();
                    CheckIfDead(i);
                }
            }
            
            // REMOVE after making wait function work
            break;
        }
    }
    
    
    public boolean inputTranslator(int buttonValue){    
       if (selectionLayer == 0){
           if (buttonValue == 0){
               selectionLayer+=1; 
               return false; 
           }
           
           if (buttonValue == 1){
               player.defend(); 
               return true; 
           }
           
           if (buttonValue == 2){
               return true; 
               // There's not really a third option for button.   
           }
           
           if (buttonValue == 3){
               return true; 
                 //insert a function for fleeing
           }
           
           if (buttonValue == 4){
               return true;
           }
           
           
       }
       
       else if (selectionLayer == 1){
           if (buttonValue == 4){
                // Back Button
                selectionLayer -= 1;
                return false;
            }
           
           if (player.selectAttack(buttonValue)){
               selectionLayer+=1;
               return false;
           }
           else{
               return false; 
           }
       }
       
       
        else if (selectionLayer == 2){
            if (buttonValue == 4){
                // Back Button
                selectionLayer -= 1;
                return false;
            }
            
            if (player.selectArcana(buttonValue)){
                selectionLayer+=1;
                return false;
            }
            else{
                return false; 
            }
        }
        
        else if (selectionLayer == 3){
            if (buttonValue == 4){
                // Back Button
                selectionLayer -= 1;
                return false;
            }
           
           player.attack(field[buttonValue]);
           return true; 
        }
    
    return true; 
    }
    
    public int getSelectionLayer(){
        return selectionLayer; 
    }
   
    public boolean combatOver(){
        if (player.currentHP <=0){
            return true;
        }
        
        for (int i = 0; i<3; i++){
            if (field[i] != null){
                return false; 
            }
        }
        
        return true;
    }
    
    public void CheckIfDead(int enemyNumber){
        if (field[enemyNumber].currentHP<=0){
            field[enemyNumber] = null; 
        }
    }
    
}
