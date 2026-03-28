package com.mycompany.yogibear;

import javax.swing.*;

/**
 * Creates and manages the main game window and menu bar.
 * Sets up the JFrame, GameEngine panel, and game menu items.
 * 
 * @author USER
 * @version 1.0
 */
public class GameGUI {
    /** Main game window */
    private final JFrame frame;
    
    /** Game engine panel containing all game logic */
    private final GameEngine gamePanel;

    /**
     * Constructs the game GUI with window, menu bar, and game panel.
     * Creates menu items for restarting and viewing high scores.
     */
    public GameGUI() {
        frame = new JFrame("Yogi Bear - Picnic Basket Collector");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        gamePanel = new GameEngine(frame);
        frame.add(gamePanel);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game");
        JMenuItem restart = new JMenuItem("Restart Game");
        JMenuItem highscores = new JMenuItem("High Scores");

        restart.addActionListener(e -> gamePanel.restartGame());
        highscores.addActionListener(e -> Database.showHighScores(frame));

        menu.add(restart);
        menu.add(highscores);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}