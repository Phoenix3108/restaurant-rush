#!/bin/sh
# Compile everything in src/ into out/ and start the game.
set -e
rm -rf out
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out restaurant.Main
