package org.example;

import java.awt.Image;
import java.awt.Rectangle;

/**
 * Represents a falling power-up item on the screen.
 * It extends Rectangle for easy collision detection.
 */
public class PowerUp extends Rectangle {
    public final PowerUpType type;
    public final Image image;

    public PowerUp(int x, int y, PowerUpType type, Image image) {
        super(x, y, GameConstants.POWERUP_SIZE, GameConstants.POWERUP_SIZE);
        this.type = type;
        this.image = image;
    }

    /**
     * Moves the power-up down the screen.
     */
    public void move() {
        y += GameConstants.POWERUP_SPEED;
    }
}
