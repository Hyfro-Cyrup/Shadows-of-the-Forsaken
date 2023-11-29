/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.model;

import java.io.Serializable;

/**
 *
 * @author Son Nguyen
 * 
 * Creature class which is a superclass that encompasses Enemy and Player within the Encounter Framework. 
 * Class name is somewhat self explanatory in its role.
 */
public abstract class Creature extends Entity implements Serializable {
    // max health of the creature
    protected int maxHP;
    //current health of the creature
    protected int currentHP;
    //how much health a creature regains at the start of its turns 
    protected int hpRegen; 
    //integer that amplifies the damage of a physical attack 
    protected int strength;
    //integer that amplifies damage of a magical attack 
    protected int soul;
    //Array which represents the attacks of the the creature 
    protected Attack[] attackArray; 
    //array that represents the damage resistances of a creature 
    protected float[] resist;
    //array that represents the value of a creature's conditions (bleeding, burning, etc.) 
    protected int[] conditions = new int[7]; 
    //int that represents if a creature is Defending. 0 if not, 1 if it is. 
    protected int isDefending = 0; 
    
    /**
     * Construct a creature superclass to represent a killable entity in the game. Subclasses will be players, and the enemies 
     * Note: Need to make this an abstract class. Should not need to be referenced directly. 
     * @param name The name of the entity
     * @param desc A short description to be shown
     * @param spriteFileName String filepath to the image of the entity
     * @param hp Max health points to initialize the creature with
     * @param str The creature's strength stat
     * @param soul The creature's soul stat
     * @param regen The creature's hpRegen stat
     * @param moveset Array of Attacks the creature can take
     * @param resistances  Array of damage resistances for each damage type
     */
    public Creature(String name, String desc, String spriteFileName,int hp, int str, int soul, 
            int regen, Attack[] moveset, float[] resistances){
        super(name, desc, spriteFileName);
        
        currentHP = maxHP = hp;
        strength = str;
        this.soul = soul;
        hpRegen = regen; 
        attackArray = moveset; 
        resist = resistances; 
    }
    
    public Creature(String name, String desc, String spriteFileName)
    {
        super(name, desc, spriteFileName);
        //TODO: default values?
    }


    /**
    * On a creature's turn, of its option is to defend. Function sets defend to 1 (is defending) 
    */
    public void defend(){
        isDefending = 1;
    }

    /**
    * Apply automatic effects when a creature begins its turn. 
    * A creature regenerates its hp by its hpRegen value, unless its burning. 
    * If it defended last turn, double the HP Regen
    * End the defending condition 
    */
    public void beginTurn(){
        currentHP += hpRegen*(1%(conditions[1]+1))*(1+isDefending);

        isDefending = 0; 
    }

    /**
    * Apply automatic effects when a creature ends its turn
    * It suffers any effects of Bleeding, Burning, and Poison. 
    * Stun value is reduced 
    */
    public void endTurn(){
        bleedTick();
        burnTick();
        poisonTick();
        stunTick();
    }
    
    
    /**
     * Calculates how much damage it takes from an attack, based on resistances.Consider having a return value to let UI know how much damage is reduced, in case we want to add it to event log.
     * @param rawDamage the incoming damage array
     * @return the total damage taken from the attack
     */
    public int takeDamage (int[] rawDamage){
        
        // If defending, incoming dmg is halved. Then resistences are applied.
        double total = 0.0;
        for (int i = 0; i<7; i++){
            total += (rawDamage[i]*(0.5*(2-isDefending)))*(1.0-resist[i]);
        }
        int finalDamage = (int) total;
        currentHP -= (int) finalDamage;
        
        // Frozen and Doomed Condition 'go off' when a creature is hit
        frozenTick();
        doomTick();
        
        return finalDamage;
    }
    
    
    /**
     * when an attack inflicts a condition, like burn. 
     * @param afflicted - an array of conditions an attack would inflict on a creature. 
     * Example: Player gets hit by Great Slash, dealing 5 dmg and 2 Bleed. The 2 Bleed is represented by the first element in
     * the afflicted array. This is added to the Player's conditions array. 
     */
    public void increaseCondition (int[] afflicted){
        for (int i = 0; i<7; i++){
            conditions[i]+= afflicted[i];
        }       
    }
    
    /**
     * Implementation of attack for enemy and player 
     * creatures will be very different.Players use a 
 'combined magic/physical attack'. Enemies attack
 is simpler, but need some from of AI to determine 
 which move to use. All future Sonny's problems. 
     * @param target is the target of the attack 
     * @return  the total damage dealt
     */
    public abstract int attack(Creature target);
    
    // Override later once abstraction is implemented. 
    
    
    

/**
 *  Below methods are Condition Ticks. Essentially, when a creature is 
 * afflicted by some condition, something occurs to make it 'go off' and deal an effect to the creature 
 */

    /**
     * Bleeding effect. A creature takes damage equal to its current Bleed value (condition[0])
     * Bleed value is then reduce by 1. Occurs at the end of a creature's turn 
     */
    public void bleedTick(){
        currentHP-= conditions[0];
        conditions[0] = Math.max(0,conditions[0] - 1);
    }

    /**
     * Burning effect. A creature takes damage equal to its current Burn value (condition[0])
     * Burned value is then reduce by 1. Occurs at the end of a creature's turn 
     * If a creature has any value of Burn greater than 0, then it receives no health regen. 
     */
    public void burnTick(){
        currentHP-= conditions[1];
        conditions[1] = Math.max(0,conditions[1] - 1);
    }

    /**
     * Poisoned effect. A creature takes damage equal to its current Poison value (condition[0])
     * Poison value is then reduce by 1. Occurs at the end of a creature's turn 
     * Poison also reduces strength by an amount equal to its value. 
     */
    public void poisonTick(){
        currentHP-= conditions[2];
        conditions[2] = Math.max(0,conditions[2] - 1);
    }

    /**
     * Frozen effect. A creature takes damage equal to its current Frozen value (condition[0])
     * Frozen is then reduced to 0. This occurs whenever the afflicted creature gets hit.
     * Frozen also reduces strength by an amount equal to its value.  
     */
    public void frozenTick(){
        currentHP -= conditions[3];
        conditions[3] = Math.max(0,conditions[3] - 1);
    }
    
    /**
     * Stunned effect. Stun also reduces strength and soul by an amount equal to its value. 
     * Lowers whenever a creature gets hit and at the end of a creature's turn. 
     */
    public void stunTick(){
        conditions[4] = Math.max(0,conditions[4] - 1);
    }

    /**
     * Doomed Condition. A creature takes extra damage equal to the Doomed value whenever it takes any damage
     * Lasts for the duration of combat
     */
    public void doomTick(){
        currentHP -= conditions[5];
    }

    /**
     * Condemned Condition. A creature takes extra damage equal to the Condemned value whenever it makes an attack. 
     * Lasts for the duration of combat 
     */
    public void condemnTick(){
        currentHP -= conditions[6];
    }
    
    /**
      * The name of the selected attack, to appear in the GUI
      * @return a formatted string
      */
     public abstract String getSelectedAttackName();
    
     /**
      * Get the current amount of hp
      * @return currentHP
      */
     public int getCurrentHP()
     {
         return currentHP;
     }
     
     /**
      * Get the max amount of hp
      * @return maxHP
      */
     public int getMaxHP()
     {
         return maxHP;
     }
    
     public int[] getStatus()
     {
        return conditions; 
     }
}
