/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.model;

/**
 * Type of entity with attacks, stats, and decisions. The opposition. 
 */
public class Enemy extends Creature {
    
    private final double[] decisionMatrix; 
    private Attack selectedAttack; 
            
    /**
     * Construct an Enemy with specified parameters
     * 
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
    Enemy(String name, String desc, String spriteFileName,int hp, int str, int soul, 
            int regen, Attack[] moveset, float[] resistances)
    {
        super(name, desc, spriteFileName, hp, str, soul, regen, moveset, resistances);
        //TODO: decisionMatrix needs to be initialized, but I don't know how you want them to work. 
        decisionMatrix = new double[moveset.length + 1];
        for (int i = 0; i < moveset.length; i++)
        {
            decisionMatrix[i] = 1.0 / (moveset.length + 1);
        }
    }
    
    /**
     * Get a deep copy of this entity instead of having multiple references to it
     * @return a new Enemy
     */
    public Enemy copy()
    {
        return new Enemy(this.getName(), this.getDescription(), this.getSpriteReference(), this.maxHP, this.strength, this.soul,
        this.hpRegen, this.attackArray, this.resist);
    }
    
    /**
     * Does the decision-making for an Enemy and executes the chosen action
     * Assumes the decisionMatrix sums to 1
     * @param player reference to the player for damage purposes
     * @return the damage dealt or DamageCode
     */
    public int takeTurn(Player player){
        double Rand = Math.random();
        double prob = 0.0;
        for (int i = 0; i < decisionMatrix.length - 1; i++)
        {
            prob += decisionMatrix[i];
            if (Rand < prob)
            {
                if (i == 0)
                {
                    this.defend();
                    return DamageCode.DEFENDED;
                }
                else
                {
                    selectedAttack = attackArray[i];
                    return this.attack(player);
                }
            }
        }
        selectedAttack = attackArray[attackArray.length - 1];
        
        return this.attack(player);
    }

    /**
     * Executes an attack against a target (the player). 
     * Assumes selectedAttack is not null because takeTurn has been called. 
     * @param target the Creature to attack
     * @return the damage dealt
     */
    @Override     
    public int attack(Creature target){
        int accuracyRand = (int)(Math.random() * 100);
        int modifier; 

        if (selectedAttack.IsMagic() == 1){
            modifier = Math.max(0, soul-conditions[4]);
        }
        else{
            modifier = Math.max(0, strength-conditions[2]-conditions[3]-conditions[4]);
        }
        int damageDealt;
        if (accuracyRand<(selectedAttack.getAccuracy())){
            int[] finalDamage = new int[7]; 

            for (int i = 0; i < 7; i++) {
                finalDamage[i]=(selectedAttack.getDamage(i)*modifier);
            }

            damageDealt = target.takeDamage(finalDamage);
            target.increaseCondition (selectedAttack.getAfflictions());
        }
        else
        {
            damageDealt = DamageCode.MISSED;
        }
    
        this.condemnTick();
        return damageDealt;
    }
    
    /**
      * The name of the selected attack, to appear in the GUI
      * @return a formatted string
      */
    @Override
    public String getSelectedAttackName()
    {
        return selectedAttack.getName() + " attack";
    }
    
    /*  predefined enemies  */
    
    /**
     * Predefined enemy with basic stats and attacks
     */
    public static final Enemy SKELETON = new Enemy("Skeleton", 
            "Boney guy", "/resources/Skeleton.png",
            30, 2, 0, 1, new Attack[]{
                Attack.SLASH, Attack.QUICK_STRIKE
            }, 
            new float[]{0.25f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.75f}); 
}
