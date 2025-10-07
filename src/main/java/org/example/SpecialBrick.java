package org.example;

import java.awt.Rectangle;


public class SpecialBrick extends Rectangle {
    // A brick can have one type of power-up, or null if it's a normal brick.
    public final PowerUpType powerUpType;

    public SpecialBrick(int x, int y, int width, int height, PowerUpType powerUpType) {
        super(x, y, width, height);
        this.powerUpType = powerUpType;
    }
}
