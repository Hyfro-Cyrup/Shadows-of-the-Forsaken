/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * [[insert description of start screen component]]
 * @author Nathan Ainslie
 */
public class StartScreen extends JPanel {
    private SceneSwitcher switcher;
    private final JButton startButton;
    
    
    public StartScreen(SceneSwitcher parent)
    {
        switcher = parent;
        // Create the components
        startButton = new JButton("Start Game");
        
        // Add component functionality
        startButton.addActionListener((ActionEvent e) -> switcher.changeScene("DUNGEON_MAP"));
        
        // Add components to the frame (note the order)
        this.add(startButton);
    }
}
