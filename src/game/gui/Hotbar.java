/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.gui;

import game.model.DungeonTile;
import game.model.EncounterEngine;
import game.model.GameState;
import game.model.Player;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
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
     * Initialize the Hotbar with an array of buttons.Each button's event listener calls a method of the EncounterEngine 
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
        var p = GameState.getInstance().getPlayer();
        for (int i = 0; i < 5; i++)
        {
            final int j = i;
            final var player = p;
            Buttons[i] = new JButton();
            Buttons[i].addActionListener(e ->{
                if (true) // TODO: Check if it is currently the player's turn
                {
                    synchronized (player)
                    {
                        engine.inputTranslator(engine.getSelectionLayer() == 3 && j < 3 ? 2-j : j);
                        player.notify();  
                    }
                    
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
        
        Player player = GameState.getInstance().getPlayer();
        
        buttonIcons = new ImageIcon[5][5];
        
        // selection layer = 0
        buttonIcons[0][0] = new ImageIcon(ImageIO.read(this.getClass().getResource("/resources/AttackIcon.png")));
        buttonIcons[0][1] = new ImageIcon(ImageIO.read(this.getClass().getResource("/resources/Block.png")));
        buttonIcons[0][2] = null;
        buttonIcons[0][3] = new ImageIcon(ImageIO.read(this.getClass().getResource("/resources/pause_icon.png")).getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        buttonIcons[0][4] = null;
        
        // selection layer = 1
        for (int i = 0; i < 4; i++)
        {
            buttonIcons[1][i] = new ImageIcon(ImageIO.read(this.getClass().getResource(player.getPhysAttack(i).getSpriteReference())));
        }
        buttonIcons[1][4] = new ImageIcon(ImageIO.read(this.getClass().getResource("/resources/BackArrow.png")));
        
        // selection layer = 2
        for (int i = 0; i < 4; i++)
        {
            buttonIcons[2][i] = new ImageIcon(ImageIO.read(this.getClass().getResource(player.getMagicAttack(i).getSpriteReference())));
        }
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
    
    /**
     * Get one of the hotbar's buttons
     * @param i Index of the button
     * @return the button
     */
    public JButton getButton(int i)
    {
        return Buttons[i];
    }
}
