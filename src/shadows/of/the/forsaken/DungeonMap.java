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
 */
public class DungeonMap extends JPanel {
    private Player player;
    private int GRIDSIZE = 30;
    private final int MAX_GRIDSIZE = 60;
    private final int MARGINS = 50;
    private final SceneSwitcher switcher;
    private final DungeonTile[][] Tiles;
    private int[] origin;

    
    /**
     * Override the paintComponent method for custom rendering.
     *
     * @param g The graphics object.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        resizeMap();
        
        for (int i = 0; i < Tiles.length; i++)
        {
            for (int j = 0; j < Tiles[i].length; j++)
            {
                if (Tiles[i][j] != null && Tiles[i][j].hasBeenSeen())
                {
                    if (Tiles[i][j].containsKey())
                    {
                        g2d.setColor(Color.YELLOW);
                    }
                    else if (Tiles[i][j].containsLadder())
                    {
                        g2d.setColor(Color.ORANGE);
                    }
                    else if (Tiles[i][j].containsEnemy())
                    {
                        g2d.setColor(Color.RED);
                    }
                    else
                    {
                        g2d.setColor(Color.GRAY);
                    }
                    g2d.fillRect(GRIDSIZE*i + origin[0], GRIDSIZE*j + origin[1], GRIDSIZE, GRIDSIZE);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(GRIDSIZE*i + origin[0], GRIDSIZE*j + origin[1], GRIDSIZE, GRIDSIZE);
                }
            }
        }
        drawPlayer(g2d);
    }
    
    private void drawPlayer(Graphics2D g) {
        g.setColor(Color.BLUE);
        int x = (int) (GRIDSIZE*(player.x + 0.5) + origin[0]);
        int y = (int) (GRIDSIZE*(player.y + 0.5) + origin[1]);
        g.fillOval(x - GRIDSIZE / 2, y - GRIDSIZE / 2, GRIDSIZE, GRIDSIZE);
    }
    
    private void resizeMap()
    {
        // panel dimensions
        int X_MAX = getWidth();
        int Y_MAX = getHeight();
        
        // step 1 is to find the bounding box of all displayed tiles
        int w = Tiles.length;
        int h = Tiles[0].length;
        int xmin = 999;
        int xmax = -999;
        int ymin = 999;
        int ymax = -999;
        for (int i = 0; i < w; i++)
        {
            for (int j = 0; j < h; j++)
            {
                if (Tiles[i][j] != null && Tiles[i][j].hasBeenSeen())
                {
                    xmin = Math.min(xmin, i);
                    xmax = Math.max(xmax, i);
                    ymin = Math.min(ymin, j);
                    ymax = Math.max(ymax, j);
                }
            }
        }
        int bbox_w = xmax - xmin + 1;
        int bbox_h = ymax - ymin + 1;
        
        // step 2 is to scale the gridsize to fill up the frame (or clamp to a max)
        int xw = (X_MAX - 2*MARGINS);
        int yh = Y_MAX - 2*MARGINS;
        int g = (int) Math.floor(Math.min(xw/bbox_w, (yh)/bbox_h));
        GRIDSIZE = Math.min(MAX_GRIDSIZE, g);
        
        // step 3 is to find the coordinates of the (0, 0) tile so we can draw all others relative to it.
        int[] o = {(X_MAX - GRIDSIZE*(bbox_w + 2*xmin - 1)) / 2, (Y_MAX - (bbox_h + 2*ymin - 1)*GRIDSIZE) / 2};
        origin = o;
    }
    
    private void markAdjacentSeen()
    {
        int[][] neighbors = {
            {player.x + 1, player.y - 1},
            {player.x + 1, player.y},
            {player.x + 1, player.y + 1},
            {player.x, player.y - 1},
            {player.x, player.y + 1},
            {player.x - 1, player.y - 1},
            {player.x - 1, player.y},
            {player.x - 1, player.y + 1},
        };
        for (int[] n : neighbors)
        {
            if (-1 < n[0] && n[0] < Tiles.length &&
                -1 < n[1] && n[1] < Tiles[0].length &&
                Tiles[n[0]][n[1]] != null)
            {
                Tiles[n[0]][n[1]].markSeen();
            }
        }
    }
    
    /**
     * Initialize visualization from GameState data
     * @param parent The component that facilitates switching screens
     */
    public DungeonMap(SceneSwitcher parent)
    {
        super(new BorderLayout());
        switcher = parent;
        GameState gameState = GameState.getInstance();
        Tiles = gameState.getMap();
        Tiles[Tiles.length / 2][Tiles[0].length / 2].markSeen();
        
        setFocusable(true);
        
        player = gameState.getPlayer();
        player.x = Tiles.length / 2;
        player.y = Tiles[0].length / 2;
        markAdjacentSeen();
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> player.move(0, -1, Tiles);
                    case KeyEvent.VK_DOWN -> player.move(0, 1, Tiles);
                    case KeyEvent.VK_LEFT -> player.move(-1, 0, Tiles);
                    case KeyEvent.VK_RIGHT -> player.move(1, 0, Tiles);
                    case KeyEvent.VK_SPACE -> System.out.println(MapMaker.test(Tiles));
                }
                markAdjacentSeen();
                repaint(); // Ask the panel to redraw itself
            }
        });
        this.setBackground(Color.BLACK);

    }
}
