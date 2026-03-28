package com.mycompany.yogibear;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.List;

/**
 * Represents a park ranger enemy that patrols the level.
 * Rangers move horizontally and vertically, catching Yogi if too close.
 * 
 * <p>Movement alternates between horizontal and vertical every 50 frames.
 * If within 50 pixels of Yogi, the ranger catches him causing respawn.</p>
 * 
 * @author USER
 * @version 1.0
 */
public class Ranger extends Sprite {
    
    /** Whether currently moving horizontally (true) or vertically (false) */
    private boolean horizontal = true;
    
    /** Direction multiplier: 1 for right/down, -1 for left/up */
    private int dir = 1;
    
    /** Frame counter for alternating movement direction */
    private int steps = 0;

    /**
     * Creates a ranger at the specified position.
     * 
     * @param x Initial X-coordinate in pixels
     * @param y Initial Y-coordinate in pixels
     * @param img Sprite image for the ranger
     */
    public Ranger(int x, int y, Image img) {
        super(x, y, 40, 40, img);
    }

    /**
     * Updates ranger position and handles collision with obstacles.
     * Alternates movement axis every 50 frames. Reverses direction when blocked.
     * 
     * @param obstacles List of obstacle sprites to check collision against
     */
    public void update(List<Sprite> obstacles) {
        steps++;

        if (steps >= 50) {
            horizontal = !horizontal;
            steps = 0;
        }

        int speed = 4;
        int dx = horizontal ? dir * speed : 0;
        int dy = horizontal ? 0 : dir * speed;

        Rectangle future = new Rectangle(x + dx, y + dy, width, height);

        boolean blocked = obstacles.stream().anyMatch(s -> future.intersects(s.getBounds()));

        if (!blocked 
            && x + dx >= 0 && x + dx + width <= 800 
            && y + dy >= 0 && y + dy + height <= 600) {
            x += dx;
            y += dy;
        } else {
            dir = -dir;
            if (Math.random() < 0.3) horizontal = !horizontal;
        }
    }

    /**
     * Checks if ranger is close enough to catch Yogi.
     * Uses center-to-center distance with 50 pixel threshold.
     * 
     * @param yogi The Yogi character to check distance to
     * @return true if distance &lt; 50 pixels, false otherwise
     */
    public boolean isNear(Yogi yogi) {
        double dx = this.getCenterX() - yogi.getCenterX();
        double dy = this.getCenterY() - yogi.getCenterY();
        return Math.hypot(dx, dy) < 50;
    }
}