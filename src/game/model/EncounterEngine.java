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
    private SelectionLayer selectionLayer;
    private final Player player;
    private String eventLog; 
    private EncounterScreen gui;
    
    public EncounterEngine()
    {
    this.player = GameState.getInstance().getPlayer();
    selectionLayer = SelectionLayer.BASE;
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
        try
        {
        if (enemies.length != 3) {
            throw new IllegalArgumentException("Array 'enemies' must have length 3.");
        }
        field = enemies;
        
        
        while (true){
            player.beginTurn();
            gui.waitForPlayer();
            selectionLayer = SelectionLayer.BASE;
            player.endTurn();
            
            if (player.ranAway() != DamageCode.FOUGHT)
            {
                gui.outputTranslator(player, null, player.ranAway());
                if (player.ranAway() == DamageCode.RAN_AWAY_SUCCESSFUL)
                {
                    // exit combat
                    return;
                }
            }
            if (!(this.combatOver()))
            {
                for (int i = 0; i<3; i++){
                    CheckIfDead(i);
                    if (field[i] != null)
                    {
                        field[i].beginTurn();
                        int damage = field[i].takeTurn(player);
                        gui.outputTranslator(field[i], player, damage);
                        field[i].endTurn();
                        //CheckIfDead(i);
                        
                    }
                }
                if (allDead())
                {
                    selectionLayer = SelectionLayer.POST_COMBAT;
                }
            }
        }
        
        }
        catch (Exception e)
        {
            // errors in the background thread are normally silent, so we need to help them speak up.
            e.printStackTrace();
        }
    }
    
    public boolean inputTranslator(int buttonValue){    
       if (selectionLayer == SelectionLayer.BASE){
           if (buttonValue == 0){
               selectionLayer = selectionLayer.getNext(); 
               return false; 
           }
           
           if (buttonValue == 1){
               player.defend(); 
               gui.outputTranslator(player, null, DamageCode.DEFENDED);
               return true; 
           }
           
           if (buttonValue == 2){
               return true; 
               // There's not really a third option for button.   
           }
           
           if (buttonValue == 3){
               return true;
           }
           
           if (buttonValue == 4){
               player.runAway(combatOver());
               return true;
           }
           
           
       }
       
       else if (selectionLayer == SelectionLayer.PHYSICAL){
           if (buttonValue == 4){
                // Back Button
                selectionLayer = selectionLayer.getPrev();
                return false;
            }
           
           if (player.selectAttack(buttonValue)){
               selectionLayer = selectionLayer.getNext();
               return false;
           }
           else{
               return false; 
           }
       }
       
       
        else if (selectionLayer == SelectionLayer.MAGICAL){
            if (buttonValue == 4){
                // Back Button
                selectionLayer = selectionLayer.getPrev();
                return false;
            }
            
            if (player.selectArcana(buttonValue)){
                selectionLayer = selectionLayer.getNext();
                return false;
            }
            else{
                return false; 
            }
        }
        
        else if (selectionLayer == SelectionLayer.ENEMY){
            if (buttonValue == 4){
                // Back Button
                selectionLayer = selectionLayer.getPrev();
                return false;
            }
           
           int damage = player.attack(field[buttonValue]);
           gui.outputTranslator(player, field[buttonValue], damage);
           return true; 
        }
    
    return true; 
    }
    
    public int getSelectionLayer(){
        return selectionLayer.ordinal(); 
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
    
    private void CheckIfDead(int enemyNumber){
        if (field[enemyNumber] != null && field[enemyNumber].currentHP<=0){
            field[enemyNumber] = null; 
        }
    }
    
    private Boolean allDead()
    {
        for (Enemy e : field)
        {
            if (e != null)
            {
                return false;
            }
        }
        
        return true;
    }
    
    
}
