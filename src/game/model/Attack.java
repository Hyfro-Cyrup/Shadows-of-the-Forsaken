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
    /**
     * name of the attack 
     */
    private final String Name; 
    /**
     * description of the attack 
     */
    private final String infoText; 
    /**
     * String filepath of the object 
     */
    private final String spriteReference; 
    /**
     * whether or not the attack is magical of physical. 0 is physical. 1 is magical
     */
    private final int mightOrmagic; 
    private final int Cost; 
    /**
     * Minimum damage range of the attack - An array because multiple damage types 
     */
    private final int[] damageArrayMin; 
    /**
     * Maximum damage range of the attack [Physical, Fire, Toxic, Cold, Lightning, Necrotic, Radiant] 
     */
    private final int[] damageArrayMax; 
    /**
     * Array which represents the afflictions the attack imposes on a target on hit.
     */
    private final int[] afflictionArray;
    /**
     * base chance that the attack hits
     */
    private final int accuracy; 
    /**
     * base chance that the attack crits, doing double damage 
     */
    private final int critChance; 
    /**
     * how many times the attack can make a strike 
     */
    private final int hits; 
    /**
     * if the attack hits all enemies in field 
     */
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
        mightOrmagic = isMagic;
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
        return mightOrmagic; 
    }
 
    public int getCost(){
        return Cost; 
    }
    
    public int getDamage(int type){
        int range = damageArrayMax[type]-damageArrayMin[type];
        int damage = (int) (Math.random() * range)+damageArrayMin[type];
        return damage; 
    }
    
}
