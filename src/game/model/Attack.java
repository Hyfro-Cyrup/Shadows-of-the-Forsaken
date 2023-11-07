/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.model;

import java.io.Serializable;

/**
 * Construct a Attack Class to act as a 'move' in the moveset of creatures. Allows for interchangeable movesets 
 * 
 * @param Name - name of the attack 
 * @param infoText - description of the attack 
 * @param spriteReference - String filepath of the object 
 * @param mightOrmagic - whether or not the attack is magical of physical. 0 is physical. 1 is magical
 * @param damageArrayMin - Minimum damage range of the attack - An array because multiple damage types 
 * @param damageArrayMax - Maxinumum damage range of the attack [Physical, Fire, Toxic, Cold, Lightning, Necrotic, Radiant] 
 * @param afflictionArray - Array which repersents the afflictions the attack imposes on a target on hit.
 * @param accuracy - base chance that the attack hits
 * @param critChance - base chance that the attack crits, doing double damage 
 * @param hits - how many times the attack can make a strike 
 * @param massAttack - if the attack hits all enimies in field 
 */
public class Attack implements Serializable{
    private String Name; 
    private String infoText; 
    private String spriteReference; 
    private int mightOrmagic; 
    private int Cost; 
    private int[] damageArrayMin; 
    private int[] damageArrayMax; 
    private int[] afflictionArray;
    private int accuracy; 
    private int critChance; 
    private int hits; 
    private Boolean massAttack; 
    
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
