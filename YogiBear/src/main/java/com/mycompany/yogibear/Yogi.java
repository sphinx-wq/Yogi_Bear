package com.mycompany.yogibear;

import java.awt.Image;
import java.awt.Point;

/**
 * Represents the player character Yogi Bear.
 * Manages Yogi's lives, spawn point, and respawn mechanics.
 * 
 * <p>Yogi starts with 3 lives and respawns at the level entrance
 * when caught by a ranger. Game ends when all lives are lost.</p>
 * 
 * @author USER
 * @version 1.0
 */
public class Yogi extends Sprite {
    
    /** Spawn point where Yogi respawns after being caught */
    private final Point spawn;
    
    /** Current number of lives (0-3) */
    private int lives = 3;

    /**
     * Creates Yogi at the specified position.
     * 
     * @param x Initial X-coordinate in pixels
     * @param y Initial Y-coordinate in pixels
     * @param img Sprite image for Yogi
     */
    public Yogi(int x, int y, Image img) {
        super(x, y, 40, 40, img);
        this.spawn = new Point(x, y);
    }

    /**
     * Respawns Yogi at the spawn point and decrements lives.
     * Called when caught by a ranger.
     */
    public void respawn() {
        this.x = spawn.x;
        this.y = spawn.y;
        lives--;
    }

    /**
     * Gets the current number of lives remaining.
     * 
     * @return Number of lives (0-3)
     */
    public int getLives() {
        return lives;
    }

    /**
     * Checks if Yogi has lost all lives.
     * 
     * @return true if lives ≤ 0, false otherwise
     */
    public boolean isDead() {
        return lives <= 0;
    }

    /**
     * Resets Yogi's lives back to 3.
     * Called when restarting the game.
     */
    public void resetLives() {
        this.lives = 3;
    }
}