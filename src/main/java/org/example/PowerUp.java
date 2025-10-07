package org.example;

import java.awt.Image;
import java.awt.Rectangle;


public class PowerUp extends Rectangle {
    public final PowerUpType type;
    public final Image image;

    public PowerUp(int x, int y, PowerUpType type, Image image) {
        super(x, y, GameConstants.POWERUP_SIZE, GameConstants.POWERUP_SIZE);
        this.type = type;
        this.image = image;
    }

    public void move() {
        y += GameConstants.POWERUP_SPEED;
    }
}
