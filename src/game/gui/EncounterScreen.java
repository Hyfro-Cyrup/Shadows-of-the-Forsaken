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
import static game.model.GameState.WinState.LOST;
import static game.model.GameState.WinState.PLAYING;
import static game.model.GameState.WinState.WON;
import game.model.Player;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
    private final JPanel innerPanel;
    
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
            return (!(s == 1 || s == 2 || s == 3));
        });
        
        // make the graphic
        graphic = new EncounterGraphic(tile);
        
        
        innerPanel = new JPanel()
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
        
        innerPanel.add(log);
        innerPanel.add(hotbar);
        innerPanel.add(graphic);
        
        this.add(innerPanel);
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    screenPauser.pause();
                    return;
                }
                if (!screenPauser.isPaused())
                {
                   switch (e.getKeyCode()) 
                   {
                        case KeyEvent.VK_C -> {
                            // cheat code
                            engine.cheatCode();
                            synchronized (player)
                            {
                                player.notify();
                            }
                        }
                        case KeyEvent.VK_SPACE, KeyEvent.VK_ENTER -> {
                            // keybinds I wanted for leaving combat
                            hotbar.getButton(4).doClick();
                        }
                    } 
                   repaint();
                }
            }
        });
        
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
        innerPanel.setBounds(0, 0, W, H);
        
    }
    
    
    /**
     * Finds the correct GUI response to an output from the EncounterEngine
     * @param source The acting Creature
     * @param target The Creature being acted upon
     * @param damage The damage dealt or DamageCode indicating action
     */
    public void outputTranslator(Creature source, Creature target, int damage)
    {
        switch (damage) {
            case DamageCode.DEFENDED -> // source defended
                logDefense(source);
            case DamageCode.MISSED -> // source missed
                logMiss(source, target);
            case DamageCode.RAN_AWAY_SUCCESSFUL -> // player ran away successfully
                logRanAway(true);
            case DamageCode.RAN_AWAY_FAILED -> // player could not get away
                logRanAway(false);
            case DamageCode.INVESTIGATED -> // player investigated non-combat
                logInvestigation();
            case DamageCode.TOUCHED -> // player interacted non-combat
                logInteraction();
            case DamageCode.GAME_WON -> // player escaped!
                logWin();
            case DamageCode.STATUS_EFFECTS -> 
                logStatus(source);
            default -> // source hit
                logAttack(source, target, damage);
        }
        repaint();
    }
    
    /**
     * Appends an arbitrary string to the log
     * @param message The String to print
     */
    public void outputTranslator(String message)
    {
        SwingUtilities.invokeLater(() ->{
           log.append(message + "\n"); 
        });
        repaint();
    }
    
    /**
     * Appends an attack action to the log
     * @param source The Creature who made the attack
     * @param target The Creature who got attacked
     * @param damage The amount of damage dealt
     */
    private void logAttack(Creature source, Creature target, int damage)
    {
        String atk = source.getSelectedAttackName();
        Boolean killed = target.getCurrentHP() < 1;
        SwingUtilities.invokeLater( () ->
        {
            if (source == player)
            {
                
                log.append("\nYou attacked the " + target.getName() + " with your " +  atk + ".\n" + 
                    "You dealt " + damage + " damage!\n");
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
                    log.append("You have been slain.\n");
                }
            }
        });
    }
    
    /**
     * Appends a defense action to the log
     * @param defender The Creature who defended
     */
    private void logDefense(Creature defender)
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
    private void logMiss(Creature source, Creature target)
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
     * Appends a flee action to the log
     * @param succeeded whether the player succeeded or not
     */
    private void logRanAway(Boolean succeeded)
    {
        Boolean combat = !engine.combatOver();
        Boolean fray = (gameState.getWinState() == GameState.WinState.PLAYING); 
        SwingUtilities.invokeLater( () -> 
        {
            if (combat)
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
            }
            else if (fray) // only print if we're exiting toward the dungeon map, not the win screen
            {
                log.append("Back to the fray...\n");
            }
            
        });
            
    }
    
    /**
     * Appends an investigate action to the log
     */
    private void logInvestigation()
    {
        Boolean isLadder = tile.containsLadder();
        SwingUtilities.invokeLater( () -> 
        {
            if (isLadder)
            {
                log.append("There's a trapdoor at the top. \nCould this be the way out?\n");
            }
            else
            {
                log.append("Something is glimmering inside the chest.\n");
            }
        });
            
    }
    
    /**
     * Appends an interaction action to the log
     */
    private void logInteraction()
    {
        Boolean isLadder = tile.containsLadder();
        Boolean hasKey = player.hasKey();
        SwingUtilities.invokeLater( () -> 
        {
            if (isLadder)
            {
                if (hasKey)
                {
                    log.append("The key fits!\n The way is open!\n");
                    hotbar.unlockExit();
                }
                else
                {
                    log.append("The trapdoor is locked.\n");
                }
            }
            else
            {
                if (hasKey)
                {
                    log.append("There's nothing else inside.\n");
                }
                else
                {
                    log.append("You found a key!\nWonder where it goes.\n");
                }
            }
        });
            
    }
    
    /**
     * Appends an ascent action to the log
     */
    private void logWin()
    {
        SwingUtilities.invokeLater( () -> 
        {
            log.append("\nDaylight fills your eyes...");
        });
    }

        /**
     * Appends status effects (bleeding, burning, poisoned, etc. - into the log) 
     */
    private void logStatus(Creature source){
        for (int i = 0;i<7;i++){
            if (source.getStatus()[i]!=0){
                if (source == player){
                    log.append("\nYou are "+translateStatus(i)+" ("+source.getStatus()[i]+")");
                }
                else{
                    log.append("\n"+source.getName()+" is "+translateStatus(i)+" ("+source.getStatus()[i]+")");
                }
            }
        }
    }

    /**
     * Takes in the index of the current element of the Condition Array being read and returns appropriate string 
     */
    private String translateStatus(int index){
        String conditionWord; 
            switch (index) {
            case 1 -> 
                conditionWord = "Burning";
            case 2 -> 
                conditionWord = "Poisoned";
            case 3 -> 
                conditionWord = "Frozened";
            case 4 -> 
                conditionWord = "Stunned";
            case 5 -> 
                conditionWord = "Doomed";
            case 6 -> 
                conditionWord = "Condemned";
            default -> 
                conditionWord = "Bleeding";
            }
        return conditionWord;
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
                    engine.runEncounter(tile.getContents());
                }
                return null;
            }
            
            @Override
            protected void done()
            {
                // Combat is over and we're back on the EDT
                // small wait then go back to the Dungeon Map
                try {
                    Thread.sleep(engine.combatOver() ? 1000 : 2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(EncounterScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
                tile.endEncounter();
                
                switcher.changeScene(
                    switch (gameState.getWinState()) {
                        case PLAYING ->  "DUNGEON_MAP";
                        case WON ->  "WIN_SCREEN";
                        case LOST ->  "LOSE_SCREEN";
                    }
                );
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
    
}
