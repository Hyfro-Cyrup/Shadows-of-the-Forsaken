/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.gui;

import game.model.DungeonTile;
import game.model.EncounterEngine;
import game.model.Entity;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A place to house all the drawing logic during an encounter (not HUD)
 */
public class EncounterGraphic extends JPanel {
    private final EncounterEngine engine;
    private final List<Entity> entities;
    private final List<JLabel> images;
    private JLabel playerIcon = null;
    
    /**
     * Create a new graphic for a given encounter
     * @param tile The DungeonTile this encounter was created from
     */
    public EncounterGraphic(DungeonTile tile)
    {
        this.engine = tile.getEngine();
        this.entities = tile.getContents();
        this.images = Arrays.asList();
        try {
            for (Entity e : entities)
            {

                    //BufferedImage ePic = ImageIO.read(this.getClass().getResource(e.getSpriteReference()));
                    //images.add(new JLabel(new ImageIcon(ePic)));
            }          
            BufferedImage pPic = ImageIO.read(this.getClass().getResource("/resources/CombatPlayer.png"));
            playerIcon = new JLabel(new ImageIcon(pPic));
            
        } catch (IOException ex) {
            Logger.getLogger(EncounterGraphic.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // add everything to the panel
        this.add(playerIcon);
        for (JLabel l : images)
        {
            this.add(l);
        }
        
        this.setBackground(Color.GRAY);
    }
    
    /**
     * Override the paintComponent method for custom rendering.
     *
     * @param g The graphics object.
     */
    @Override
    protected void paintComponent(Graphics g) 
    {
        int W = getWidth();
        int H = getHeight();
        
        playerIcon.setBounds(0, H - playerIcon.getHeight(),(int) (playerIcon.getWidth() ), (int) (playerIcon.getHeight() ));
        
        super.paintComponent(g);
    }
}
