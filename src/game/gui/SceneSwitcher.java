/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package game.gui;

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
}
