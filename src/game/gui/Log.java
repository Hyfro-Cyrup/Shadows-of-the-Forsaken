/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.gui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
        log = new JTextArea(s);
        this.log.setEditable(false);
        this.log.setForeground(Color.WHITE);
        this.log.setLineWrap(true);
        this.log.setWrapStyleWord(true);
        this.log.setAutoscrolls(true);
        Color bg = new Color(69, 48, 8);
        this.log.setBackground(bg);
        
        setViewportView(log);
        this.setBackground(bg);
    }
    
    /**
     * Add text to the bottom of the log, and autoscroll to the bottom of the log
     * @param s The String to add
     */
    public void append(String s)
    {
        this.log.append(s);
        // Scroll to the bottom
        int bottomPosition = log.getDocument().getLength();
        log.setCaretPosition(bottomPosition);
        JScrollBar vertical = this.getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() );
        
    }
    
    
    // Scrollbar was getting positioned wrong at weird times, possibly during repainting
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        JScrollBar vertical = this.getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() );
    }
}
