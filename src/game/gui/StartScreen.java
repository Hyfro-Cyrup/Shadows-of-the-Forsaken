/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.gui;

import game.model.GameState;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.imageio.ImageIO; //allows the program to read and write images
import java.awt.image.BufferedImage; //allows the program to use image processing
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main menu for the game. Has buttons for navigating to the game or load screen.
 */
public class StartScreen extends JPanel {
    private SceneSwitcher switcher;
    private final JButton newButton;        // var for new game button
    private final JButton loadButton;       // var for load game button
    private final JButton exitButton;   // var for continue game button
    private BufferedImage backgroundImage;

    /**
     * Initialize the images and buttons for the start screen
     * 
     * @param parent The component that facilitates switching screens
     */
    public StartScreen(SceneSwitcher parent) {
        
            switcher = parent;
            
            // Loads the background image (figuring out how to import and resize the image done by chat gpt)
            try
            {
                backgroundImage = ImageIO.read(this.getClass().getResource("/resources/StartMenuBackground.jpg"));
            } catch (IOException ex) {
                Logger.getLogger(StartScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Create the buttons
            newButton = new JButton("New Game");
            loadButton = new JButton("Load Game");
            exitButton = new JButton("Exit");
            
            // Add component functionality
            newButton.addActionListener((ActionEvent e) -> GameState.newInstance().loadScreen());
            loadButton.addActionListener((ActionEvent e) -> switcher.changeScene("LOAD_SCREEN"));
            exitButton.addActionListener((ActionEvent e) -> System.exit(0));
            
            // Add the buttons to the panel
            add(newButton);
            add(loadButton);
            add(exitButton);
    }

    /**
     * Use an override to allow the image to be in the background as well as changing button positions
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
        
        // Title
        String title = "Shadows of the Forsaken";
        int messageLength = title.length();
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.setColor(Color.WHITE);
        g.drawString(title,(panelWidth / 2) - ((messageLength * 20)  / 2),(panelHeight) / 3);
        
        // Calculate the New Game button's position to center it
        int buttonWidth = 150;
        int buttonHeight = 50;
        
        int buttonX = (panelWidth - buttonWidth) / 2; // Centers the button horizontally
        int buttonY = (panelHeight - buttonHeight) / 2; // Centers the button vertically

        newButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
        
        // Calculate the Load Game button's position to place it slightly below the start button
        int lbuttonWidth = 150;
        int lbuttonHeight = 50;
        int lbuttonX = (panelWidth - lbuttonWidth) / 2; // Centers the button horizontally
        int lbuttonY = (panelHeight - (-2)*lbuttonHeight) / 2; // Slightly shifts the button down from vertical center
        
        loadButton.setBounds(lbuttonX, lbuttonY, lbuttonWidth, lbuttonHeight);
        
        // Exits program
        int ebuttonWidth = 75;
        int ebuttonHeight = 50;
        int ebuttonX = (panelWidth - ebuttonWidth) / 2; // Centers the button horizontally
        int ebuttonY = (panelHeight - (-5)*ebuttonHeight) / 2; // Slightly shifts the button down from vertical center
        
        exitButton.setBounds(ebuttonX, ebuttonY, ebuttonWidth, ebuttonHeight);
    }
}
