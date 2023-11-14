/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.model;

import java.util.List;

/**
 *
 * @author Son Nguyen
 */
public class EncounterEngine {
    private Enemy field[] = new Enemy[3]; 
    private int selectionLayer;
    private final Player player;
    private String eventLog; 
    
    public EncounterEngine()
    {
    this.player = GameState.getInstance().getPlayer();
    selectionLayer = 0; 
    }
    
    
    
    //promptPlayerInput is a placeholder for a therotical method in a GUI class
    // that will take user input. 
    
    public void combatEncounter(Enemy[] enemies){
        if (enemies.length != 3) {
            throw new IllegalArgumentException("Array 'enemies' must have length 3.");
        }
        field = enemies;
        
        
        while (!(this.combatOver())){
            player.beginTurn();
            // promptPlayerInput()
            player.endTurn();
            
            for (int i = 0; i<3; i++){
                field[i].beginTurn();
                CheckIfDead(i);
                field[i].takeTurn(player);
                field[i].endTurn();
                CheckIfDead(i);
            }
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
           
           
       }
       
       else if (selectionLayer == 1){
           if (buttonValue == 3){
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
            if (buttonValue == 3){
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
            if (buttonValue == 3){
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
