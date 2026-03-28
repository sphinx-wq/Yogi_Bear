package com.mycompany.yogibear;

import java.awt.Image;

/**
 * Represents a collectible picnic basket in the game.
 * Yogi must collect all baskets to complete a level.
 * 
 * @author USER
 * @version 1.0
 */
public class Basket extends Sprite {
    
    /**
     * Creates a basket at the specified position.
     * 
     * @param x X-coordinate in pixels
     * @param y Y-coordinate in pixels
     * @param img Sprite image for the basket
     */
    public Basket(int x, int y, Image img) {
        super(x, y, 30, 30, img);
    }
}