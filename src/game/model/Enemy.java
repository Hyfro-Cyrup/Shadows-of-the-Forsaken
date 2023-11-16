/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.model;

/**
 *
 * @author Son Nguyen
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
    public Enemy(String name, String desc, String spriteFileName,int hp, int str, int soul, 
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
    
    
    public int takeTurn(Player player){
        double Rand = Math.random();
        /*double sumProb = decisionMatrix[0]+decisionMatrix[1]+decisionMatrix[2]
                +decisionMatrix[3]+decisionMatrix[4]; 
        
        double range1 = decisionMatrix[0]/sumProb;
        double range2 = range1+decisionMatrix[1]/sumProb;
        double range3 = range2+decisionMatrix[2]/sumProb;
        double range4 = range3+decisionMatrix[3]/sumProb;
        
        if (Rand<(range1)){
            this.defend();
        }
        
        else if ((range1<=Rand) && (Rand<range2)){
            selectedAttack = attackArray[0];
            this.attack(player);
        }
        
        else if ((range2<=Rand) && (Rand<range3)){
            selectedAttack = attackArray[1];
            this.attack(player);
        }
        
        else if ((range3<=Rand) && (Rand<range4)){
            selectedAttack = attackArray[2];
            this.attack(player);
        }
   
        else if (Rand>=range4){
            selectedAttack = attackArray[3];
            this.attack(player);
        }*/
        
        // I didn't want to fully uproot Son's hard work, but need a better version
        // that works with variable length array, though it does assume that it's normalized
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
        selectedAttack = attackArray[decisionMatrix.length - 1];
        return this.attack(player);
    }

    
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
    
    public static final Enemy SKELETON = new Enemy("Skeleton", 
            "Boney guy", "/resources/Skeleton.png",
            30, 2, 0, 1, new Attack[]{
                Attack.SLASH, Attack.QUICK_STRIKE
            }, 
            new float[]{0.25f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.75f}); 
}
