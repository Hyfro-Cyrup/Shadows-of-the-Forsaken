/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.model;

import game.gui.EncounterScreen;
import static game.model.SelectionLayer.ATK_ENEMY;
import static game.model.SelectionLayer.COMBAT;
import static game.model.SelectionLayer.INSPECT_ENEMY;
import static game.model.SelectionLayer.MAGICAL;
import static game.model.SelectionLayer.NON_COMBAT;
import static game.model.SelectionLayer.PHYSICAL;
import static game.model.SelectionLayer.POST_COMBAT;
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
                gui.outputTranslator(player,null,DamageCode.STATUS_EFFECTS);
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
                        break;
                    }
                }
                if (!(this.combatOver())) // combat always over in non-combat encounters
                {
                    for (int i = 0; i<3; i++){
                        CheckIfDead(i);
                        if (field[i] != null)
                        {
                            gui.outputTranslator(field[i],null,DamageCode.STATUS_EFFECTS);
                            field[i].beginTurn();
                            int damage = field[i].takeTurn(player);
                            gui.outputTranslator(field[i], player, damage);
                            field[i].endTurn();

                        }
                    }
                    if (player.currentHP <= 0)
                    {
                        GameState.getInstance().Lose();
                        break;
                    }
                    if (allDead())
                    {
                        SwingUtilities.invokeLater( () -> {
                            // I don't like referencing Swing on the domain model side, 
                            // but this block was causing a concurrency issue with selectionLayer
                            selectionLayer = SelectionLayer.POST_COMBAT;
                            gui.outputTranslator("All enemies are vanquished.");
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
                    // Go to attack selection
                    selectionLayer = selectionLayer.getNext();
                    return;
                }
                
                if (buttonValue == 1){
                    // Action: Defend
                    player.defend();
                    gui.outputTranslator(player, null, DamageCode.DEFENDED);
                    return;
                }    
                
                if (buttonValue == 2){
                    selectionLayer = SelectionLayer.INSPECT_ENEMY;
                }
                
                if (buttonValue == 4){
                    // Action: Flee
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
            case ATK_ENEMY -> {
                if (buttonValue == 4){
                    // Back Button
                    selectionLayer = selectionLayer.getPrev();
                    return;
                }
                
                if (buttonValue < 3 && field[buttonValue] != null){
                    int damage = player.attack(field[buttonValue]);
                    gui.outputTranslator(player, field[buttonValue], damage);
                }
            }
            
            case INSPECT_ENEMY -> {
                if (buttonValue == 4){
                    // Back Button
                    selectionLayer = SelectionLayer.COMBAT;
                    return;
                }
                if (buttonValue < 3 && field[buttonValue] != null){
                    gui.outputTranslator(null,field[buttonValue],DamageCode.INSPECT);
                    selectionLayer = SelectionLayer.COMBAT;
                }
            }
            
            case POST_COMBAT -> {
                if (buttonValue == 4){
                    // Action: Leave
                    player.runAway(true);
                    return;
                }
            }
            case NON_COMBAT -> {
                if (buttonValue == 0){
                    // Action: Investigate
                    gui.outputTranslator(player, null, DamageCode.INVESTIGATED);
                    return;
                }
                if (buttonValue == 1){
                    // Action: Interact
                    gui.outputTranslator(player, null, DamageCode.TOUCHED);
                    if (tile.containsKey())
                    {
                        player.recieveKey();
                    }
                    else if (tile.containsLadder() && player.hasKey())
                    {
                        tile.unlockExit();
                    }
                    return;
                }
                if (buttonValue == 2 && tile.exitUnlocked()){
                    // Action: Ascend
                    gui.outputTranslator(null, null, DamageCode.GAME_WON);
                    GameState.getInstance().Win();
                    player.runAway(true); // need to exit combat
                    
                }
                
                if (buttonValue == 4) {
                    // Action: Leave
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
