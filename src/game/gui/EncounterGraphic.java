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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * A place to house all the drawing logic during an encounter (not HUD)
 */
public class EncounterGraphic extends JPanel {
    private final EncounterEngine engine;
    private final List<Entity> entities;
    private final List<BufferedImage> images;
    private BufferedImage playerIcon = null;
    
    /**
     * Create a new graphic for a given encounter
     * @param tile The DungeonTile this encounter was created from
     */
    public EncounterGraphic(DungeonTile tile)
    {
        this.engine = tile.getEngine();
        this.entities = tile.getContents();
        this.images = new ArrayList<>();
        
        // populate the images and playerIcon
        try {
            for (Entity e : entities)
            {
                    BufferedImage ePic = ImageIO.read(this.getClass().getResource(e.getSpriteReference()));
                    images.add(ePic);
            }          
            playerIcon = ImageIO.read(this.getClass().getResource("/resources/CombatPlayer.png"));
            
        } catch (IOException ex) {
            Logger.getLogger(EncounterGraphic.class.getName()).log(Level.SEVERE, null, ex);
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
        super.paintComponent(g);
        
        int W = getWidth();     // default = 542
        int H = getHeight();    // default = 485
        
        // draw the player
        int size = Math.min(H/2, 300);
        g.drawImage(playerIcon, 0, H - size, size, size, this);
        
        // space entities properly
        int padding = 10;
        size = Math.min(Math.min((W - 4*padding)/3, H/2 - 20), 250); 
        padding = (W - 3*size) / 4;
        for (int i = 0; i < images.size(); i++)
        {
            var img = images.get(i);
            g.drawImage(img, W - (i+1)*(padding + size), 0, size, size, this);
        }
        
    }
}
