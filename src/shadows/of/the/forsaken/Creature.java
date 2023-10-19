/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

/**
 *
 * @author Son Nguyen
 * 
 * Creature class which is a superclass that encompasses Enemy and Player within the Encounter Framework. 
 * Class name is somewhat self explaintory in its role.
 */

public abstract class Creature extends Entity {
    // Self evident 
    private int maxHP;
    private int currentHP;
    private int hpRegen; 
    
    // STR and SOUL will modify attack, depending if it's magic 
    // or Physical. 
    private int Strength;
    private int Soul;
    
    // creature's moveset
    private Attack[] attackArray; 
    
    // creature's damage resistences to each dmg type. Goes from 0.0 to 1.0.
    private float[] resist;
    
    // conditions creature is afflicted with. See Attack Class/Game Manuel for 
    // details 
    private int[] conditions = new int[7]; 
    
    // If a creature is defending or not. More convienient as int than Bool. 
    private int isDefending = 0; 
    
    
    Creature(String name, String spriteFileName,int hp, int str, int soul, 
            int regen, Attack[] moveset, float[] resistences){
        super(name,spriteFileName);
        
        currentHP = maxHP = hp;
        Strength = str;
        Soul = soul;
        hpRegen = regen; 
        attackArray = moveset; 
        resist = resistences; 
    }
    
 
    public void defend(){
        isDefending = 1; 
    }
    
    public void beginTurn(){
        // Hp regen is default begin of turn for all creatures
        // If a creature used it's turn to defend, double regeneration
        // However, if a creature is burning, no regeneration
        currentHP += hpRegen*(1%(conditions[1]+1))*(2*isDefending);
       
        isDefending = 0; 
    }
    
    public void endTurn(){
        // These condition go off all and reduce at endturns
        bleedTick();
        burnTick();
        poisonTick();
        stunTick();
    }
    
    
    // Calculates how much damage it takes from an attack,
    // based on resistences. 
    // Consider having a return value to let UI know how 
    // much dmg is reduced, in case we want to add it to eventlog.
    public void takeDamage (int[] rawDamage){
        
        // Better pratice is to use size()/len instead of static value but 
        // we know both damage and resistence arrays should be 7 in length.
        // If not, something is very wrong. 
        
        // If defending, incoming dmg is halved. Then resistences are applied.
        for (int i = 0; i<7; i++){
            currentHP -= (int) (rawDamage[i]*(0.5*isDefending))*(1.0-resist[i]);
        }
        
        // Frozen and Doomed Condition 'go off' when a creature is hit
        frozenTick();
        doomTick();
    }
    
    
    // when an attack inflicts a condition, like burn. 
    public void increaseCondition (int[] afflicted){
        for (int i = 0; i<7; i++){
            conditions[i]+= afflicted[i];
        }       
    }
    
    // Implementation of attack for enenmy and player 
    // creatures will be very different. Players use a 
    // 'combined magic/physical attack'. Enimies attack
    // is simplier, but need some from of AI to determine 
    // which move to use. All future Sonny's probelms. 
    public abstract String attack(Creature target);
    
    // May return text for the UI. Or int. Figure that out later. 
    
    
    
// Condition Ticks. Essientally, when a creature is 
// afflicted by some condition, something occurs to make it 
// 'go off'. Description in Game Manuel.
    public void bleedTick(){
        currentHP-= conditions[0];
        conditions[0]--; 
    }
    
    public void burnTick(){
        currentHP-= conditions[1];
        conditions[1]--; 
    }
    
    public void poisonTick(){
        currentHP-= conditions[2];
        conditions[2]--;
    }
    
    public void frozenTick(){
        currentHP -= conditions[3];
        conditions[3] = 0; 
    }
    
    public void stunTick(){
        conditions[4]--;
    }
    
    public void doomTick(){
        currentHP -= conditions[5];
    }
    
    public void condemnTick(){
        currentHP -= conditions[6];
    }
    
}
