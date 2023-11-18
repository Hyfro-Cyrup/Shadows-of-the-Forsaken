/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.model;

import game.gui.EncounterScreen;
import java.util.List;
import javax.swing.SwingUtilities;

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
    private DungeonTile tile;
    
    public EncounterEngine(DungeonTile tile)
    {
    this.player = GameState.getInstance().getPlayer();
    this.tile = tile;
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
    
    public void runEncounter(List<Entity> contents){
        try
        {
        SelectionLayer dfault = SelectionLayer.NON_COMBAT;  // COMBAT or NON_COMBAT
        // populate the field
        field = new Enemy[]{null, null, null};
        int j = 0;
        for (Entity e : contents)
        {
            if (e instanceof Enemy enemy)
            {
                field[j] = enemy;
                dfault = SelectionLayer.COMBAT;
                j++;
                if (j > 2)
                {
                    break;
                }
            }
        }
        
        
        while (true){
            player.beginTurn();
            selectionLayer = dfault;
            gui.waitForPlayer();
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
            if (!(this.combatOver())) // combat always over in non-combat encounters
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
                    SwingUtilities.invokeLater( () -> {
                        // I don't like referencing Swing on the domain model side, 
                        // but this block was causing a concurrency issue with selectionLayer
                        selectionLayer = SelectionLayer.POST_COMBAT;
                        gui.outputTranslator("All enemies are vanquished.\n");
                    });
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
    
    public void inputTranslator(int buttonValue){    
        switch (selectionLayer) 
        {
            case COMBAT -> {
                if (buttonValue == 0){
                    selectionLayer = selectionLayer.getNext();
                    return;
                }
                
                if (buttonValue == 1){
                    player.defend();
                    gui.outputTranslator(player, null, DamageCode.DEFENDED);
                    return;
                }    
                
                if (buttonValue == 4){
                    player.runAway(combatOver());
                    return;
                }
                
            }
            case PHYSICAL -> {
                if (buttonValue == 4){
                    // Back Button
                    selectionLayer = selectionLayer.getPrev();
                    return;
                }
                
                if (player.selectAttack(buttonValue)){
                    selectionLayer = selectionLayer.getNext();
                    return;
                }
            }
            case MAGICAL -> {
                if (buttonValue == 4){
                    // Back Button
                    selectionLayer = selectionLayer.getPrev();
                    return;
                }
                
                if (player.selectArcana(buttonValue)){
                    selectionLayer = selectionLayer.getNext();
                    return;
                }
            }
            case ENEMY -> {
                if (buttonValue == 4){
                    // Back Button
                    selectionLayer = selectionLayer.getPrev();
                    return;
                }
                
                int damage = player.attack(field[buttonValue]);
                gui.outputTranslator(player, field[buttonValue], damage);

            }
            case POST_COMBAT -> {
                if (buttonValue == 4){
                    player.runAway(true);
                    return;
                }
            }
            case NON_COMBAT -> {
                if (buttonValue == 0)
                {
                    gui.outputTranslator(player, null, DamageCode.INVESTIGATED);
                    return;
                }
                if (buttonValue == 1)
                {
                    gui.outputTranslator(player, null, DamageCode.TOUCHED);
                    if (tile.containsKey())
                    {
                        player.recieveKey();
                    }
                    if (tile.containsLadder())
                    {
                        if (player.hasKey())
                        {
                            // win
                        }
                        else
                        {
                            // don't
                        }
                    }
                    return;
                }
                
                if (buttonValue == 4) {
                    player.runAway(true);
                    return;
                }
                
            }
        }
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
    
    /**
     * Kills all the enemies
     */
    public void cheatCode()
    {
        for (Enemy e : field)
        {
            if (e != null)
            {
                e.currentHP = 0;
            }
        }
        player.defend(); // must look like player took their turn
    }
    
    
}
