/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.gui;

import java.awt.Color;
import java.awt.ScrollPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/**
 * Custom JTextArea to house log messages
 */
public class Log extends JScrollPane {
    private final JTextArea log;
    /**
     * Initialize all the settings of the Log
     * @param s Initialization text
     */
    public Log(String s)
    {
        super();
        log = new JTextArea();
        this.log.setEditable(false);
        this.log.setForeground(Color.WHITE);
        this.log.setLineWrap(true);
        this.log.setWrapStyleWord(true);
        this.log.setAutoscrolls(true);
        this.log.setBackground(new Color(69, 48, 8));
        
        setViewportView(log);
    }
    
    public void append(String s)
    {
        this.log.append(s);
        // Scroll to the bottom
        int bottomPosition = log.getDocument().getLength();
        log.setCaretPosition(bottomPosition);
        System.out.println("Caret position set: " + bottomPosition);
        
    }
}
