#!/usr/bin/env bash
# Builds and launches Guess The Number on Linux/macOS.
# Usage: ./run.sh                      (finds JavaFX in your home folder)
#        FX=/path/to/javafx-sdk/lib ./run.sh   (use a specific JavaFX)
set -e

# Always run from the project folder, wherever the script is called from
cd "$(dirname "$0")"

# Find JavaFX: use $FX if set, otherwise look for ~/javafx-sdk-*/lib
if [ -z "$FX" ]; then
  FX=$(ls -d "$HOME"/javafx-sdk-*/lib 2>/dev/null | sort -V | tail -n 1)
fi
if [ -z "$FX" ] || [ ! -d "$FX" ]; then
  echo "Couldn't find JavaFX."
  echo "Download the JavaFX SDK from https://gluonhq.com/products/javafx/ and unzip it into your home folder,"
  echo "or run: FX=/path/to/javafx-sdk/lib ./run.sh"
  exit 1
fi
echo "Using JavaFX at $FX"

MODULES="javafx.controls,javafx.fxml"

echo "Compiling..."
javac --module-path "$FX" --add-modules "$MODULES" -d bin \
  src/app/*.java src/app/misc/*.java src/app/network/*.java src/app/packets/*.java

echo "Copying screens, styles, fonts, and icons..."
cp -r src/app/guiConfigs src/app/fonts src/app/icons bin/app/

echo "Starting Guess The Number..."
java --module-path "$FX" --add-modules "$MODULES" -cp bin app.Gui
