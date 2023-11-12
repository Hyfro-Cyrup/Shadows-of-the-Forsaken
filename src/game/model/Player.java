/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.model;

import java.util.List;

/**
 * A creature that the player controls and has an inventory.
 */
public class Player extends Creature {
    private Boolean hasExitKey;
    private List<Entity> Inventory;
    public int x, y;
    
    private Attack[] arcanaArray; 
    
    private int maxStamina; 
    private int currentStamina;
    private int regenStamina; 
    
    private int maxMana;
    private int currentMana;
    private int regenMana; 
    
    private int Ego;
    private int Will;
    
    private Attack selectedAttack;
    private Attack selectedArcana; 
    
    
    /**
     * Construct a Player with specified parameters
     * 
     * @param _spritePath String filepath to the image of the entity
     * @param hp The health of the creature
     * @param attacks A list of Attack objects
     * @param key Boolean whether or not the exit key is in the player's inventory
     * @param inventory List of entities that the player has with them. Currently unused.
     */
    public Player(String _spritePath, int hp, List<Attack> attacks, Boolean key, List<Entity> inventory)
    {
        super("The Player", "The FOOOLISH KNIGHT", _spritePath);
        hasExitKey = key;
        Inventory = inventory;
        x = 0;
        y = 0;
    }
    
    /**
     * Mutator to move the player by some `dx` tiles in the x direction and `dy` tiles in the y direction
     * @param dx Number of tiles to move in x direction
     * @param dy Number of tiles to move in y direction
     * @param map Collection of DungeonTiles used for movement bounds
     */
    public void move(int dx, int dy, DungeonTile[][] map){
        int nx = x + dx;
        int ny = y + dy;
        DungeonTile tile = map[nx][ny];
        if (-1 < nx && nx < map.length && 
            -1 < ny && ny < map[0].length &&
            tile != null)
        {
            x = nx;
            y = ny;
            if (tile.containsEnemy())
            {
                tile.startCombat();
            }
        }
    }
    
    /**
    * Apply automatic effects when a creature begins its turn. 
    * A creature regenerates its hp by its hpRegen value, unless its burning. 
    * If it defended last turn, double the HP Regen
    * End the defending condition 
    */
    
    @Override 
    public void beginTurn(){
        currentHP += hpRegen*(1%(conditions[1]+1))*(1+isDefending);
        currentStamina += regenStamina*(1+isDefending);
        currentMana += regenMana*(1+isDefending);
        
        if (currentHP>maxHP){
            currentHP=maxHP;
        }
        
        if (currentStamina>maxStamina){
            currentStamina=maxStamina;
        }
        
        if (currentMana>maxMana){
            currentMana=maxMana;
        }
        isDefending = 0; 
    }
    
    
     /**
    * Switch the current 'selected' Technique and make sure the selection is valid
    * If the selection is not, return -1 
    * @param slot - which Technique you are attempting to switch to 
    * @return - true if the selection is valid
    */
    public boolean selectAttack(int slot){
        if ((attackArray[slot].getCost()<currentStamina) && (attackArray[slot]!=null)){
            selectedAttack = attackArray[slot];
            return true; 
        }
        else{
            return false; 
        }
    }
   
      /**
    * Switch the current 'selected' Arcana and make sure the selection is valid
    * If the selection is not, return false
    * @param slot - which Arcana you are attempting to switch to
    * @return - true if the selection is valid
    */
    public boolean selectArcana(int slot){
        if ((arcanaArray[slot].getCost()<currentMana) && (arcanaArray[slot]!=null)){
            selectedArcana = arcanaArray[slot];
            return true; 
        }
        else{
            return false; 
        }
    }
    
    
    @Override 
    /**
    * Attack a target creature with the currently selected Technique and Arcana 
    * If the attack hits, deal damage and apply conditions.
    * Stamina and Mana are spent as usual. It is assumed the selected attacks 
    * are below the threshold. 
    * @param target - creature that is being targeted by the player 
    */
    public void attack(Creature target){
    int accuracyRand = (int)(Math.random() * 100);
    
    if (accuracyRand<(selectedAttack.getAccuracy()+selectedArcana.getAccuracy())){
        int[] finalDamage = new int[7]; 
        
        for (int i = 0; i < 7; i++) {
            finalDamage[i]+=(selectedAttack.getDamage(i)*(strength-conditions[2]-conditions[3]-conditions[4]))
                    +(selectedArcana.getDamage(i)*soul-conditions[4]);
        }
        
        target.takeDamage(finalDamage);
        target.increaseCondition (selectedAttack.getAfflictions());
        target.increaseCondition (selectedArcana.getAfflictions());
    }
    
        this.condemnTick();
        currentStamina-=selectedAttack.getCost();
        currentMana-=selectedArcana.getCost(); 
    }
    
    
    
      /**
    * Add an attack or switch out an attack to the player's Technique or Arcana 
    * moveset. 
    * @param techOrArcane - is an Arcana or a Technique 
    * @param slot - which slot are your inserting it into. 
    * @param newAttack - the Attack you are inserting.
    */
     public void exchangeAttack(int techOrArcane, int slot, Attack newAttack){
         if (techOrArcane == 0){
            attackArray[slot] = newAttack;
         }
         
         else{
            arcanaArray[slot] = newAttack; 
        }
     }
    
}
