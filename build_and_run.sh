#!/bin/bash
# Chess-Java Build & Run Script
# Usage: bash build_and_run.sh

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
SRC_DIR="$SCRIPT_DIR/chess_engine/src"
BUILD_DIR="$SCRIPT_DIR/build"

echo "=== Gazi Chess Engine - Build Script ==="
echo ""

# Clean build directory
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"

# Copy resources
echo "[1/3] Copying resources..."
mkdir -p "$BUILD_DIR/resources"
cp "$SRC_DIR/resources/pieces.png" "$BUILD_DIR/resources/"

# Compile all Java files
echo "[2/3] Compiling Java sources..."
javac -d "$BUILD_DIR" \
    -sourcepath "$SRC_DIR" \
    "$SRC_DIR/Game/GameEngine/Game.java" \
    "$SRC_DIR/Game/GameEngine/ThreeChecksChess.java" \
    "$SRC_DIR/Game/GameEngine/User/AI_User.java" \
    "$SRC_DIR/Game/GameEngine/User/ServerUser.java" \
    "$SRC_DIR/Game/Piece/ThreeChecksKing.java" \
    "$SRC_DIR/GUI/Board/Menu/CreateGameButtonFunction.java" \
    2>&1

if [ $? -ne 0 ]; then
    echo ""
    echo "!!! Compilation failed. See errors above."
    exit 1
fi

echo "    Compilation successful!"
echo "[3/3] Starting Gazi Chess Engine..."
echo ""

# Run from build directory so relative paths work
cd "$BUILD_DIR"
java -cp "$BUILD_DIR" Game.GameEngine.Game
