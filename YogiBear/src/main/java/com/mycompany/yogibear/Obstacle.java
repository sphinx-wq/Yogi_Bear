package com.mycompany.yogibear;

import java.awt.Image;

/**
 * Represents an impassable obstacle in the game (tree or rock).
 * Blocks movement for both Yogi and rangers.
 * 
 * @author USER
 * @version 1.0
 */
public class Obstacle extends Sprite {
    
    /**
     * Creates an obstacle at the specified position.
     * 
     * @param x X-coordinate in pixels
     * @param y Y-coordinate in pixels
     * @param img Sprite image for the obstacle
     */
    public Obstacle(int x, int y, Image img) {
        super(x, y, 40, 40, img);
    }
}