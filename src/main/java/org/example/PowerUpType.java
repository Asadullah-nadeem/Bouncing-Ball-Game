package org.example;

public enum PowerUpType {
    X2(2, "/2xBall.png", false),
    X4(4, "/4xBall.png", false),
    X8(8, "/8xBall.png", false),
    X16(16, "/16xBall.png", false),
    HEART(0, "/heart.png", true);

    public final int multiplier;
    public final String imagePath;
    public final boolean isLifeUp;

    PowerUpType(int multiplier, String imagePath, boolean isLifeUp) {
        this.multiplier = multiplier;
        this.imagePath = imagePath;
        this.isLifeUp = isLifeUp;
    }
}
