/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * GUI for selecting and loading previously saved games
 */
public class LoadScreen extends JPanel {
    private SceneSwitcher switcher;
    private final JButton backButton;
    private ImageIcon backgroundImage;
    
    /**
     * Initialize the images and buttons for the load screen
     * 
     * @param parent The component that facilitates switching screens
     */
    public LoadScreen(SceneSwitcher parent)
    {
        switcher = parent;
        
        // Load the background image from the internet and resize it (code for background image is copied from StartScreen.java
        try {
            URL imageUrl = new URL("https://cdna.artstation.com/p/assets/images/images/006/315/366/large/taryn-meixner-dungeon-interior.jpg?1497628693");
            BufferedImage originalImage = ImageIO.read(imageUrl);

            int newWidth = 800;  // Set the width of the panel
            int newHeight = 600; // Set the height of the panel

            // Resize the image to fit within the panel dimensions
            Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            backgroundImage = new ImageIcon(scaledImage); //New image resized to fit the 800 x 600 window
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception, e.g., show an error message, basically if improper url will throw error
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
        backgroundImage.paintIcon(this, g, 0, 0);
        
        int buttonWidth = 150;
        int buttonHeight = 50;
        int panelWidth = getWidth(); // Get the panel's width
        int panelHeight = getHeight(); // Get the panel's height
        int buttonX = (panelWidth - buttonWidth) / 2;
        int buttonY = (panelHeight - (-2)*buttonHeight) / 2;

        backButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
    }
}
