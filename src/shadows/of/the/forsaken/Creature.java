/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

import java.io.Serializable;

/**
<<<<<<< HEAD
 * A type of entity that has hp and attacks. Used in combat encounters.
=======
 *
 * @author Son Nguyen
 * 
 * Creature class which is a superclass that encompasses Enemy and Player within the Encounter Framework. 
 * Class name is somewhat self explaintory in its role.
>>>>>>> origin/master
 */



/**
 * Construct a creature superclass to repersent an killable entity in the game. Subclasses will be players, and the enimies 
 * Note: Need to make this an abstract class. Should not need to be referenced directly. 
 * 
 * @param maxHP - max health of the creature
 * @param currentHP - current health of hte creature
 * @param hpRegen - how much health a creature regains at the start of its turns 
 * @param strength - integer that amplifies the damage of a physical attaack 
 * @param soul - integer that amplifies damage of a magical attack 
 * @param attackArray - Array which repersents the movesets of the a creature 
 * @param resist - array that repersents the damage resistences of a creature 
 * @param conditions - array that represents the value of a creature's conditions (bleeding, burning, etc.) 
 * @param isDefending - int that represents if a creature is Defending. 0 if not, 1 if it is. 
 */

public class Creature extends Entity implements Serializable {
    protected int maxHP;
    protected int currentHP;
    protected int hpRegen; 
    protected int strength;
    protected int soul;
    protected Attack[] attackArray; 
    protected float[] resist;
    protected int[] conditions = new int[7]; 
    protected int isDefending = 0; 
    
    
    public Creature(String name, String desc, String spriteFileName,int hp, int str, int soul, 
            int regen, Attack[] moveset, float[] resistences){
        super(name, desc, spriteFileName);
        
        currentHP = maxHP = hp;
        strength = str;
        this.soul = soul;
        hpRegen = regen; 
        attackArray = moveset; 
        resist = resistences; 
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
     * Calculates how much damage it takes from an attack, based on resistences. 
     * Consider having a return value to let UI know how much dmg is reduced, in case we want to add it to eventlog.
     * @param rawDamage the incoming damage array
     */
    public void takeDamage (int[] rawDamage){
        
        // If defending, incoming dmg is halved. Then resistences are applied.
        for (int i = 0; i<7; i++){
            currentHP -= (int) (rawDamage[i]*(0.5*isDefending))*(1.0-resist[i]);
        }
        
        // Frozen and Doomed Condition 'go off' when a creature is hit
        frozenTick();
        doomTick();
    }
    
    
    /**
     * when an attack inflicts a condition, like burn. 
     * @param afflicted - an array of conditions an attack would inflict on a creature. 
     * Example: Player gets hit by Great Slash, dealing 5 dmg and 2 Bleed. The 2 Bleed is repersented by the first element in
     * the afflicted array. This is added to the Player's conditions array. 
     */
    public void increaseCondition (int[] afflicted){
        for (int i = 0; i<7; i++){
            conditions[i]+= afflicted[i];
        }       
    }
    
    /**
     * Implementation of attack for enenmy and player 
     * creatures will be very different. Players use a 
     * 'combined magic/physical attack'. Enimies attack
     * is simplier, but need some from of AI to determine 
     * which move to use. All future Sonny's probelms. 
     * @param target is the target of the attack 
     */
    public void attack(Creature target){
        //continue; 
    }
    
    // Override later once abstraction is implemented. 
    
    
    

/**
 *  Below methods are Condition Ticks. Essientally, when a creature is 
 * afflicted by some condition, something occurs to make it 'go off' and deal an effect to the creature 
 */

    /**
     * Bleeding effect. A creature takes damage equal to its current Bleed value (condition[0])
     * Bleed value is then reduce by 1. Occurs at the end of a creature's turn 
     */
    public void bleedTick(){
        currentHP-= conditions[0];
        conditions[0]--; 
    }

    /**
     * Burning effect. A creature takes damage equal to its current Burn value (condition[0])
     * Burned value is then reduce by 1. Occurs at the end of a creature's turn 
     * If a creature has any value of Burn greater than 0, then it recieves no health regen. 
     */
    public void burnTick(){
        currentHP-= conditions[1];
        conditions[1]--; 
    }

    /**
     * Poisoned effect. A creature takes damage equal to its current Poison value (condition[0])
     * Poison value is then reduce by 1. Occurs at the end of a creature's turn 
     * Poison also reduces strength by an amount equal to its value. 
     */
    public void poisonTick(){
        currentHP-= conditions[2];
        conditions[2]--;
    }

    /**
     * Frozened effect. A creature takes damage equal to its current Frozen value (condition[0])
     * Frozen is then reduced to 0. This occurs whenvers the afflicted creature gets hit.
     * Frozen also reduces strength by an amount equal to its value.  
     */
    public void frozenTick(){
        currentHP -= conditions[3];
        conditions[3] = 0; 
    }
    
    /**
     * Stunned effect. Stun also reduces strength and soul by an amount equal to its value. 
     * Lowers whenever a creature gets hit and at the end of a creature's turn. 
     */
    public void stunTick(){
        conditions[4]--;
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
    
}
