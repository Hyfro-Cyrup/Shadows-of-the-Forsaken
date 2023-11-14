/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.gui;

import game.model.DungeonTile;
import game.model.GameState;
import game.model.Player;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Displays necessary components for both combat and non-combat encounters
 * 
 */
public class EncounterScreen extends JPanel {
    private final Hotbar hotbar;
    private final Player player;
    private final GameState gameState;
    private final JTextArea log;
    private final EncounterGraphic graphic;
    
    public EncounterScreen(SceneSwitcher switcher, DungeonTile tile)
    {
        // initialize the JPanel
        super();
        //this.setLayout(null);
        
        // get GameState data
        gameState = GameState.getInstance();
        player = gameState.getPlayer();
        
        // make the text area
        log = new JTextArea("You encountered a...");
        log.setEditable(false);
        log.setForeground(Color.WHITE);
        log.setBackground(new Color(69, 48, 8));
        this.add(log);
        //log.setPreferredSize(new Dimension(300, 0));
        
        // make the hotbar
        hotbar = new Hotbar(tile);
        this.add(hotbar);
        
        // make the graphic
        graphic = new EncounterGraphic(tile);
        this.add(graphic);
        
        repaint();
        
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
        
        // resize the components (easier than layout mangers)
        int log_w = Math.min((int)((W * 5) / 16), 300);  // default = 250 @ W=800 (transition @ W=960)
        int hot_h = Math.min((int)(H / 5), 80);          // default = 80 @ H=600 (transition @ 400)
        hotbar.setBounds(0, H - hot_h, W - log_w, hot_h);
        log.setBounds(W - log_w, 0, log_w, H);
        graphic.setBounds(0, 0, W - log_w, H - hot_h);
    }
}
