package org.example;

import java.awt.Rectangle;

/**
 * Represents a brick in the game that may or may not contain a power-up
 * and can have multiple hits before breaking.
 */
public class SpecialBrick extends Rectangle {
    public final PowerUpType powerUpType;
    private int health;

    /**
     * Constructor for a brick.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param width The width of the brick.
     * @param height The height of the brick.
     * @param powerUpType The type of power-up this brick contains (can be null).
     * @param health The number of hits this brick can take.
     */
    public SpecialBrick(int x, int y, int width, int height, PowerUpType powerUpType, int health) {
        super(x, y, width, height);
        this.powerUpType = powerUpType;
        this.health = Math.max(1, health); // Health must be at least 1
    }

    /**
     * This method is called when the ball hits the brick.
     * It reduces the brick's health and returns true if the brick is destroyed.
     * @return true if health is 0 or less, otherwise false.
     */
    public boolean hit() {
        this.health--;
        return this.health <= 0;
    }

    /**
     * Gets the current health of the brick.
     * @return The current health.
     */
    public int getHealth() {
        return health;
    }
}

