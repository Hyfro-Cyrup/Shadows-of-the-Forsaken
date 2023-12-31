/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

import game.model.Player;
import game.model.DungeonTile;
import game.model.GameState;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

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
    private DungeonTile[][] Tiles;
    private int[] origin;
    private ScreenPauser screenPauser;
    private GameState gameState;
    
    private BufferedImage tile, combat, key, ladder, playerIcon;
    private Boolean playerFacingLeft = false; // used to make playerIcon look where they're going

    /**
     * Initialize visualization from GameState data
     * @param parent The component that facilitates switching screens
     */
    public DungeonMap(SceneSwitcher parent)
    {
        super();
        this.setLayout(null);
        
        switcher = parent;
        gameState = GameState.getInstance();
        Tiles = gameState.getMap();
        screenPauser = new ScreenPauser(this);
        
        player = gameState.getPlayer();
        player.x = Tiles.length / 2;
        player.y = Tiles[0].length / 2;
        markAdjacentSeen();
        
        // make all the icons
        try {
            makeAllIcons();
        } catch (IOException ex) {
            Logger.getLogger(DungeonMap.class.getName()).log(Level.SEVERE, null, ex);
        }

        setFocusable(true);
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    screenPauser.pause();
                    return;
                }
                if (!screenPauser.isPaused())
                {
                   switch (e.getKeyCode()) 
                   {
                        case KeyEvent.VK_UP, KeyEvent.VK_W -> player.move(0, -1, Tiles);
                        case KeyEvent.VK_DOWN, KeyEvent.VK_S -> player.move(0, 1, Tiles);
                        case KeyEvent.VK_LEFT, KeyEvent.VK_A -> {
                            player.move(-1, 0, Tiles);
                            playerFacingLeft = true;
                        }
                        case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> {
                            player.move(1, 0, Tiles);
                            playerFacingLeft = false;
                        }
                    } 
                }
                
                
                repaint();
                SwingUtilities.invokeLater(() -> { // ensure the scene is repainted first
                    if (Tiles[player.x][player.y].inEncounter()) {
                        try {
                            Thread.sleep(500); // add a small delay (Make animation later if time)
                        } catch (InterruptedException ex) {
                            Logger.getLogger(DungeonMap.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        // Move to the EncounterScreen
                        switcher.changeScene(Tiles[player.x][player.y].getGUI());
                    }
                });
            }
        });
        this.setBackground(Color.BLACK);

    }
    
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
                BufferedImage img = null;
                if (Tiles[i][j] != null && Tiles[i][j].hasBeenSeen())
                {
                    if (Tiles[i][j].containsKey())
                    {
                        img = key;
                    }
                    else if (Tiles[i][j].containsLadder())
                    {
                        img = ladder;
                    }
                    else if (Tiles[i][j].containsEnemy())
                    {
                        img = combat;
                    }
                    
                    g2d.drawImage(tile, GRIDSIZE*i + origin[0], GRIDSIZE*j + origin[1], GRIDSIZE, GRIDSIZE, this);
                    if (img != null)
                    {
                        g2d.drawImage(img, GRIDSIZE*i + origin[0], GRIDSIZE*j + origin[1], GRIDSIZE, GRIDSIZE, this);
                    }
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(GRIDSIZE*i + origin[0], GRIDSIZE*j + origin[1], GRIDSIZE, GRIDSIZE);
                    
                }
            }
        }
        drawPlayer(g2d);
        screenPauser.draw(g2d);
        
        
    }
    
    private void drawPlayer(Graphics2D g) 
    {
        int x = (int) (GRIDSIZE*(player.x) + origin[0]);
        int y = (int) (GRIDSIZE*(player.y) + origin[1]);
        
        if (playerFacingLeft)
        {
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-playerIcon.getWidth(null), 0);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            g.drawImage(op.filter(playerIcon, null), x, y, GRIDSIZE, GRIDSIZE, this);
        }
        else
        {
            g.drawImage(playerIcon, x, y, GRIDSIZE, GRIDSIZE, this);
        }
        
    }
    
    private void resizeMap()
    {
        // Now is as good a time as any to refresh these in case of loading desynchronization
        gameState = GameState.getInstance();
        player = gameState.getPlayer();
        Tiles = gameState.getMap();
        markAdjacentSeen();
        
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
        int[] o = {(X_MAX - GRIDSIZE*(bbox_w + 2*xmin)) / 2, (Y_MAX - (bbox_h + 2*ymin)*GRIDSIZE) / 2};
        origin = o;
    }
    
    private void markAdjacentSeen()
    {
        int[][] neighbors = {
            {player.x + 1, player.y - 1},
            {player.x + 1, player.y},
            {player.x + 1, player.y + 1},
            {player.x, player.y - 1},
            {player.x, player.y},
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
    
    private void makeAllIcons() throws IOException
    {
        playerIcon = ImageIO.read(this.getClass().getResource("/resources/MapPlayer.png"));
        tile = ImageIO.read(this.getClass().getResource("/resources/MapTile.png"));
        key = ImageIO.read(this.getClass().getResource("/resources/Chest.png"));
        combat = ImageIO.read(this.getClass().getResource("/resources/AttackMapIcon.png"));
        ladder = ImageIO.read(this.getClass().getResource("/resources/LadderMapIcon.png"));
        
    }
    
}
