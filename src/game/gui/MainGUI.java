/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package game.gui;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * Main entry point to the game. Holds and switches between all views. 
 */
public class MainGUI implements SceneSwitcher{
    private final JFrame frame;
    private JPanel panel;
    private final Map<String, JPanel> Scenes;
    
    private static MainGUI instance;
    
    /**
     * Get singleton gui / SceneSwitcher
     * @return MainGUI instance
     */
    public static MainGUI getInstance()
    {
        if (instance == null)
        {
            instance = new MainGUI();
        }
        return instance;
    }
    
    /**
     * Initializes the main game window.
     */
    private MainGUI() {
        // Initialize values for each scene
        Scenes = new HashMap();
        Scenes.put("START_SCREEN", new StartScreen(this));
        Scenes.put("LOAD_SCREEN", new LoadScreen(this));
        Scenes.put("DUNGEON_MAP", new DungeonMap(this));
        
        // Create the frame (window) to house the main menu.
        frame = new JFrame("Shadows of the Forsaken");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Go to the first scene
        panel = Scenes.get("START_SCREEN"); 
        
        frame.add(panel);
        
        frame.setVisible(true);
    }
    
    /**
     * Change out MainGUI's main panel for the requested scene.
     *
     * @param sceneName The name of the scene to switch to, defined by `Scenes` Map of MainGUI
     */
    @Override
    public void changeScene(String sceneName) {
        
        // Remove all components from the main frame (i.e., main menu)
        frame.getContentPane().removeAll();

        // Add the panel for the relevant scene
        panel = Scenes.get(sceneName);  //TODO: try catch for sceneName in Scenes
        frame.add(panel);
        panel.requestFocusInWindow();

        // Refresh the frame to reflect the changes
        frame.revalidate();
        frame.repaint();
    }
    
    /**
     * Change out MainGUI's main panel for the requested scene.
     * This version takes in a JPanel, allowing for dynamic scene switching without 
     * storing the new JPanel in MainGUI.Scenes.
     * Useful for EncounterScene objects.
     *
     * @param newScene The JPanel to display. 
     */
    @Override
    public void changeScene(JPanel newScene) {
        
        // Remove all components from the main frame (i.e., main menu)
        frame.getContentPane().removeAll();

        // Add the panel for the relevant scene
        panel = newScene; 
        frame.add(panel);
        panel.requestFocusInWindow();

        // Refresh the frame to reflect the changes
        frame.revalidate();
        frame.repaint();
    }

    
    
}
