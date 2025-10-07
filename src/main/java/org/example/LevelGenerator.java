package org.example;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

/**
 * A utility class responsible for generating brick layouts for each level.
 */
public class LevelGenerator {

    /**
     * Generates a list of brick rectangles for a given level.
     * @param level The current game level.
     * @param random A Random object for procedural generation.
     * @return An ArrayList of Rectangles representing the bricks.
     */
    public static ArrayList<Rectangle> generateLevel(int level, Random random) {
        ArrayList<Rectangle> bricks = new ArrayList<>();
        // Total width of all bricks without any spacing, to center them correctly.
        int totalBricksWidth = (GameConstants.BRICK_COLS * GameConstants.BRICK_WIDTH);
        int startX = (GameConstants.PANEL_WIDTH - totalBricksWidth) / 2;

        if (level <= 10) {
            generateRandomLayout(bricks, startX, 0.4 + (level * 0.02), random);
        } else if (level <= 20) {
            generateRandomLayout(bricks, startX, 0.6 + ((level - 10) * 0.01), random);
        } else {
            int patternType = (level - 21) % 5;
            switch (patternType) {
                case 0: generateCheckerboardLayout(bricks, startX); break;
                case 1: generateSolidWithGapsLayout(bricks, startX, random); break;
                case 2: generatePyramidLayout(bricks, startX); break;
                case 3: generateVerticalLinesLayout(bricks, startX); break;
                default: generateRandomLayout(bricks, startX, 0.85, random); break;
            }
        }
        return bricks;
    }

    private static void generateRandomLayout(ArrayList<Rectangle> bricks, int startX, double density, Random random) {
        for (int row = 0; row < GameConstants.BRICK_ROWS; row++) {
            for (int col = 0; col < GameConstants.BRICK_COLS; col++) {
                if (random.nextDouble() < density) {
                    addBrick(bricks, startX, row, col);
                }
            }
        }
    }

    private static void generateCheckerboardLayout(ArrayList<Rectangle> bricks, int startX) {
        for (int row = 0; row < GameConstants.BRICK_ROWS; row++) {
            for (int col = 0; col < GameConstants.BRICK_COLS; col++) {
                if ((row + col) % 2 == 0) {
                    addBrick(bricks, startX, row, col);
                }
            }
        }
    }

    private static void generateSolidWithGapsLayout(ArrayList<Rectangle> bricks, int startX, Random random) {
        for (int row = 0; row < GameConstants.BRICK_ROWS; row++) {
            int gapCol = random.nextInt(GameConstants.BRICK_COLS - 2) + 1;
            for (int col = 0; col < GameConstants.BRICK_COLS; col++) {
                if (col != gapCol && col != gapCol + 1) {
                    addBrick(bricks, startX, row, col);
                }
            }
        }
    }

    private static void generatePyramidLayout(ArrayList<Rectangle> bricks, int startX) {
        int centerCol = GameConstants.BRICK_COLS / 2;
        for (int row = 0; row < GameConstants.BRICK_ROWS; row++) {
            for (int col = 0; col < GameConstants.BRICK_COLS; col++) {
                if (Math.abs(col - centerCol) <= row) {
                    addBrick(bricks, startX, row, col);
                }
            }
        }
    }

    private static void generateVerticalLinesLayout(ArrayList<Rectangle> bricks, int startX) {
        for (int row = 0; row < GameConstants.BRICK_ROWS; row++) {
            for (int col = 0; col < GameConstants.BRICK_COLS; col++) {
                if (col % 3 == 0) {
                    addBrick(bricks, startX, row, col);
                }
            }
        }
    }

    // The 'spacing' parameter has been removed to make bricks touch.
    private static void addBrick(ArrayList<Rectangle> bricks, int startX, int row, int col) {
        int brickX = startX + col * GameConstants.BRICK_WIDTH;
        int brickY = 50 + row * GameConstants.BRICK_HEIGHT; // The 50 is the top margin.
        bricks.add(new Rectangle(brickX, brickY, GameConstants.BRICK_WIDTH, GameConstants.BRICK_HEIGHT));
    }
}

