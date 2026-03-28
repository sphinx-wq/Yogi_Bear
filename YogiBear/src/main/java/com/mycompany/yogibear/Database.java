package com.mycompany.yogibear;

import javax.swing.*;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Manages MySQL database operations for high scores.
 * Handles connection, table creation, score saving, and retrieval.
 *
 * <p>Database credentials are loaded from {@code db.properties} in the
 * classpath root (src/main/resources/db.properties). Never hard-code
 * credentials in source files.</p>
 *
 * <p>Stores player names, levels reached, completion times, and timestamps.
 * Scores are ranked by level (descending) then time (ascending).</p>
 *
 * @author USER
 * @version 1.0
 */
public class Database {

    /** Loaded database configuration properties */
    private static final Properties props = loadProps();

    /** JDBC connection URL for MySQL yogibear database */
    private static final String DB_URL = props.getProperty("db.url");

    /** Database username */
    private static final String DB_USER = props.getProperty("db.user");

    /** Database password */
    private static final String DB_PASSWORD = props.getProperty("db.password");

    /**
     * Loads database credentials from db.properties on the classpath.
     * Throws a RuntimeException at startup if the file is missing,
     * so misconfiguration is caught immediately rather than at query time.
     *
     * @return loaded Properties object
     */
    private static Properties loadProps() {
        Properties p = new Properties();
        try (InputStream in = Database.class.getResourceAsStream("/db.properties")) {
            if (in == null) {
                throw new RuntimeException(
                    "db.properties not found in classpath.\n" +
                    "Copy db.properties.example to db.properties and fill in your credentials."
                );
            }
            p.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load db.properties: " + e.getMessage(), e);
        }
        return p;
    }

    /**
     * Initializes the database and creates the highscores table if needed.
     * Should be called once at application startup.
     */
    public static void init() {
        try (Connection c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement s = c.createStatement()) {

            s.execute("""
                CREATE TABLE IF NOT EXISTS highscores (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    player_name VARCHAR(100) NOT NULL,
                    level_reached INT NOT NULL,
                    time_seconds BIGINT NOT NULL,
                    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """);
            System.out.println("✓ MySQL database initialized successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Database connection failed!\n" +
                "Make sure MySQL is running.\n" +
                "Error: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Saves a player's score to the database.
     *
     * @param name    Player name (trimmed)
     * @param level   Level reached (1-10)
     * @param seconds Total time in seconds
     */
    public static void saveScore(String name, int level, long seconds) {
        String sql = "INSERT INTO highscores(player_name, level_reached, time_seconds) VALUES(?, ?, ?)";
        try (Connection c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            ps.setInt(2, level);
            ps.setLong(3, seconds);
            ps.executeUpdate();
            System.out.println("✓ Score saved: " + name + " - Level " + level);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Failed to save score: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays the top 10 high scores in a dialog window.
     * Scores are ordered by level reached (descending) and time (ascending).
     *
     * @param parent Parent frame for the dialog
     */
    public static void showHighScores(JFrame parent) {
        List<Score> scores = new ArrayList<>();
        String sql = """
            SELECT player_name, level_reached, time_seconds, timestamp
            FROM highscores ORDER BY level_reached DESC, time_seconds ASC LIMIT 10
            """;
        try (Connection c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                scores.add(new Score(
                    rs.getString(1), rs.getInt(2),
                    rs.getLong(3), rs.getString(4)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent,
                "Failed to load high scores: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("<html><body style='font-family: Arial; padding: 10px;'>");
        sb.append("<h2 style='text-align: center; color: #2c5aa0;'>🏆 TOP 10 HIGH SCORES 🏆</h2>");
        sb.append("<table border='1' cellpadding='8' cellspacing='0' style='border-collapse: collapse; width: 100%;'>");
        sb.append("<tr style='background-color: #4a90e2; color: white;'>");
        sb.append("<th>Rank</th><th>Player</th><th>Level</th><th>Time</th><th>Date</th>");
        sb.append("</tr>");

        if (scores.isEmpty()) {
            sb.append("<tr><td colspan='5' style='text-align: center; padding: 20px;'>");
            sb.append("No scores yet! Be the first!</td></tr>");
        } else {
            for (int i = 0; i < scores.size(); i++) {
                Score sc = scores.get(i);
                long m = sc.seconds / 60;
                long s = sc.seconds % 60;
                String bgColor = (i % 2 == 0) ? "#f0f0f0" : "#ffffff";
                sb.append(String.format("<tr style='background-color: %s;'>", bgColor));
                sb.append(String.format("<td style='text-align: center;'>%d</td>", i + 1));
                sb.append(String.format("<td>%s</td>",
                    sc.name.length() > 15 ? sc.name.substring(0, 15) + "..." : sc.name));
                sb.append(String.format("<td style='text-align: center;'>%d</td>", sc.level));
                sb.append(String.format("<td style='text-align: center;'>%02d:%02d</td>", m, s));
                sb.append(String.format("<td>%s</td>", sc.date.substring(0, 16)));
                sb.append("</tr>");
            }
        }
        sb.append("</table>");
        sb.append("</body></html>");

        JLabel label = new JLabel(sb.toString());
        JOptionPane.showMessageDialog(parent, label, "High Scores", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Internal class representing a single high score entry.
     */
    static class Score {
        /** Player name */
        String name;

        /** Timestamp of score */
        String date;

        /** Level reached */
        int level;

        /** Total time in seconds */
        long seconds;

        /**
         * Creates a score entry.
         *
         * @param n Player name
         * @param l Level reached
         * @param s Time in seconds
         * @param d Timestamp string
         */
        Score(String n, int l, long s, String d) {
            name = n; level = l; seconds = s; date = d;
        }
    }
}