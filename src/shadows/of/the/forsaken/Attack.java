/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

/**
 * Represents an attack action with associated damage range, hit probability, and a name.
 */
public class Attack {
    private int min_damage;
    private int max_damage;
    private double hit_chance;
    private String name;
    
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
    }
    
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
    }
    
    /**
     * Calculates the damage inflicted by the attack. 
     * If the attack misses, the method returns -1.
     *
     * @return Calculated damage value, or -1 if the attack misses.
     */
    public int calculateDamage()
    {
        if (Math.random() > hit_chance){
            // return -1 if the attack misses
            return -1; 
        }
        
        return (int) (min_damage + Math.floor((max_damage - min_damage) * Math.random()));
    }
}
