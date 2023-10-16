/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

/**
 * The main component that draws the dungeon map. There is a temporary slider here
 * so that I can play with different map generating algorithms. 
 * @author Nathan Ainslie
 */
public class DungeonMap extends JPanel {
    Player player;
    private final int RADIUS = 15; // Radius of the circle/sprite
    private final int GRIDSIZE = 30;
    private final SceneSwitcher switcher;
    private DungeonTile[][] Tiles;
    private DungeonTile[][][] multiMap;
    private JSlider iterationSlider;

    
    /**
     * Override the paintComponent method for custom rendering.
     *
     * @param g The graphics object.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw a simple grid of rectangles
        for (int i = 0; i < Tiles.length; i++)
        {
            for (int j = 0; j < Tiles[i].length; j++)
            {
                if (Tiles[i][j] != null)
                {
                    g2d.setColor(Color.RED);
                    g2d.fillRect(GRIDSIZE*i, GRIDSIZE*j, GRIDSIZE, GRIDSIZE);
                    g2d.setColor(Color.BLACK);
                }
                g2d.drawRect(GRIDSIZE*i, GRIDSIZE*j, GRIDSIZE, GRIDSIZE);
            }
        }
        //TODO: Draw a rectangle for each element of Tiles. Later, use an image.
        drawPlayer(g2d);
    }
    
    private void drawPlayer(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.fillOval(player.x - RADIUS, player.y - RADIUS, 2 * RADIUS, 2 * RADIUS);
    }
    
    public DungeonMap(SceneSwitcher parent)
    {
        super(new BorderLayout());
        switcher = parent;
        GameState gameState = GameState.getInstance();
        Tiles = gameState.getMap();
        multiMap = gameState.getMultiMap();
        
        iterationSlider = new JSlider(JSlider.VERTICAL, 0, multiMap.length - 1, 0);
        iterationSlider.setMajorTickSpacing(1);
        iterationSlider.setSnapToTicks(true);
        iterationSlider.addChangeListener((ChangeEvent e) -> {
            Tiles = multiMap[(int) Math.floor(iterationSlider.getValue())];
            repaint();
        });
        this.add(iterationSlider, BorderLayout.EAST);
        setFocusable(true);
        
        player = gameState.getPlayer();
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> player.move(0, -RADIUS);
                    case KeyEvent.VK_DOWN -> player.move(0, RADIUS);
                    case KeyEvent.VK_LEFT -> player.move(-RADIUS, 0);
                    case KeyEvent.VK_RIGHT -> player.move(RADIUS, 0);
                }
                repaint(); // Ask the panel to redraw itself
            }
        });
    }
}
