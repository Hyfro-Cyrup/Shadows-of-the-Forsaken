/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

import java.util.List;

/**
 * [[insert description of creature class]]
 * @author Nathan Ainslie
 */
public class Creature extends Entity {
    private int HP;
    private List<Attack> Attacks;
    
    public Creature(String _name, String _description, String _spritePath, int hp, List<Attack> attacks)
    {
        super(_name, _description, _spritePath);
        HP = hp;
        Attacks = attacks;
    }
    
    
    
}
