package org.example;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.ImageIcon;

/**
 * A utility class to handle loading image resources from the classpath.
 */
public class ImageLoader {

    /**
     * Loads a single image from the resources folder.
     * @param path The path to the image, relative to the resources root (e.g., "/ball.png").
     * @return The loaded Image object, or null if loading fails.
     */
    public static Image loadImage(String path) {
        try {
            return new ImageIcon(Objects.requireNonNull(ImageLoader.class.getResource(path))).getImage();
        } catch (Exception e) {
            System.err.println("Failed to load image: " + path);
            return null;
        }
    }

    /**
     * Loads all the ball images specifically.
     * @return An ArrayList of ball Image objects.
     */
    public static ArrayList<Image> loadBallImages() {
        ArrayList<Image> images = new ArrayList<>();
        String[] ballNames = {"/Ball.png", "/Cat_ball.png", "/Coin_ball.png", "/Moon_Ball.png", "/Moon_Cat_Ball.png"};
        for (String name : ballNames) {
            Image img = loadImage(name);
            if (img != null) {
                images.add(img);
            }
        }
        return images;
    }
}
