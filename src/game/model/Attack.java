/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.model;

import java.io.Serializable;

/**
 * Construct a Attack Class to act as a 'move' in the moveset of creatures. Allows for interchangeable movesets 
 * 
 */
public class Attack implements Serializable{
    // name of the attack 
    private final String Name; 
    // description of the attack 
    private final String infoText; 
    // String filepath of the object 
    private final String spriteReference; 
    // whether or not the attack is magical of physical. 0 is physical. 1 is magical
    private final int mightOrMagic; 
    private final int Cost; 
    // Minimum damage range of the attack - An array because multiple damage types 
    private final int[] damageArrayMin; 
    // Maximum damage range of the attack [Physical, Fire, Toxic, Cold, Lightning, Necrotic, Radiant] 
    private final int[] damageArrayMax; 
    // Array which represents the afflictions the attack imposes on a target on hit.
    private final int[] afflictionArray;
    // base chance that the attack hits
    private final int accuracy; 
    // base chance that the attack crits, doing double damage 
    private final int critChance; 
    // how many times the attack can make a strike 
    private final int hits; 
    // if the attack hits all enemies in field 
    private final Boolean massAttack; 
    
    /**
     * Construct a Attack Class to act as a 'move' in the moveset of creatures.Allows for interchangeable movesets 
     * 
     * @param name - name of the attack 
     * @param info - description of the attack 
     * @param spriteFile - String filepath of the object 
     * @param isMagic - whether or not the attack is magical of physical. 0 is physical. 1 is magical
     * @param price - the cost of the attack
     * @param minDmg - Minimum damage range of the attack - An array because multiple damage types 
     * @param maxDmg - Maximum damage range of the attack [Physical, Fire, Toxic, Cold, Lightning, Necrotic, Radiant] 
     * @param effects - Array which represents the afflictions the attack imposes on a target on hit.
     * @param hitChance - base chance that the attack hits
     * @param crit - base chance that the attack crits, doing double damage 
     * @param hitNum - how many times the attack can make a strike 
     * @param hitAll - if the attack hits all enemies in field 
     */
    Attack(String name, String info, String spriteFile, int isMagic, int price,
            int[] minDmg, int[] maxDmg, int[] effects, int hitChance, int crit, 
            int hitNum, Boolean hitAll){
        
        Name = name;
        infoText = info;
        spriteReference = spriteFile;
        mightOrMagic = isMagic;
        Cost = price;
        damageArrayMin = minDmg;
        damageArrayMax = maxDmg;
        afflictionArray = effects;
        accuracy = hitChance;
        critChance = crit;
        hits = hitNum;
        massAttack = hitAll; 

    }
    
    
// Getter Methods 
    public String getName(){
        return Name;
    }

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
    
    public int IsMagic(){
        return mightOrMagic; 
    }
 
    public int getCost(){
        return Cost; 
    }
    
    public int getDamage(int type){
        int range = damageArrayMax[type]-damageArrayMin[type];
        int damage = (int) (Math.random() * range)+damageArrayMin[type];
        return damage; 
    }
    
    /*  Named attacks because these are complicated to construct    */
    
    // TODO: change these filepaths out for real ones
    // Physical
    public static final Attack SLASH = new Attack(
            "Slash", "medium damage and accuracy", "/resources/SlashIcon.png", 0, 2, 
            new int[]{1, 0, 0, 0, 0, 0, 0}, new int[]{6, 0, 0, 0, 0, 0, 0}, 
            new int[]{0, 0, 0, 0, 0, 0, 0}, 80, 5, 1, false
    );
    public static final Attack CLEAVE = new Attack(
            "Cleave", "high damage, low accuracy", "/resources/CleaveIcon.png", 0, 4, 
            new int[]{2, 0, 0, 0, 0, 0, 0}, new int[]{8, 0, 0, 0, 0, 0, 0}, 
            new int[]{0, 0, 0, 0, 0, 0, 0}, 70, 5, 1, false
    );
    public static final Attack QUICK_STRIKE = new Attack(
            "Quick Strike", "low damage, high accuracy", "/resources/QuickStrikeIcon.png", 0, 2, 
            new int[]{1, 0, 0, 0, 0, 0, 0}, new int[]{4, 0, 0, 0, 0, 0, 0}, 
            new int[]{0, 0, 0, 0, 0, 0, 0}, 100, 5, 1, false
    );
    public static final Attack SHIELD_BASH = new Attack(
            "Shield Bash", "low damage, stuns enemies", "/resources/SheildBashIcon.png", 0, 3, 
            new int[]{1, 0, 0, 0, 0, 0, 0}, new int[]{6, 0, 0, 0, 0, 0, 0}, 
            new int[]{0, 0, 0, 0, 2, 0, 0}, // 2 Stun
            60, 5, 1, false
    );
    
    // Magical
    public static final Attack TRUE_STRIKE = new Attack(
            "True Strike", "increases accuracy", "/resources/TrueStrikeIcon.png", 1, 3, 
            new int[]{0, 0, 0, 0, 0, 0, 0}, new int[]{0, 0, 0, 0, 0, 0, 0}, 
            new int[]{0, 0, 0, 0, 0, 0, 0}, 50, 20, 1, false
    );
    public static final Attack FLAME = new Attack(
            "Flame", "fire damage and burn", "/resources/FlameIcon.png", 1, 3, 
            new int[]{0, 1, 0, 0, 0, 0, 0}, new int[]{0, 4, 0, 0, 0, 0, 0}, 
            new int[]{0, 2, 0, 0, 0, 0, 0}, // 2 Burn
            0, 0, 1, false
    );
    public static final Attack DEATH = new Attack(
            "Death", "high necrotic damage at a price", "/resources/DeathIcon.png", 1, 6, 
            new int[]{0, 0, 0, 0, 0, 4, 0}, new int[]{0, 0, 0, 0, 0, 8, 0}, 
            new int[]{0, 0, 0, 0, 0, 0, 0}, 0, 10, 1, false
    );
    public static final Attack POISON_SPRAY = new Attack(
            "Poison Spray", "toxic damage and shock", "/resources/PoisonSprayIcon.png", 1, 4, 
            new int[]{0, 0, 2, 0, 0, 0, 0}, new int[]{0, 0, 4, 0, 0, 0, 0}, 
            new int[]{0, 0, 2, 0, 0, 0, 0}, // 2 Poison
            0, 0, 1, false
    );
    
    
}
