/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * GUI for selecting and loading previously saved games
 */
public class LoadScreen extends JPanel {
    private SceneSwitcher switcher;
    private final JButton backButton;
    private BufferedImage backgroundImage;
    
    /**
     * Initialize the images and buttons for the load screen
     * 
     * @param parent The component that facilitates switching screens
     */
    public LoadScreen(SceneSwitcher parent)
    {
        switcher = parent;
        
       // Loads the background image (figuring out how to import and resize the image done by chat gpt)
            try
            {
                backgroundImage = ImageIO.read(this.getClass().getResource("/resources/StartMenuBackground.jpg"));
            } catch (IOException ex) {
                Logger.getLogger(StartScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        // Create the components
        backButton = new JButton("Back to Start");
        
        // Add component functionality
        backButton.addActionListener((ActionEvent e) -> switcher.changeScene("START_SCREEN"));
        
        // Add components to the frame (note the order)
        this.add(backButton);
    }
    
    /**
     * Override to allow image to be placed in background and to change positioning of the button(s)
     *
     * @param g The graphics object.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int panelWidth = getWidth(); // Get the panel's width
        int panelHeight = getHeight(); // Get the panel's height
        
        int bgWidth = backgroundImage.getWidth();
        int bgHeight = backgroundImage.getHeight();
        
        // scale and crop the background image -- there should be no whitespace around image regardless of window size
        double scale = Math.max(((double)panelWidth )/ bgWidth, ((double) panelHeight )/ bgHeight);
        int newWidth = (int) (scale * bgWidth);
        int newHeight = (int) (scale * bgHeight);
        g.drawImage(backgroundImage, (panelWidth - newWidth) / 2, (panelHeight - newHeight) / 2, newWidth, newHeight, this);
        
        
        int buttonWidth = 150;
        int buttonHeight = 50;
        int buttonX = (panelWidth - buttonWidth) / 2;
        int buttonY = (panelHeight - (-2)*buttonHeight) / 2;

        backButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
    }
}
