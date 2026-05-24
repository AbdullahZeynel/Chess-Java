# ♔ Gazi Chess Engine

A Java-based chess engine built with Swing, featuring a modern GUI, multiple game variants, and a clean architecture.

## Features

- **Standard Chess** — Full chess rules with castling, en passant, and pawn promotion
- **Three Checks** — First to deliver three checks wins
- **Chess Clock** — Configurable time controls (Blitz, Rapid, Classical)
- **FEN Support** — Board positions stored and loaded via FEN notation
- **Game Logging** — Automatic move history recording
- **Modern UI** — Dark theme with gradient backgrounds, rounded buttons, and smooth animations

## Project Structure

```
src/main/
├── java/
│   ├── Game/
│   │   ├── GameEngine/              # Core game logic
│   │   │   ├── ChessEngine.java         # Main engine (moves, validation, game state)
│   │   │   ├── CheckScanner.java        # Check and checkmate detection
│   │   │   ├── FEN.java                 # FEN string parser and generator
│   │   │   ├── Game.java                # Application entry point
│   │   │   └── User/                    # Player management
│   │   ├── Piece/                   # Chess pieces
│   │   │   ├── Piece.java               # Abstract base class
│   │   │   ├── King, Queen, Rook, Bishop, Knight, Pawn
│   │   │   └── PieceMoves.java          # Movement interface
│   │   └── Exceptions/              # Custom exceptions
│   ├── GUI/
│   │   ├── Board/                   # Board rendering
│   │   │   ├── Board.java               # Main board panel
│   │   │   ├── ChessClock.java          # Timer display
│   │   │   └── Menu/                    # Menu system (Frame, CreateNewGame)
│   │   ├── Input.java               # Mouse input handler
│   │   ├── Tile.java                # Tile rendering and highlights
│   │   └── Arrow.java               # Arrow drawing for analysis
│   └── resources/
│       ├── Variables.java            # Global constants and color palette
│       ├── Theme.java                # Theme abstraction
│       └── GaziTheme.java            # Default dark theme
├── kotlin/                          # Kotlin source files
└── resources/
    └── pieces.png                    # Chess piece sprites
```

## Getting Started

### Prerequisites
- Java JDK 21 or later

### Build & Run
```bash
./gradlew run
```

Or using the convenience script:
```bash
bash build_and_run.sh
```

### Build JAR
```bash
./gradlew jar
java -jar build/libs/chess-engine-6.2.1.jar
```

## Controls

| Action | Input |
|--------|-------|
| Select piece | Left click on piece |
| Move piece (click) | Click piece, then click target square |
| Move piece (drag) | Left click + drag |
| Switch selection | Click another friendly piece |
| Deselect | Click empty/invalid square |
| Highlight square | Right click |
| Draw arrow | Right click + drag |
| Clear highlights | Left click on empty |

## Technologies

- **Language:** Java + Kotlin
- **Build System:** Gradle (Kotlin DSL)
- **GUI Framework:** Java Swing
- **Architecture:** MVC-inspired with encapsulated game engine

## Authors

Built by Abdullah and Kerem as a university project at Gazi University.

## License

This project is for educational purposes.
