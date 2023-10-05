/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * [[insert description of load screen]]
 * @author Nathan Ainslie
 */
public class LoadScreen extends JPanel {
    private SceneSwitcher switcher;
    private final JButton backButton;
    
    
    public LoadScreen(SceneSwitcher parent)
    {
        switcher = parent;
        
        // Create the components
        backButton = new JButton("Start Game");
        
        // Add component functionality
        backButton.addActionListener((ActionEvent e) -> switcher.changeScene("START_SCREEN"));
        
        // Add components to the frame (note the order)
        this.add(backButton);
    }
}
