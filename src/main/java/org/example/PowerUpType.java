package org.example;

public enum PowerUpType {
    X2(2, "/2xBall.png"),
    X4(4, "/4xBall.png"),
    X8(8, "/8xBall.png"),
    X16(16, "/16xBall.png");

    public final int multiplier;
    public final String imagePath;

    PowerUpType(int multiplier, String imagePath) {
        this.multiplier = multiplier;
        this.imagePath = imagePath;
    }
}
