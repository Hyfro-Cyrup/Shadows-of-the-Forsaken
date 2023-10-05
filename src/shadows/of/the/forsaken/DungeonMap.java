/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.util.Collections.emptyList;
import javax.swing.JPanel;

/**
 *
 * @author Nathan Ainslie
 */
public class DungeonMap extends JPanel {
    Player player;
    private final int RADIUS = 30; // Radius of the circle/sprite
    private final SceneSwitcher switcher;
    private DungeonTile[][] Tiles;

    
    /**
     * Override the paintComponent method for custom rendering.
     *
     * @param g The graphics object.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // For now, let's just draw a simple rectangle to represent the dungeon area.
        g2d.drawRect(50, 50, 300, 200);
        //TODO: Draw a rectangle for each element of Tiles. Later, use an image.
        drawPlayer(g2d);
    }
    
    private void drawPlayer(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.fillOval(player.x - RADIUS, player.y - RADIUS, 2 * RADIUS, 2 * RADIUS);
    }
    
    public DungeonMap(SceneSwitcher parent)
    {
        switcher = parent;
        Tiles = new DungeonTile[10][10];
        
        setFocusable(true);
        
        player = new Player("Dummy/file/path", 69, emptyList(), false, emptyList());
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> player.move(0, -5);
                    case KeyEvent.VK_DOWN -> player.move(0, 5);
                    case KeyEvent.VK_LEFT -> player.move(-5, 0);
                    case KeyEvent.VK_RIGHT -> player.move(5, 0);
                }
                repaint(); // Ask the panel to redraw itself
            }
        });
    }
}
