/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.gui;

import game.model.EncounterEngine;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The bar containing buttons at the bottom of the EncounterScreen
 */
public class Hotbar extends JPanel {
    JButton Buttons[];
    JPanel innerPanel;
    
    /**
     * Initialize the Hotbar with an array of buttons.Each button's event listener calls a method of the EncounterEngine 
     * @param engine The EncounterEngine running the current combat
     */
    public Hotbar(EncounterEngine engine)
    {
        // initialize the JPanel
        super();
        
        // create the buttons
        Buttons = new JButton[] {new JButton("fuck"), new JButton("fuck"), new JButton("fuck"), new JButton("fuck")};
        for (int i = 0; i < 4; i++)
        {
            final int j = i;
            Buttons[i].addActionListener(e ->{
                if (true) // TODO: Check if it is currently the player's turn
                {
                    engine.inputTranslator(j);
                }
            });
            lookAndFeel(Buttons[i]);
            this.add(Buttons[i]);
            this.setBackground(new Color(69, 48, 8));
        }
    }
    
    /**
     * Changes the appearance of a JButton
     * @param btn The JButton to act on
     */
    private void lookAndFeel(JButton btn)
    {
        btn.setBackground(new Color(102, 69 ,19));
    }
    
    /**
     * Override the paintComponent method for custom rendering.
     *
     * @param g The graphics object.
     */
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        
        int W = getWidth();
        int H = getHeight();
        g.setColor(new Color(69, 48, 108));
        g.fillRect(0, 0, W, H);
        
        // evenly space/size the buttons, rounding to the right
        for (int i = 0; i < 4; i++)
        {
            Buttons[i].setBounds((int) Math.ceil((W*i) / 4), 0, (int) Math.ceil(((float) W) / 4.0), H);
        }
    }
}
