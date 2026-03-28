package com.mycompany.yogibear;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class GameEngine extends JPanel implements KeyListener {

    public static final int WIDTH = 800, HEIGHT = 600;
    private final JFrame frame;
    private final Timer timer;
    public Level level;
    private int currentLevel = 1;
    private long gameStartTime;
    private long levelStartTime;

    private final Set<Integer> pressedKeys = new HashSet<>();
    private static final int MOVE_SPEED = 4; 

    public final Image yogiImg   = loadImage("/images/yogi.png");
    public final Image rangerImg = loadImage("/images/ranger.png");
    public final Image basketImg = loadImage("/images/basket.png");
    public final Image treeImg   = loadImage("/images/tree.png");
    public final Image rockImg   = loadImage("/images/rock.png");

    /**
     * Load an image from the resources folder
     * Falls back to programmatically created image if file not found
     */
    private Image loadImage(String path) {
        try {
            BufferedImage img = ImageIO.read(
                Objects.requireNonNull(getClass().getResourceAsStream(path))
            );
            return img;
        } catch (IOException | NullPointerException e) {
            System.err.println("Could not load image: " + path + " - Using fallback");
            
            if (path.contains("yogi")) return createYogi();
            if (path.contains("ranger")) return createRanger();
            if (path.contains("basket")) return createBasket();
            if (path.contains("tree")) return createTree();
            if (path.contains("rock")) return createRock();
            
            return createPlaceholder(Color.MAGENTA); 
        }
    }

    private Image createYogi() {
        BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(200, 120, 0));
        g.fillOval(4, 4, 32, 32);
        g.setColor(Color.BLACK);
        g.fillOval(10, 10, 8, 8);
        g.fillOval(22, 10, 8, 8);
        g.fillRect(16, 20, 8, 8);
        g.setColor(new Color(255, 200,100));
        g.fillOval(8, 24, 24, 12);
        g.dispose();
        return img;
    }

    private Image createRanger() {
        BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.GREEN.darker().darker());
        g.fillRect(8, 8, 24, 28);
        g.setColor(new Color(200, 150, 80));
        g.fillOval(12, 4, 16, 16);
        g.setColor(new Color(100, 40, 0));
        g.fillRect(8, 0, 24, 8);
        g.setColor(Color.BLACK);
        g.fillOval(14, 8, 4, 4);
        g.fillOval(22, 8, 4, 4);
        g.dispose();
        return img;
    }

    private Image createBasket() {
        BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(180, 120, 40));
        g.fillRoundRect(6, 12, 28, 22, 8, 8);
        g.setColor(new Color(255, 220, 100));
        g.fillRect(4, 10, 32, 8);
        g.setColor(Color.RED);
        g.fillRect(4, 10, 32, 4);
        g.setColor(Color.WHITE);
        for (int i = 0; i < 5; i++) g.fillRect(8 + i*6, 10, 3, 4);
        g.dispose();
        return img;
    }

    private Image createTree() {
        BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(100, 50, 0));
        g.fillRect(16, 20, 8, 16);
        g.setColor(new Color(0, 120, 0));
        g.fillOval(4, 4, 32, 28);
        g.setColor(new Color(0, 140, 0));
        g.fillOval(8, 8, 24, 24);
        g.dispose();
        return img;
    }

    private Image createRock() {
        BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.GRAY.darker());
        g.fillOval(4, 6, 32, 28);
        g.setColor(Color.DARK_GRAY);
        g.fillOval(8, 10, 24, 20);
        g.dispose();
        return img;
    }

    private Image createPlaceholder(Color c) {
        BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(c);
        g2d.fillRoundRect(2, 2, 36, 36, 12, 12);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(2, 2, 36, 36, 12, 12);
        g2d.dispose();
        return img;
    }

    public GameEngine(JFrame frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(34, 139, 34));
        setFocusable(true);
        
        // Add KeyListener for smooth movement
        addKeyListener(this);
        
        gameStartTime = System.currentTimeMillis();
        level = new Level();
        loadCurrentLevel();

        timer = new Timer(16, e -> gameLoop()); // ~60 FPS
        timer.start();
    }

    private void loadCurrentLevel() {
        try {
            level.load("level" + String.format("%02d", currentLevel) + ".txt", this);
            levelStartTime = System.currentTimeMillis();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Level file not found: level" + String.format("%02d", currentLevel) + ".txt");
        }
    }

    /**
     * Handle continuous movement based on currently pressed keys
     */
    private void handleMovement() {
        Yogi yogi = level.getYogi();
        if (yogi == null) return;

        int dx = 0;
        int dy = 0;

        if (pressedKeys.contains(KeyEvent.VK_W)) dy -= MOVE_SPEED;
        if (pressedKeys.contains(KeyEvent.VK_S)) dy += MOVE_SPEED;
        if (pressedKeys.contains(KeyEvent.VK_A)) dx -= MOVE_SPEED;
        if (pressedKeys.contains(KeyEvent.VK_D)) dx += MOVE_SPEED;

        if (dx == 0 && dy == 0) return;

        Rectangle next = new Rectangle(yogi.getX() + dx, yogi.getY() + dy, 40, 40);
        boolean blocked = level.getObstacles().stream()
                .anyMatch(o -> next.intersects(o.getBounds()));

        if (!blocked && next.x >= 0 && next.x + 40 <= WIDTH 
                && next.y >= 0 && next.y + 40 <= HEIGHT) {
            yogi.setX(yogi.getX() + dx);
            yogi.setY(yogi.getY() + dy);
        }
    }

    private void gameLoop() {
        Yogi yogi = level.getYogi();
        if (yogi == null) return;

        handleMovement();

        level.getBaskets().removeIf(b -> yogi.collides(b));

        for (Ranger r : level.getRangers()) {
            r.update(level.getObstacles());
            if (r.isNear(yogi)) {
                yogi.respawn();
                if (yogi.isDead()) {
                    gameOver();
                }
                break;
            }
        }

        if (level.allBasketsCollected()) {
            currentLevel++;
            if (currentLevel > 10) {
                long totalSeconds = (System.currentTimeMillis() - gameStartTime) / 1000;
                String name = JOptionPane.showInputDialog(frame,
                    "🎉 YOU WIN THE ENTIRE GAME! 🎉\n\n" +
                    "Total time: " + String.format("%02d:%02d\n\n", totalSeconds/60, totalSeconds%60) +
                    "Enter your name:",
                    "Victory!", JOptionPane.PLAIN_MESSAGE);
                
                if (name != null && !name.trim().isEmpty()) {
                    Database.saveScore(name.trim(), currentLevel, totalSeconds);
                }
                restartGame();
            } else {
                loadCurrentLevel();
            }
        }

        repaint();
    }

    private void gameOver() {
        long totalSeconds = (System.currentTimeMillis() - gameStartTime) / 1000;
        
        String name = JOptionPane.showInputDialog(frame,
            "GAME OVER!\n" +
            "You reached level " + currentLevel + "\n" +
            "Total time: " + String.format("%02d:%02d\n\n", totalSeconds/60, totalSeconds%60) +
            "Enter your name:",
            "Game Over", JOptionPane.PLAIN_MESSAGE);

        if (name != null && !name.trim().isEmpty()) {
            Database.saveScore(name.trim(), currentLevel, totalSeconds);
        }

        restartGame();
    }

    public void restartGame() {
        currentLevel = 1;
        gameStartTime = System.currentTimeMillis();
        loadCurrentLevel();
        if (level.getYogi() != null) {
            level.getYogi().resetLives();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        level.getObstacles().forEach(s -> s.draw(g));
        level.getBaskets().forEach(s -> s.draw(g));
        level.getRangers().forEach(s -> s.draw(g));
        if (level.getYogi() != null) {
            level.getYogi().draw(g);
        }
        g.setColor(Color.RED);
        int heartX = 120; 
        for (int i = 0; i < level.getYogi().getLives(); i++) {
        g.fillOval(heartX + i * 25, 55, 15, 15);
        g.fillOval(heartX + 10 + i * 25, 55, 15, 15);
        int[] xPoints = {heartX + 5 + i * 25, heartX + 20 + i * 25, heartX + 12 + i * 25};
        int[] yPoints = {60, 60, 75};
        g.fillPolygon(xPoints, yPoints, 3);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial Black", Font.BOLD, 20));
        g.drawString("Level: " + currentLevel, 20, 40);
        g.drawString("Lives: " + level.getYogi().getLives(), 20, 70);
        g.drawString("Baskets left: " + level.getBaskets().size(), 20, 100);

        long secs = (System.currentTimeMillis() - levelStartTime) / 1000;
        g.drawString(String.format("Time: %02d:%02d", secs/60, secs%60), 600, 40);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
     
    }
}