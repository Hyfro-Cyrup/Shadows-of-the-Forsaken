/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.gui;

import game.model.GameState;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.function.Supplier;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Service that facilitates the pausing of a screen (JPanel) with a button. 
 * For best results, call ScreenPauser.draw(g) in your paintComponent method.
 */
public class ScreenPauser {
    private boolean paused = false;
    private JButton pauseButton;
    private JPanel centeredPanel;
    private JPanel rightPanel;
    private final JPanel parent;
    private final SceneSwitcher switcher;
    
    /**
     * Makes a screen pauser with custom button in the upper left corner
     * @param parent The JPanel that holds the ScreenPauser object
     */
    public ScreenPauser(JPanel parent)
    {
        this.parent = parent;
        this.switcher = MainGUI.getInstance();
        makePauseButton();
        makePauseScreen();
    }
    
    /**
     * Make a screen pauser and attaches it to a specified button
     * @param parent The JPanel that holds the ScreenPauser object
     * @param pauseButton The button to facilitate pausing
     * @param condition function void -> Boolean that returns true when you want the button to pause.
     */
    public ScreenPauser(JPanel parent, JButton pauseButton, Supplier<Boolean> condition)
    {
        this.parent = parent;
        this.pauseButton = pauseButton;
        this.switcher = MainGUI.getInstance();
        pauseButton.addActionListener(e -> {
            if (condition.get())
            {
                pause();
            }
        });
        makePauseScreen();
    }
    
    /**
     * Draw the gray overlay and buttons if the screen in paused
     * @param g2d Graphics2D object used for drawing
     */
    public void draw(Graphics2D g2d)
    {
        if (paused)
        {
            int W = parent.getWidth();
            int H = parent.getHeight();
            
            g2d.setColor(new Color(50, 50, 50, 150));
            g2d.fillRect(0, 0, W, H);
            
            var size = centeredPanel.getPreferredSize();
            centeredPanel.setBounds((W - size.width) / 2, (H - size.height) / 2, size.width, size.height);
            size = rightPanel.getPreferredSize();
            rightPanel.setBounds((7*W / 10) + ((3*W / 10) - 2*size.width) / 2, (H - size.height) / 2, 3 * W/ 10, size.height);
            centeredPanel.revalidate();
            centeredPanel.repaint();
            rightPanel.revalidate();
            rightPanel.repaint();
        }
        
    }
    
    /**
     * Manually toggle whether the screen is paused or unpaused
     */
    public void pause()
    {
        paused = !paused;
        centeredPanel.setVisible(paused);
        rightPanel.setVisible(paused);
        parent.repaint();
    }
    
    /**
     * Get the current state of the pauser: paused or unpaused
     * @return True if paused
     */
    public Boolean isPaused() { return paused; }
    
    private void makePauseButton()
    {
        pauseButton = new JButton("");
        
        pauseButton.setIcon(new ImageIcon(new ImageIcon(DungeonMap.class.getResource("/resources/pause_icon.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));      
        pauseButton.setBorderPainted(false);
        pauseButton.setContentAreaFilled(false); 
       
        pauseButton.setBounds(20, 20, 60, 60);
        
        pauseButton.addActionListener((ActionEvent e)  -> pause());
        
        pauseButton.setFocusable(false);
        
        parent.add(pauseButton);
    }
    
    private void makePauseScreen()
    {
        
        centeredPanel = new JPanel(new GridLayout(0, 1));
        
        rightPanel = new JPanel(new GridLayout(0, 1));
        
        JButton backToGame = new JButton("Back to Game");
        JButton goToStart = new JButton("Main Menu");
        
        goToStart.addActionListener((ActionEvent e) -> {
            switcher.changeScene("START_SCREEN");
            pause();
        });
        backToGame.addActionListener((ActionEvent e) -> pause());
        
        for (JButton btn : new JButton[] {backToGame, goToStart})
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
        
        parent.add(centeredPanel);
        parent.add(rightPanel);
        
        parent.setComponentZOrder(centeredPanel, 1);
        parent.setComponentZOrder(rightPanel, 1);
        
    }
}
