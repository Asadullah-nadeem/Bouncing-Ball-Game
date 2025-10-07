package org.example;

import java.util.ArrayList;
import java.util.Random;

/**
 * A utility class responsible for creating the brick layouts for each level.
 * The levels get progressively harder and more varied.
 */
public class LevelGenerator {

    /**
     * The main method to generate a level's brick layout.
     * @param level The current game level.
     * @param currentLives The player's current number of lives.
     * @param random A Random object for generating patterns.
     * @return An ArrayList of SpecialBrick objects for the level.
     */
    public static ArrayList<SpecialBrick> generateLevel(int level, int currentLives, Random random) {
        ArrayList<SpecialBrick> bricks = new ArrayList<>();
        int startX = (GameConstants.PANEL_WIDTH - (GameConstants.BRICK_COLS * GameConstants.BRICK_WIDTH)) / 2;

        if (level <= 10) { // Easy levels with low density
            generateRandomLayout(bricks, startX, 0.4 + (level * 0.02), level, currentLives, random);
        } else if (level <= 20) { // Medium levels with higher density
            generateRandomLayout(bricks, startX, 0.6 + ((level - 10) * 0.01), level, currentLives, random);
        } else { // Hard levels with patterns
            int patternType = (level - 21) % 5; // Cycle through 5 patterns for variety
            switch (patternType) {
                case 0:
                    generateCheckerboardLayout(bricks, startX, level, currentLives, random);
                    break;
                case 1:
                    generateSolidWithGapsLayout(bricks, startX, level, currentLives, random);
                    break;
                case 2:
                    generatePyramidLayout(bricks, startX, level, currentLives, random);
                    break;
                case 3:
                    generateVerticalLinesLayout(bricks, startX, level, currentLives, random);
                    break;
                default:
                    generateRandomLayout(bricks, startX, 0.85, level, currentLives, random);
                    break;
            }
        }
        return bricks;
    }

    private static void addBrick(ArrayList<SpecialBrick> bricks, int x, int y, int level, int currentLives, Random random) {
        PowerUpType powerUpType = determinePowerUpType(level, currentLives, random);
        int health = determineBrickHealth(level, random);
        bricks.add(new SpecialBrick(x, y, GameConstants.BRICK_WIDTH, GameConstants.BRICK_HEIGHT, powerUpType, health));
    }

    private static int determineBrickHealth(int level, Random random) {
        if (level < 5) return 1; // Always 1 health for the first 4 levels
        if (level <= 15) {
            // Chance for 2-health bricks
            return random.nextDouble() < 0.2 ? 2 : 1;
        } else {
            // Chance for 2-health and 3-health bricks in later levels
            double roll = random.nextDouble();
            if (roll < 0.1) return 3; // 10% chance for 3 health
            if (roll < 0.3) return 2; // 20% chance for 2 health
            return 1; // 70% chance for 1 health
        }
    }

    private static PowerUpType determinePowerUpType(int level, int currentLives, Random random) {
        // First, check for a heart drop if the player is not at max lives.
        if (currentLives < GameConstants.MAX_LIVES && random.nextDouble() < GameConstants.HEART_DROP_CHANCE) {
            return PowerUpType.HEART;
        }

        // If no heart drops, then check for a score multiplier.
        if (random.nextDouble() < 0.1) { // 10% chance for a score multiplier
            if (level > 10 && random.nextDouble() < 0.1) return PowerUpType.X16; // Very rare
            if (level > 8 && random.nextDouble() < 0.2) return PowerUpType.X8;
            if (level > 4 && random.nextDouble() < 0.3) return PowerUpType.X4;
            return PowerUpType.X2;
        }
        return null; // This is a normal brick
    }

    private static void generateRandomLayout(ArrayList<SpecialBrick> bricks, int startX, double density, int level, int currentLives, Random random) {
        for (int row = 0; row < GameConstants.BRICK_ROWS; row++) {
            for (int col = 0; col < GameConstants.BRICK_COLS; col++) {
                if (random.nextDouble() < density) {
                    addBrick(bricks, startX + col * GameConstants.BRICK_WIDTH, row * GameConstants.BRICK_HEIGHT + 50, level, currentLives, random);
                }
            }
        }
    }

    private static void generateCheckerboardLayout(ArrayList<SpecialBrick> bricks, int startX, int level, int currentLives, Random random) {
        for (int row = 0; row < GameConstants.BRICK_ROWS; row++) {
            for (int col = 0; col < GameConstants.BRICK_COLS; col++) {
                if ((row + col) % 2 == 0) {
                    addBrick(bricks, startX + col * GameConstants.BRICK_WIDTH, row * GameConstants.BRICK_HEIGHT + 50, level, currentLives, random);
                }
            }
        }
    }

    private static void generateSolidWithGapsLayout(ArrayList<SpecialBrick> bricks, int startX, int level, int currentLives, Random random) {
        for (int row = 0; row < GameConstants.BRICK_ROWS; row++) {
            int gapCol = random.nextInt(GameConstants.BRICK_COLS - 2) + 1;
            for (int col = 0; col < GameConstants.BRICK_COLS; col++) {
                if (col != gapCol && col != gapCol + 1) {
                    addBrick(bricks, startX + col * GameConstants.BRICK_WIDTH, row * GameConstants.BRICK_HEIGHT + 50, level, currentLives, random);
                }
            }
        }
    }

    private static void generatePyramidLayout(ArrayList<SpecialBrick> bricks, int startX, int level, int currentLives, Random random) {
        int centerCol = GameConstants.BRICK_COLS / 2;
        for (int row = 0; row < GameConstants.BRICK_ROWS; row++) {
            for (int col = 0; col < GameConstants.BRICK_COLS; col++) {
                if (Math.abs(col - centerCol) <= row) {
                    addBrick(bricks, startX + col * GameConstants.BRICK_WIDTH, row * GameConstants.BRICK_HEIGHT + 50, level, currentLives, random);
                }
            }
        }
    }

    private static void generateVerticalLinesLayout(ArrayList<SpecialBrick> bricks, int startX, int level, int currentLives, Random random) {
        for (int row = 0; row < GameConstants.BRICK_ROWS; row++) {
            for (int col = 0; col < GameConstants.BRICK_COLS; col++) {
                if (col % 3 == 0) {
                    addBrick(bricks, startX + col * GameConstants.BRICK_WIDTH, row * GameConstants.BRICK_HEIGHT + 50, level, currentLives, random);
                }
            }
        }
    }
}

