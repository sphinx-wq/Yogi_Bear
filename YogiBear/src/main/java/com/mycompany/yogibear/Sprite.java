package com.mycompany.yogibear;

import java.awt.*;

/**
 * Base class for all game sprites (characters, obstacles, items).
 * Provides common functionality for position, size, rendering, and collision detection.
 * 
 * @author USER
 * @version 1.0
 */
public class Sprite {
    /** X-coordinate position in pixels */
    protected int x;
    
    /** Y-coordinate position in pixels */
    protected int y;
    
    /** Width of the sprite in pixels */
    protected int width;
    
    /** Height of the sprite in pixels */
    protected int height;
    
    /** Sprite image to render */
    protected Image image;
    
    /** Whether this sprite is active and should be rendered */
    protected boolean active = true;

    /**
     * Creates a new sprite with the specified position, size, and image.
     * 
     * @param x X-coordinate in pixels
     * @param y Y-coordinate in pixels
     * @param width Width in pixels
     * @param height Height in pixels
     * @param image The image to render for this sprite
     */
    public Sprite(int x, int y, int width, int height, Image image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    /**
     * Renders this sprite on the screen if it is active.
     * 
     * @param g Graphics context to draw on
     */
    public void draw(Graphics g) {
        if (active) g.drawImage(image, x, y, width, height, null);
    }

    /**
     * Gets the bounding rectangle for collision detection.
     * 
     * @return Rectangle representing this sprite's bounds
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    /**
     * Checks if this sprite collides with another sprite.
     * 
     * @param other The other sprite to check collision with
     * @return true if sprites intersect, false otherwise
     */
    public boolean collides(Sprite other) {
        return active && other.active && getBounds().intersects(other.getBounds());
    }

    /**
     * Gets the current X-coordinate.
     * 
     * @return X-coordinate in pixels
     */
    public int getX() { return x; }
    
    /**
     * Gets the current Y-coordinate.
     * 
     * @return Y-coordinate in pixels
     */
    public int getY() { return y; }
    
    /**
     * Gets the X-coordinate of the sprite's center point.
     * 
     * @return Center X-coordinate in pixels
     */
    public int getCenterX() {
        return x + width / 2;
    }

    /**
     * Gets the Y-coordinate of the sprite's center point.
     * 
     * @return Center Y-coordinate in pixels
     */
    public int getCenterY() {
        return y + height / 2;
    }
    
    /**
     * Sets the X-coordinate position.
     * 
     * @param x New X-coordinate in pixels
     */
    public void setX(int x) { this.x = x; }
    
    /**
     * Sets the Y-coordinate position.
     * 
     * @param y New Y-coordinate in pixels
     */
    public void setY(int y) { this.y = y; }
}