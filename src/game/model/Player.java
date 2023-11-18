/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A creature that the player controls and has an inventory.
 */
public class Player extends Creature {
    private Boolean hasExitKey;
    private List<Entity> Inventory;
    public int x, y;
    
    private final Attack[] arcanaArray; 
    
    private final int maxStamina; 
    private int currentStamina;
    private final int regenStamina; 
    
    private final int maxMana;
    private int currentMana;
    private final int regenMana; 
    
    private int Ego;
    private int Will;
    
    private Attack selectedAttack;
    private Attack selectedArcana; 
    
    private Boolean hasTakenTurn = false;
    private int runAwayResult;
    
    
    /**
     * Construct a Player with default parameters
     */
    public Player()
    {
        super("The Player", "The FOOOLISH KNIGHT", "/resources/CombatPlayer.png");
        hasExitKey = false;
        Inventory = new ArrayList<>();
        x = 0;
        y = 0;
        maxStamina = currentStamina = 10;
        regenStamina = 2;
        maxMana = currentMana = 10;
        regenMana = 2;
        currentHP = maxHP = 30;
        resist = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        attackArray = new Attack[]{
            Attack.SLASH, Attack.CLEAVE, Attack.QUICK_STRIKE, Attack.SHIELD_BASH
        };
        arcanaArray = new Attack[]{
            Attack.TRUE_STRIKE, Attack.FLAME, Attack.POISON_SPRAY, Attack.DEATH
        };
        strength = soul = 2;
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
            if (tile.inCombat())
            {
                // This should handle the combat re-entry better than rewriting it here
                GameState.getInstance().loadScreen();
            }
            else if (tile.containsEnemy())
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
        selectedAttack = null;
        selectedArcana = null;
        hasTakenTurn = false;
        runAwayResult = DamageCode.FOUGHT;
    }
    
    
     /**
    * Switch the current 'selected' Technique and make sure the selection is valid
    * If the selection is not, return -1 
    * @param slot - which Technique you are attempting to switch to 
    * @return - true if the selection is valid
    */
    public boolean selectAttack(int slot){
        if ((attackArray[slot].getCost()<=currentStamina) && (attackArray[slot]!=null)){
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
        if ((arcanaArray[slot].getCost()<=currentMana) && (arcanaArray[slot]!=null)){
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
    public int attack(Creature target){
        int accuracyRand = (int)(Math.random() * 100);

        int damageDealt;
        if (accuracyRand<(selectedAttack.getAccuracy()+selectedArcana.getAccuracy())){
            int[] finalDamage = new int[7]; 

            for (int i = 0; i < 7; i++) {
                finalDamage[i] = (selectedAttack.getDamage(i)*(strength-conditions[2]-conditions[3]-conditions[4]))
                        +(selectedArcana.getDamage(i)*soul-conditions[4]);
            }

            damageDealt = target.takeDamage(finalDamage);
            target.increaseCondition (selectedAttack.getAfflictions());
            target.increaseCondition (selectedArcana.getAfflictions());
        }
        else
        {
            damageDealt = DamageCode.MISSED;
        }

            this.condemnTick();
            currentStamina-=selectedAttack.getCost();
            currentMana-=selectedArcana.getCost(); 
            
        hasTakenTurn = true;
            
        return damageDealt;
    }
    
    /**
    * On a creature's turn, of its option is to defend. Function sets defend to 1 (is defending) 
    */
    @Override
    public void defend(){
        isDefending = 1; 
        hasTakenTurn = true;
    }
    
    /**
     * Player has the option to run away from combat, but it doesn't work every time
     * @param combatOver True if combat is already over. Overrides the chance of failure.
     */
    public void runAway(Boolean combatOver)
    {
        if (combatOver || Math.random() < 0.34) // ~1/3 chance
        {
            runAwayResult = DamageCode.RAN_AWAY_SUCCESSFUL;
        }
        else
        {
            runAwayResult = DamageCode.RAN_AWAY_FAILED;
        }
        hasTakenTurn = true;
    }
    
    /**
     * Determine if the player successfully escaped combat this turn.
     * Reuses DamageCode to represent all 3 cases: Didn't run, ran successfully, and ran but failed to get away.
     * @return a DamageCode constant
     */
    public int ranAway()
    {
        return this.runAwayResult;
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
     
     /**
      * The name of the selected attack, to appear in the GUI
      * @return a formatted string
      */
    @Override
    public String getSelectedAttackName()
    {
        return selectedArcana.getName() + "-infused " + selectedAttack.getName();
    }
    
    /**
     * Whether the player has taken their turn or not. Useful for waiting.
     * @return True after the player has made their attack
     */
    public Boolean hasTakenTurn()
    {
        return this.hasTakenTurn;
    }
      
    /**
     * Get a physical attack option at specified index
     * @param index Int position of the attack array
     * @return the Attack at that position of the array
     */
    public Attack getPhysAttack(int index)
    {
        return attackArray[index];
    }
    
    /**
     * Get a magical attack option at specified index
     * @param index Int position of the attack array
     * @return the Attack at that position of the array
     */
    public Attack getMagicAttack(int index)
    {
        return arcanaArray[index];
    }
    
    /**
      * Get the current amount of stamina
      * @return currentStamina
      */
     public int getCurrentStamina()
     {
         return currentStamina;
     }
     
     /**
      * Get the max amount of stamina
      * @return maxStamina
      */
     public int getMaxStamina()
     {
         return maxStamina;
     }
     
     /**
      * Get the current amount of mana
      * @return currentMana
      */
     public int getCurrentMana()
     {
         return currentMana;
     }
     
     /**
      * Get the max amount of mana
      * @return maxMana
      */
     public int getMaxMana()
     {
         return maxMana;
     }
     
     /**
      * Cheap fix to an exploit where saving and reloading allows the player to 
      * gain the benefits of `startTurn` an additional time
      * This negates those benefits, called at load time
      */
     public void loadFix()
     {
        currentStamina -= regenStamina*(1+isDefending);
        currentMana -= regenMana*(1+isDefending);
     }
     
     /**
      * Whether the player has found the exit key or not.
      * @return True if the player can exit
      */
     public Boolean hasKey()
     {
         return this.hasExitKey;
     }
     
     /**
      * Indicate that the player has found the exit key
      */
     public void recieveKey()
     {
         this.hasExitKey = true;
     }
    
}
