package org.example;

import javax.swing.*;





public class GameWindow extends JFrame {

    /**
     * Constructor for the GameWindow frame.
     * Sets up the window properties and adds the main GamePanel.
     */
    public GameWindow() {
        setTitle("Bouncing Ball Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Create and add the panel where all the game action happens.
        add(new GamePanel());

        pack(); // Sizes the frame to fit the preferred size of its subcomponents.
        setLocationRelativeTo(null); // Centers the window on the screen.
    }
}
