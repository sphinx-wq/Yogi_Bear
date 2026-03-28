package com.mycompany.yogibear;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Manages game level data including sprites, obstacles, and level loading.
 * Parses level files and creates game objects from text representations.
 * 
 * @author USER
 * @version 1.0
 */
public class Level {

    /** List of obstacle sprites (trees and rocks) */
    private final List<Sprite> obstacles = new ArrayList<>();
    
    /** List of collectible baskets */
    private final List<Basket> baskets = new ArrayList<>();
    
    /** List of ranger enemies */
    private final List<Ranger> rangers = new ArrayList<>();
    
    /** The player character Yogi */
    private Yogi yogi;

    /**
     * Loads a level from a text file in the resources/levels directory.
     * Parses characters to create game objects: Y=Yogi, R=Ranger, B=Basket, T=Tree, M=Mountain.
     * 
     * @param name Level filename (e.g., "level01.txt")
     * @param engine GameEngine instance for accessing sprite images
     * @throws IOException if level file cannot be read
     */
    public void load(String name, GameEngine engine) throws IOException {
        obstacles.clear();
        baskets.clear();
        rangers.clear();
        yogi = null;

        BufferedReader br = new BufferedReader(new InputStreamReader(
            Objects.requireNonNull(getClass().getResourceAsStream("/levels/" + name))));

        int y = 0;
        String line;
        while ((line = br.readLine()) != null) {
            if (line.length() < 20) continue;

            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                if (c == ' ' || c == '.') continue;

                int px = x * 40;
                int py = y * 40;

                switch (c) {
                    case 'Y' -> yogi = new Yogi(px, py, engine.yogiImg);
                    case 'R' -> rangers.add(new Ranger(px, py, engine.rangerImg));
                    case 'B' -> baskets.add(new Basket(px + 5, py + 5, engine.basketImg));
                    case 'T' -> obstacles.add(new Obstacle(px, py, engine.treeImg));
                    case 'M' -> obstacles.add(new Obstacle(px, py, engine.rockImg));
                }
            }
            y++;
        }
        br.close();
    }

    /**
     * Gets the list of obstacle sprites.
     * 
     * @return List of obstacles
     */
    public List<Sprite> getObstacles() { return obstacles; }
    
    /**
     * Gets the list of basket sprites.
     * 
     * @return List of baskets
     */
    public List<Basket> getBaskets() { return baskets; }
    
    /**
     * Gets the list of ranger sprites.
     * 
     * @return List of rangers
     */
    public List<Ranger> getRangers() { return rangers; }
    
    /**
     * Gets the Yogi player character.
     * 
     * @return Yogi instance
     */
    public Yogi getYogi() { return yogi; }

    /**
     * Checks if all baskets have been collected.
     * 
     * @return true if no baskets remain, false otherwise
     */
    public boolean allBasketsCollected() {
        return baskets.isEmpty();
    }
}