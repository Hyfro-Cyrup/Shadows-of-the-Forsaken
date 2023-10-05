/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

/**
 * [[insert description of attack class]]
 * @author Nathan Ainslie
 */
public class Attack {
    private int min_damage;
    private int max_damage;
    private double hit_chance;
    private String name;
    
    public Attack(int min, int max, double chance, String _name)
    {
        min_damage = min;
        max_damage = max;
        hit_chance = chance;
        name = _name;
    }
    
    public int calculateDamage()
    {
        // return -1 if the attack misses
        return -1; 
    }
}
