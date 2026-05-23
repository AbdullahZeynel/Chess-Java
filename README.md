# ♔ Gazi Chess Engine

A Java-based chess engine built with Swing, featuring a modern GUI, multiple game variants, and a clean architecture.

## Features

- **Standard Chess** — Full chess rules with castling, en passant, and pawn promotion
- **Three Checks** — First to deliver three checks wins
- **Merge Chess** — Experimental chess variant
- **Chess Clock** — Configurable time controls (Blitz, Rapid, Classical)
- **FEN Support** — Board positions stored and loaded via FEN notation
- **Game Logging** — Automatic move history recording
- **Modern UI** — Dark theme with gradient backgrounds, rounded buttons, and smooth animations

## Project Structure

```
chess_engine/src/
├── Game/
│   ├── GameEngine/              # Core game logic
│   │   ├── ChessEngine.java         # Main engine (moves, validation, game state)
│   │   ├── CheckScanner.java        # Check and checkmate detection
│   │   ├── FEN.java                 # FEN string parser and generator
│   │   ├── Game.java                # Application entry point
│   │   └── User/                    # Player management
│   ├── Piece/                   # Chess pieces
│   │   ├── Piece.java               # Abstract base class
│   │   ├── King, Queen, Rook, Bishop, Knight, Pawn
│   │   └── PieceMoves.java          # Movement interface
│   └── Exceptions/              # Custom exceptions
├── GUI/
│   ├── Board/                   # Board rendering
│   │   ├── Board.java               # Main board panel
│   │   ├── ChessClock.java          # Timer display
│   │   └── Menu/                    # Menu system (Frame, CreateNewGame)
│   ├── Input.java               # Mouse input handler
│   ├── Tile.java                # Tile rendering and highlights
│   └── Arrow.java               # Arrow drawing for analysis
└── resources/
    ├── Variables.java            # Global constants and color palette
    └── pieces.png                # Chess piece sprites
```

## Getting Started

### Prerequisites
- Java JDK 11 or later

### Build & Run
```bash
bash build_and_run.sh
```

Or manually:
```bash
# Compile
mkdir -p build
javac -d build -sourcepath chess_engine/src \
    chess_engine/src/Game/GameEngine/Game.java

# Copy resources
mkdir -p build/resources
cp chess_engine/src/resources/pieces.png build/resources/

# Run
cd build && java Game.GameEngine.Game
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

- **Language:** Java
- **GUI Framework:** Java Swing
- **Architecture:** MVC-inspired with encapsulated game engine

## Authors

Built by Abdullah and Kerem as a university project at Gazi University.

## License

This project is for educational purposes.
