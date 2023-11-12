/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package game.gui;

import javax.swing.JPanel;

/**
 * Hides everything about the main program class except its ability to switch scenes.
 * This makes for better encapsulation when it's passed to the scenes
 */
public interface SceneSwitcher {
    /**
     * Switches from the current scene to the one specified by `sceneName`.
     * @param sceneName Unique String associated with destination
     */
    public void changeScene(String sceneName);
    
    /**
     * Change out MainGUI's main panel for the requested scene.
     * This version takes in a JPanel, allowing for dynamic scene switching without 
     * storing the new JPanel in MainGUI.Scenes.
     * Useful for EncounterScene objects.
     * 
     * @param newScene The JPanel to display. 
     */
    public void changeScene(JPanel newScene);
}
