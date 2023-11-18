/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.gui;

import game.model.Attack;
import game.model.Creature;
import game.model.DamageCode;
import game.model.DungeonTile;
import game.model.EncounterEngine;
import game.model.Entity;
import game.model.GameState;
import game.model.Player;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 * Displays necessary components for both combat and non-combat encounters
 * 
 */
public class EncounterScreen extends JPanel {
    private final Hotbar hotbar;
    private final Player player;
    private final GameState gameState;
    private final Log log;
    private final EncounterGraphic graphic;
    private final SceneSwitcher switcher;
    private final EncounterEngine engine;
    private final DungeonTile tile;
    private final ScreenPauser screenPauser;
    private final JPanel test;
    
    /**
     * Create new EncounterScreen. Initializes Hotbar, log, and graphics area from the DungeonTile information
     * @param tile  The DungeonTile this encounter was built from
     */
    public EncounterScreen(DungeonTile tile)
    {
        // initialize the JPanel
        super();
        
        switcher = MainGUI.getInstance();
        
        // get encounter data
        this.tile = tile;
        this.engine = tile.getEngine();
        
        // get GameState data
        gameState = GameState.getInstance();
        player = gameState.getPlayer();
        
        
        
        // make the text area
        log = new Log("You encountered a...\n");
        // print the initial text
        for (Entity e : tile.getContents())
        {
            log.append(e.getIntro() + "\n");
        }
        
        
        
        // make the hotbar
        hotbar = new Hotbar(tile);
        
        screenPauser = new ScreenPauser(this, hotbar.getButton(3), () -> {
            int s = engine.getSelectionLayer();
            return ((s == 0) || (s == 4));
        });
        
        // make the graphic
        graphic = new EncounterGraphic(tile);
        
        
        test = new JPanel()
        {
            @Override 
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                int W = getWidth();
                int H = getHeight();

                // resize the components (easier than layout mangers)
                int log_w = Math.min((int)((W * 5) / 16), 300);  // default = 250 @ W=800 (transition @ W=960)
                int hot_h = Math.min((int)(H / 5), 80);          // default = 80 @ H=600 (transition @ 400)
                hotbar.setBounds(0, H - hot_h, W - log_w, hot_h);
                log.setBounds(W - log_w, 0, log_w, H);
                graphic.setBounds(0, 0, W - log_w, H - hot_h);
            }
            
            @Override
            protected void paintChildren(Graphics g) 
            {
                super.paintChildren(g);

                screenPauser.draw((Graphics2D) g);
            }
        };
        
        test.add(log);
        test.add(hotbar);
        test.add(graphic);
        
        this.add(test);
        
        repaint();
        
    }
    
     
    /**
     * Override the paintComponeent method for custom rendering
     * 
     * @param g The graphics object.
     */
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        int W = getWidth();
        int H = getHeight();
        
        // resize the inner JPanel (easier than layout mangers)
        test.setBounds(0, 0, W, H);
        
    }
    
    
    
    public void outputTranslator(Creature source, Creature target, int damage)
    {
        switch (damage) {
            case DamageCode.DEFENDED -> // source defended
                logDefense(source);
            case DamageCode.MISSED -> // source missed
                logMiss(source, target);
            case DamageCode.RAN_AWAY_SUCCESSFUL -> //source ran away successfully
                logRanAway(true);
            case DamageCode.RAN_AWAY_FAILED -> // source could not get away
                logRanAway(false);
            default -> // source hit
                logAttack(source, target, damage);
        }
    }
    
    /**
     * Appends an attack action to the log
     * @param source The Creature who made the attack
     * @param target The Creature who got attacked
     * @param damage The amount of damage dealt
     */
    public void logAttack(Creature source, Creature target, int damage)
    {
        String atk = source.getSelectedAttackName();
        Boolean killed = target.getCurrentHP() < 1;
        SwingUtilities.invokeLater( () ->
        {
            if (source == player)
            {
                
                log.append("\nYou attacked the " + target.getName() + " with your " +  atk + ".\n" + 
                    "It dealt " + damage + " damage!\n");
                if (killed)
                {
                    log.append("You killed it!\n");
                }
            }
            else
            {
                log.append("The " + source.getName() + " attacked you with its " + atk + ".\n" + 
                    "It dealt " + damage + " damage!\n");
                if (killed)
                {
                    log.append("\nYou have been slain.\n");
                }
            }
        });
    }
    
    /**
     * Appends a defense action to the log
     * @param defender The Creature who defended
     */
    public void logDefense(Creature defender)
    {
        if (defender == player)
        {
            SwingUtilities.invokeLater( () -> 
            {
                log.append("\nYou defended!\n");
            });
        }
        else
        {
            SwingUtilities.invokeLater( () -> 
            {
                log.append(defender.getName() + " defended!\n");
            });
        }
    }
    
    /**
     * Appends a missed attack action to the log
     * @param source The Creature who made the attack
     * @param target The Creature who got attacked
     */
    public void logMiss(Creature source, Creature target)
    {
        String atk = source.getSelectedAttackName();
        if (source == player)
        {
            SwingUtilities.invokeLater( () -> 
            {
                log.append("\nYou attacked the " + target.getName() + " with your " +  atk + ".\n" + 
                    "You missed.\n");
            });
            
        }
        else
        {
            SwingUtilities.invokeLater( () -> 
            {
                log.append("The " + source.getName() + " attacked you with its " + atk + ".\n" + 
                        "It missed!\n");
            });
        }
    }
    
    /**
     * Appends an arbitrary string to the log
     * @param message The String to print
     */
    public void outputTranslator(String message)
    {
        log.append(message + "\n");
    }
    
    /**
     * Appends a flee action to the log
     * @param succeeded whether the player succeeded or not
     */
    public void logRanAway(Boolean succeeded)
    {
        
        SwingUtilities.invokeLater( () -> 
        {
            log.append("\nYou tried to run away from the fight...\n");
            if (succeeded)
            {
                log.append("You got away successfully!\n");
            }
            else
            {
                log.append("You couldn't get away.\n");
            }
        });
            
    }
    
    /**
     * Runs the combat encounter in the background, allowing for waiting
     */
    public void startCombat()
    {
        SwingWorker<Void, Void> bgThread = new SwingWorker<Void, Void>()
        {
            @Override
            protected Void doInBackground()
            {
                synchronized (GameState.getInstance().getPlayer())
                {
                    engine.combatEncounter(tile.getContents());
                }
                return null;
            }
            
            @Override
            protected void done()
            {
                // Combat is over and we're back on the EDT
                // small wait then go back to the Dungeon Map
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(EncounterScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
                tile.endCombat();
                switcher.changeScene("DUNGEON_MAP");
            }
        };
        
        bgThread.execute();
    }
    
    /**
     * Waits for the player to take their turn
     * To be called from the domain model
     */
    public void waitForPlayer()
    {
        repaint();
        synchronized (player)
        {
            try {
                while (!player.hasTakenTurn())
                {
                    player.wait();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(EncounterScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void leaveCombat()
    {
        
    }
}
