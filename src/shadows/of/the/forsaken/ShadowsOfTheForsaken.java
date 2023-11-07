/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package shadows.of.the.forsaken;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Main entry point to the game. Holds and switches between all views. 
 */
public class ShadowsOfTheForsaken implements SceneSwitcher{
    private final JFrame frame;
    private JPanel panel;
    private final Map<String, JPanel> Scenes;
    /**
     * Initializes the main game window.
     */
    public ShadowsOfTheForsaken() {
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
     * Change out SoTF's main panel for the requested scene.
     *
     * @param sceneName The name of the scene to switch to. Defined by `Scenes` Map of SoTF
     */
    @Override
    public void changeScene(String sceneName) {
        System.out.println("Starting the game!");
        
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
     * The main method of the game.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Initialize the game object
        ShadowsOfTheForsaken SoTF = new ShadowsOfTheForsaken();
    }
    
    
}
