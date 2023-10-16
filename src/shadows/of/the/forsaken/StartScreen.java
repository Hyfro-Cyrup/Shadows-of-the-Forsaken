/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.net.URL; //allows the program to work with urls
import javax.imageio.ImageIO; //allows the program to read and write images
import java.awt.image.BufferedImage; //allows the program to use image processing

/**
 * Main menu for the game. Has buttons for navigating to the game or load screen.
 */
public class StartScreen extends JPanel {
    private SceneSwitcher switcher;
    private final JButton startButton;
    private final JButton loadButton;
    private ImageIcon backgroundImage;

    /**
     * Initialize the images and buttons for the start screen
     * 
     * @param parent The component that facilitates switching screens
     */
    public StartScreen(SceneSwitcher parent) {
        switcher = parent;

        // Loads the background image from the internet and resizes it (figuring out how to import and resize the image done by chat gpt)
        try {
            URL imageUrl = new URL("https://cdna.artstation.com/p/assets/images/images/006/315/366/large/taryn-meixner-dungeon-interior.jpg?1497628693");
            BufferedImage originalImage = ImageIO.read(imageUrl);
            int newWidth = 800;  // Set the width of the panel
            int newHeight = 600; // Set the height of the panel

            // Resize the image to fit within the panel dimensions
            Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            backgroundImage = new ImageIcon(scaledImage); //The new image resized to fit the 800 x 600 window
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception, e.g., show an error message, basically if improper url input will throw error
        }

        // Create the buttons
        startButton = new JButton("Start Game");
        loadButton = new JButton("Load Game");

        // Add component functionality
        startButton.addActionListener((ActionEvent e) -> switcher.changeScene("DUNGEON_MAP"));
        loadButton.addActionListener((ActionEvent e) -> switcher.changeScene("LOAD_SCREEN"));

        // Add the buttons to the panel
        add(startButton);
        add(loadButton);
    }

    /**
     * Use an override to allow the image to be in the background as well as changing button positions
     * 
     * @param g The graphics object.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        backgroundImage.paintIcon(this, g, 0, 0);
        
        // Calculate the start button's position to center it
        int buttonWidth = 150;
        int buttonHeight = 50;
        int panelWidth = getWidth(); // Get the panel's width
        int panelHeight = getHeight(); // Get the panel's height
        int buttonX = (panelWidth - buttonWidth) / 2; // Centers the button horizontally
        int buttonY = (panelHeight - buttonHeight) / 2; // Centers the button vertically

        startButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
        
        // Calculate the load button's position to place it slightly below the start button
        int lbuttonWidth = 150;
        int lbuttonHeight = 50;
        int lbuttonX = (panelWidth - lbuttonWidth) / 2; // Centers the button horizontally
        int lbuttonY = (panelHeight - (-2)*lbuttonHeight) / 2; // Slightly shifts the button down from vertical center
        
        loadButton.setBounds(lbuttonX, lbuttonY, lbuttonWidth, lbuttonHeight);
    }
}