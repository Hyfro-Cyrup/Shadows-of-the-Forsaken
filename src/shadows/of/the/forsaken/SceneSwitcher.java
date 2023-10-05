/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package shadows.of.the.forsaken;

/**
 * Hides everything about the main program class except its ability to switch scenes.
 * This makes for better encapsulation when it's passed to the scenes
 * @author Nathan Ainslie
 */
public interface SceneSwitcher {
    public void changeScene(String sceneName);
}
