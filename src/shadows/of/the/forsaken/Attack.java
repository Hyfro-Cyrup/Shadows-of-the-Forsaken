/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

/**
<<<<<<< HEAD
 * Represents an attack action with associated damage range, hit probability, and a name.
=======
 *
 * @author Son Nguyen, thanks for Nathan for starting this 
 * 
 * Attack is a class. Handles info on it's own specs. Figures
 * out how much damage it deals. Having attacks as objects
 * allows us to interchange them for monsters and allows 
 * players to 'gain' new moves. 
 * 
 * For details on how this is intended to translate into mechanics 
 * see the Game Manuel I posted in the discord. 
 * 
 * Note To Self: Get around to making constructor
>>>>>>> origin/master
 */

// May need to be private class - Only exists contained within 
// Creature objects. Look more into that.
public class Attack {
    private String Name; // Name is needed to appear in eventlog
    private String infoText; // description text
    private String spriteReference; // In case we 
    private int mightOrmagic; // Magical or Physical Attack
    
<<<<<<< HEAD
    /**
     * Constructs a new Attack instance with the specified parameters.
     *
     * @param min    Minimum damage the attack can inflict.
     * @param max    Maximum damage the attack can inflict.
     * @param chance Probability of the attack being successful.
     * @param _name  Descriptive name for the attack.
     */
    public Attack(int min, int max, double chance, String _name)
    {
        min_damage = min;
        max_damage = max;
        hit_chance = chance;
        name = _name;
=======
    // Range of damage and what type of damage it deals
    // [Physical, Fire, Toxic, Cold, Lightning, Necrotic, Radiant] 
    private int[] damageArrayMin; 
    private int[] damageArrayMax; 
    
    // Afflictions/Conditions the attacks imposes 
    // [Bleed, Burn, Poisoned, Frozened, Stunned, Doomed, Condemned]
    private int[] afflictionArray;
    
    // Self explainatory 
    private int accuracy; 
    private int critChance; 
    
    // Hits is how many time the strikes the attack makes
    // May or may not be feature in final, but putting it here in case
    private int hits; 
    
    // Another 'maybe' feature. Mass Attacks hit all enimies on field 
    private Boolean massAttack; 
    
    // constructor may change, depending on how we chooose to encode the info
    // yeah I know I could use this.variable but I don't want to. 
    Attack(String name, String info, String spriteFile, int isMagic,
            int[] minDmg, int[] maxDmg, int[] effects, int hitChance, int crit, 
            int hitNum, Boolean hitAll){
        
        Name = name;
        infoText = info;
        spriteReference = spriteFile;
        mightOrmagic = isMagic;
        damageArrayMin = minDmg;
        damageArrayMax = maxDmg;
        afflictionArray = effects;
        accuracy = hitChance;
        critChance = crit;
        hits = hitNum;
        massAttack = hitAll; 
        
>>>>>>> origin/master
    }
    
<<<<<<< HEAD
    /**
     * Constructs a default Attack instance representing a club attack.
     * The club attack has a damage range of 1 to 3 with a 75% hit chance.
     */
    public Attack()
    {
        min_damage = 1;
        max_damage = 3;
        hit_chance = 0.75;
        name = "club";
=======
    // Getter methods. Don't need much explaination. 
    public String getName(){
        return Name;
>>>>>>> origin/master
    }
    
<<<<<<< HEAD
    /**
     * Calculates the damage inflicted by the attack. 
     * If the attack misses, the method returns -1.
     *
     * @return Calculated damage value, or -1 if the attack misses.
     */
=======
    public String getSpriteReference(){
        return spriteReference;
    }
    
    public String getInfo(){
        return infoText;
    }
    
    public int getAccuracy(){
        return accuracy; 
    }
    
    public int getCritChance(){
        return critChance;
    }
    
    public int getHits(){
        return hits; 
    }
    
    public int[] getAfflictions(){
        return afflictionArray;
    }
    
    // determines if attack is magical or physical
    // probably better way to do it but this works for now. 
    public int IsMagic(){
    return mightOrmagic; 
    }
    
    // Implement later. I think it's probably best to handle hit/miss externally. 
>>>>>>> origin/master
    public int calculateDamage()
    {
        if (Math.random() > hit_chance){
            // return -1 if the attack misses
            return -1; 
        }
        
        return (int) (min_damage + Math.floor((max_damage - min_damage) * Math.random()));
    }
}
