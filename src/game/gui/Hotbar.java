/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.gui;

import game.model.DungeonTile;
import game.model.EncounterEngine;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The bar containing buttons at the bottom of the EncounterScreen
 */
public class Hotbar extends JPanel {
    private JButton Buttons[];
    private ImageIcon[][] buttonIcons;
    private EncounterEngine engine;
    private DungeonTile tile;
    
    /**
     * Initialize the Hotbar with an array of buttons. Each button's event listener calls a method of the EncounterEngine 
     * @param tile The DungeonTile this encounter was built from
     */
    public Hotbar(DungeonTile tile)
    {
        // initialize the JPanel
        super();
        
        this.tile = tile;
        this.engine = tile.getEngine();
        
        // create the buttons
        Buttons = new JButton[5];
        for (int i = 0; i < 5; i++)
        {
            final int j = i;
            Buttons[i] = new JButton();
            Buttons[i].addActionListener(e ->{
                if (true) // TODO: Check if it is currently the player's turn
                {
                    engine.inputTranslator(engine.getSelectionLayer() == 3 && j < 3 ? 2-j : j);
                    repaint();
                }
            });
            lookAndFeel(Buttons[i]);
            this.add(Buttons[i]);
            this.setBackground(new Color(69, 48, 8));
        }
        try {
            makeAllIcons();
        } catch (IOException ex) {
            Logger.getLogger(Hotbar.class.getName()).log(Level.SEVERE, null, ex);
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
    
    private void makeAllIcons() throws IOException
    {
        // initialize the whole buttons array
        // structure: Buttons[i].setIcon(buttonIcons[engine.getSelectionLayer()][i]);
        
        buttonIcons = new ImageIcon[5][5];
        
        // selection layer = 0
        buttonIcons[0][0] = new ImageIcon(ImageIO.read(this.getClass().getResource("/resources/AttackIcon.png")));
        buttonIcons[0][1] = new ImageIcon(ImageIO.read(this.getClass().getResource("/resources/Block.png")));
        buttonIcons[0][2] = null;
        buttonIcons[0][3] = null;
        buttonIcons[0][4] = null;
        
        // selection layer = 1
        buttonIcons[1][0] = new ImageIcon(ImageIO.read(this.getClass().getResource("/resources/AttackIcon.png")));
        buttonIcons[1][1] = new ImageIcon(ImageIO.read(this.getClass().getResource("/resources/AttackIcon.png")));
        buttonIcons[1][2] = new ImageIcon(ImageIO.read(this.getClass().getResource("/resources/AttackIcon.png")));
        buttonIcons[1][3] = new ImageIcon(ImageIO.read(this.getClass().getResource("/resources/AttackIcon.png")));
        buttonIcons[1][4] = new ImageIcon(ImageIO.read(this.getClass().getResource("/resources/BackArrow.png")));
        
        // selection layer = 2
        buttonIcons[2][0] = new ImageIcon(ImageIO.read(this.getClass().getResource("/resources/AttackIcon.png")));
        buttonIcons[2][1] = new ImageIcon(ImageIO.read(this.getClass().getResource("/resources/AttackIcon.png")));
        buttonIcons[2][2] = new ImageIcon(ImageIO.read(this.getClass().getResource("/resources/AttackIcon.png")));
        buttonIcons[2][3] = new ImageIcon(ImageIO.read(this.getClass().getResource("/resources/AttackIcon.png")));
        buttonIcons[2][4] = new ImageIcon(ImageIO.read(this.getClass().getResource("/resources/BackArrow.png")));
        
        // selection layer = 3
        buttonIcons[3][0] = null;
        buttonIcons[3][1] = null;
        buttonIcons[3][2] = null;
        buttonIcons[3][3] = null;
        buttonIcons[3][4] = new ImageIcon(ImageIO.read(this.getClass().getResource("/resources/BackArrow.png")));
        int i = 2;
        for (var e : tile.getContents())
        {
            // TODO: resize
            buttonIcons[3][i] = new ImageIcon(ImageIO.read(this.getClass().getResource(e.getSpriteReference())));
            i--;
        }
        
        // selection layer = 4
        buttonIcons[4][0] = new ImageIcon(ImageIO.read(this.getClass().getResource("/resources/Investigate.png")));
        buttonIcons[4][1] = new ImageIcon(ImageIO.read(this.getClass().getResource("/resources/Touch.png")));
        buttonIcons[4][2] = null;
        buttonIcons[4][3] = null;
        buttonIcons[4][4] = new ImageIcon(ImageIO.read(this.getClass().getResource("/resources/BackArrow.png")));
        
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
        //g.setColor(new Color(69, 48, 108));
        //g.fillRect(0, 0, W, H);
        
        // evenly space/size the buttons, rounding to the right
        for (int i = 0; i < 5; i++)
        {
            Buttons[i].setBounds((int) Math.ceil((W*i) / 5.0), 0, (int) Math.ceil(((float) W) / 5.0), H);
            Buttons[i].setIcon(buttonIcons[engine.getSelectionLayer()][i]);
            if (buttonIcons[engine.getSelectionLayer()][i] == null)
            {
                //Buttons[i].setBackground(Color.RED);
                
            }
        }
    }
}
