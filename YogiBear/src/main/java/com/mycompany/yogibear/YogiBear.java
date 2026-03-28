package com.mycompany.yogibear;

import javax.swing.*;

/**
 * Main entry point for the Yogi Bear game application.
 * Initializes the database and launches the game GUI.
 * 
 * @author USER
 * @version 1.0
 */
public class YogiBear {
    
    /**
     * Application entry point. Initializes the MySQL database
     * and creates the game window on the Event Dispatch Thread.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Database.init();
        SwingUtilities.invokeLater(GameGUI::new);
    }
}