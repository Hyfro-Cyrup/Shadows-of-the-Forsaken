/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.model.Player;
import game.model.DungeonTile;
import game.model.GameState;

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
    private boolean paused = false;
    private JButton pauseButton;
    private JPanel centeredPanel;
    private JPanel rightPanel;
    private GameState gameState;

    
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
        if (paused)
        {
            g2d.setColor(new Color(50, 50, 50, 150));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        var size = centeredPanel.getPreferredSize();
        int swidth = getWidth();
        int sheight = getHeight();
        centeredPanel.setBounds((swidth - size.width) / 2, (sheight - size.height) / 2, size.width, size.height);
        size = rightPanel.getPreferredSize();
        rightPanel.setBounds((7*swidth / 10) + ((3*swidth / 10) - 2*size.width) / 2, (sheight - size.height) / 2, 3 * swidth/ 10, size.height);
        centeredPanel.revalidate();
        centeredPanel.repaint();
        rightPanel.revalidate();
        rightPanel.repaint();
        
        
    }
    
    private void drawPlayer(Graphics2D g) 
    {
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
        super();
        switcher = parent;
        gameState = GameState.getInstance();
        Tiles = gameState.getMap();
        Tiles[Tiles.length / 2][Tiles[0].length / 2].markSeen(); 
        
        player = gameState.getPlayer();
        player.x = Tiles.length / 2;
        player.y = Tiles[0].length / 2;
        markAdjacentSeen();
        
        // make all the buttons
        this.setLayout(null);
        makePauseButton();
        makePauseScreen();

        setFocusable(true);
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    pause();
                    return;
                }
                if (!paused)
                {
                   switch (e.getKeyCode()) 
                   {
                        case KeyEvent.VK_UP -> player.move(0, -1, Tiles);
                        case KeyEvent.VK_DOWN -> player.move(0, 1, Tiles);
                        case KeyEvent.VK_LEFT -> player.move(-1, 0, Tiles);
                        case KeyEvent.VK_RIGHT -> player.move(1, 0, Tiles);
                    } 
                }
                
                markAdjacentSeen();
                repaint(); // Ask the panel to redraw itself
            }
        });
        this.setBackground(Color.BLACK);

    }
    
    private void pause()
    {
        paused = !paused;
        centeredPanel.setVisible(paused);
        rightPanel.setVisible(paused);
        repaint();
    }
    
    private void makePauseButton()
    {
        
        pauseButton = new JButton("");
        
        pauseButton.setIcon(new ImageIcon(new ImageIcon(DungeonMap.class.getResource("/resources/pause_icon.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));      
        pauseButton.setBorderPainted(false);
        pauseButton.setContentAreaFilled(false); 
       
        pauseButton.setBounds(20, 20, 60, 60);
        
        pauseButton.addActionListener((ActionEvent e)  -> pause());
        
        pauseButton.setFocusable(false);
        
        this.add(pauseButton);
    }
    
    private void makePauseScreen()
    {
        
        centeredPanel = new JPanel(new GridLayout(0, 1));
        
        rightPanel = new JPanel(new GridLayout(0, 1));
        
        
        JButton goToStart = new JButton("Main Menu");
        JButton backToGame = new JButton("Back to Game");
        
        goToStart.addActionListener((ActionEvent e) -> {
            switcher.changeScene("START_SCREEN");
            pause();
        });
        backToGame.addActionListener((ActionEvent e) -> pause());
        
        for (JButton btn : new JButton[] {goToStart, backToGame})
        {
            JPanel bp = new JPanel();
            bp.add(btn);
            bp.setOpaque(false);
            centeredPanel.add(bp);
        }
        
        JButton slot1 = new JButton("Slot 1");
        JButton slot2 = new JButton("Slot 2");
        JButton slot3 = new JButton("Slot 3");
        
        
        JLabel label = new JLabel("Save Game");
        label.setForeground(Color.WHITE);
        rightPanel.add(label);
        
        int i = 1;
        for (JButton btn : new JButton[]{ slot1, slot2, slot3})
        {
            final int j = i;
            JPanel bp = new JPanel();
            bp.add(btn);
            bp.setOpaque(false);
            rightPanel.add(bp);
            btn.addActionListener((ActionEvent e) -> GameState.saveGame("save" + String.valueOf(j)));
            i++;
        }
        

        
        centeredPanel.setOpaque(false);
        rightPanel.setOpaque(false);
        
        centeredPanel.add(Box.createGlue());
        rightPanel.add(Box.createGlue());
        
        centeredPanel.setVisible(false);
        rightPanel.setVisible(false);
        
        this.add(centeredPanel);
        this.add(rightPanel);
        
    }
    
}
