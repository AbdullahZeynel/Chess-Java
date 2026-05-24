#!/bin/bash
# Chess Engine Build & Run Script
# Usage: bash build_and_run.sh

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

echo "=== Chess Engine - Gradle Build ==="
echo ""

cd "$SCRIPT_DIR"
./gradlew run
