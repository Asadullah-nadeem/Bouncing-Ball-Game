package org.example;

public class GameConstants {
    // Panel and Window
    public static final int PANEL_WIDTH = 800;
    public static final int PANEL_HEIGHT = 600;

    // Paddle
    public static final int PADDLE_WIDTH = 120;
    public static final int PADDLE_HEIGHT = 15;
    public static final int PADDLE_SPEED = 10;

    // Ball
    public static final int BALL_DIAMETER = 25;
    public static final int INITIAL_BALL_SPEED = 4;
    public static final int MAX_BALL_SPEED = 8;

    // Bricks
    public static final int BRICK_WIDTH = 50;
    public static final int BRICK_HEIGHT = 25;
    public static final int BRICK_ROWS = 10;
    public static final int BRICK_COLS = 16;

    // Power-ups
    public static final int POWERUP_SIZE = 25;
    public static final int POWERUP_SPEED = 3;
    public static final long POWERUP_DURATION_MS = 10000; // 10 seconds
    public static final double HEART_DROP_CHANCE = 0.05; // 5% chance for a heart to drop

    // Game Rules
    public static final int MAX_LIVES = 3;
}

