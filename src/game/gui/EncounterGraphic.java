/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.gui;

import game.model.Creature;
import game.model.DungeonTile;
import game.model.EncounterEngine;
import game.model.Entity;
import game.model.GameState;
import game.model.Player;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * A place to house all the drawing logic during an encounter (not HUD)
 */
public class EncounterGraphic extends JPanel {
    private final EncounterEngine engine;
    private final List<Entity> entities;
    private final List<BufferedImage> images;
    private BufferedImage playerIcon = null;
    private final Player player = GameState.getInstance().getPlayer();
    
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
        int padding;
        
        // draw the player
        int size = Math.min(H/2, 300);
        g.drawImage(playerIcon, 0, H - size, size, size, this);
        
        // draw the player's stat bars
        int     margin_top = 50, 
                margin_left = 25,
                margin_right = 25;
        padding = 20;
        int barWidth = Math.min(W - size - margin_left - margin_right, 350);
        int barHeight = 12;
        Color barColors[] = new Color[]{
            Color.RED,      // HP color
            Color.GREEN,    // Stamina color 
            Color.BLUE      // Mana color
        };
        int barLengths[] = new int[]{
            (int) ((double) player.getCurrentHP() * (barWidth - 4) / player.getMaxHP()),
            (int) ((double) player.getCurrentStamina() * (barWidth - 4) / player.getMaxStamina()),
            (int) ((double) player.getCurrentMana() * (barWidth - 4) / player.getMaxMana())
        };
        String barLabels[] = new String[]{
            "HP " + player.getCurrentHP() + "/" + player.getMaxHP(),
            "STAMINA " + player.getCurrentStamina() + "/" + player.getMaxStamina(),
            "MANA " + player.getCurrentMana() + "/" + player.getMaxMana()
        };
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        for (int i = 0; i < 3; i++)
        {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(size + margin_left, H - size + margin_top + i*(padding + barHeight), barWidth, barHeight);
            g.setColor(barColors[i]);
            g.fillRect(size + margin_left + 2, H - size + margin_top + i*(padding + barHeight) + 2, barLengths[i], barHeight - 4);
            g.drawString(barLabels[i], size + margin_left, H - size + margin_top + i*(padding + barHeight) + 12 + barHeight);
        }
        
        // space entities properly
        padding = 10;
        size = Math.min(Math.min((W - 4*padding)/3, H/2 - 20), 250); 
        padding = (W - 3*size) / 4;
        for (int i = 0; i < images.size(); i++)
        {
            var img = images.get(i);
            var left = W - (i+1)*(padding + size);
            g.drawImage(img, left, 0, size, size, this);

            Entity e = entities.get(i);
            if (e instanceof Creature c)
            {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(left - 1, size + 10 - 1, size + 2, 10 + 2);
                
                // Health bar
                g.setColor(Color.RED);
                g.fillRect(left + 1, size + 10 + 1, (int) ((double) c.getCurrentHP() * (size - 2) / c.getMaxHP()), 10-2);
                //g.drawString("" + c.getCurrentHP(), left - 1, size + 10 - 1 + 12 + 11);
            }
        }
        
    }
}
