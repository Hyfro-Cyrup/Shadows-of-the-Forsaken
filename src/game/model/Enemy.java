/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.model;

import java.util.List;

/**
 *
 * @author Son Nguyen
 */
public class Enemy extends Creature {
    
    private double[] decisionMatrix; 
    private Attack selectedAttack; 
            
     /**
     * Construct a Player with specified parameters
     * 
     * @param _spritePath String filepath to the image of the entity
     * @param hp The health of the creature
     * @param attacks A list of Attack objects
     */
    
    public Enemy(String _spritePath, int hp, List<Attack> attacks)
    {
        super("The Player", "The FOOOLISH KNIGHT", _spritePath);
    }
    
    
    public void takeTurn(Player player){
        double Rand = Math.random();
        double sumProb = decisionMatrix[0]+decisionMatrix[1]+decisionMatrix[2]
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
        }
        
    }

    
    @Override     
    public void attack(Creature target){
    int accuracyRand = (int)(Math.random() * 100);
    int modifier; 
   
    if (selectedAttack.IsMagic() == 1){
        modifier = soul-conditions[4];
    }
    else{
        modifier = strength-conditions[2]-conditions[3]-conditions[4];
    }
    
    if (accuracyRand<(selectedAttack.getAccuracy())){
        int[] finalDamage = new int[7]; 
        
        for (int i = 0; i < 7; i++) {
            finalDamage[i]+=(selectedAttack.getDamage(i)*modifier);
        }
        
        target.takeDamage(finalDamage);
        target.increaseCondition (selectedAttack.getAfflictions());
    }
    
        this.condemnTick();
    }
}
