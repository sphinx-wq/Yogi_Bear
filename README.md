# Yogi Bear: Picnic Basket Collector 🐻

A Java Swing desktop game inspired by the classic Yogi Bear cartoon. Players guide Yogi through Yellowstone National Park, collecting picnic baskets across 10 levels while avoiding park rangers. High scores are persisted to a MySQL database.

## Gameplay

Navigate Yogi through a grid-based forest map using WASD keys, collecting all picnic baskets on each level to advance. Park rangers patrol the map horizontally and vertically — if one gets too close, Yogi loses a life. With 3 lives and 10 levels to complete, the game tracks your total time and saves your score to a leaderboard when the run ends.

## Features

- 10 hand-crafted levels loaded from text-based map files
- Ranger AI with dynamic horizontal/vertical patrolling and randomised direction changes
- Collision detection for obstacles, screen boundaries, and basket collection
- 3-life system with respawning at level entrance
- Live HUD showing current level, lives, baskets remaining, and elapsed time
- Victory screen after completing all 10 levels
- MySQL-backed high score table (top 10, ranked by level then time)
- Menu bar with restart and high scores options
- Programmatically generated fallback sprites — no external image files required

## Tech Stack

- Java 25
- Java Swing (GUI and game loop via `javax.swing.Timer` at ~60 FPS)
- Maven (build and dependency management)
- MySQL + mysql-connector-j 8.2.0 (high score persistence)

## Prerequisites

- JDK 25+
- Maven 3.9+
- MySQL server running locally

## Getting Started

**1. Clone the repository:**
```bash
git clone https://github.com/your-username/yogi-bear.git
cd yogi-bear
```

**2. Set up the database config:**

Copy the example config and fill in your MySQL credentials:
```bash
cp src/main/resources/db.properties.example src/main/resources/db.properties
```

Edit `db.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/yogibear?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=your_mysql_username
db.password=your_mysql_password
```

The database and table are created automatically on first run.

**3. Build and run:**
```bash
mvn clean package
mvn exec:java
```

## Controls

| Key | Action |
|-----|--------|
| W | Move up |
| A | Move left |
| S | Move down |
| D | Move right |

## Project Structure

```
src/
├── main/
│   ├── java/com/mycompany/yogibear/
│   │   ├── YogiBear.java      # Entry point
│   │   ├── GameGUI.java       # Window and menu bar
│   │   ├── GameEngine.java    # Game loop, rendering, input
│   │   ├── Level.java         # Level loading and entity management
│   │   ├── Sprite.java        # Base class for all game objects
│   │   ├── Yogi.java          # Player character
│   │   ├── Ranger.java        # Enemy AI
│   │   ├── Basket.java        # Collectible item
│   │   ├── Obstacle.java      # Trees and rocks
│   │   └── Database.java      # MySQL high score persistence
│   └── resources/
│       ├── levels/            # Level map text files (level01.txt – level10.txt)
│       ├── images/            # Sprite images (optional, fallbacks built-in)
│       ├── db.properties      # Your local DB credentials (gitignored)
│       └── db.properties.example
```

## Status

Core game complete. Potential future improvements:
- Unit tests for game logic (Ranger AI, collision detection, scoring)
- Animated sprites
- Sound effects

## License

MIT
