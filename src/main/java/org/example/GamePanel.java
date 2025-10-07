package org.example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * This class represents the panel where the game animation happens.
 * It handles drawing, game logic, user input, and state management.
 */
public class GamePanel extends JPanel {
    // Game state
    private GameState gameState;
    private int score;
    private int level;
    private int lives;
    private int lastScore;
    private Timer timer;
    private final Random random;

    // Ball properties
    private int ballX, ballY;
    private int ballDx, ballDy;

    // Paddle properties
    private int paddleX;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    // Obstacles
    private ArrayList<Rectangle> bricks;

    // Menu states
    private int selectedMenuItem;
    private final String[] menuItems = {"Play", "About", "Exit"};
    private int selectedPauseItem;
    private final String[] pauseMenuItems = {"Resume", "Restart Level", "Main Menu"};

    // Image assets
    private ArrayList<Image> ballImages;
    private Image currentBallImage;
    private Image brickImage;
    private Image heartImage;
    private Image gameOverImage;

    // Ball selection
    private int selectedBallIndex;

    /**
     * Constructor for the GamePanel.
     * Sets up the panel, loads resources, and starts the game loop.
     */
    public GamePanel() {
        setPreferredSize(new Dimension(GameConstants.PANEL_WIDTH, GameConstants.PANEL_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        random = new Random();

        ballImages = new ArrayList<>();
        loadImages();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyRelease(e);
            }
        });

        gameState = GameState.MENU;
        lastScore = 0;
        selectedMenuItem = 0;
        selectedPauseItem = 0;
        selectedBallIndex = 0;

        timer = new Timer(10, e -> gameLoop());
        timer.start();
    }

    private void loadImages() {
        ballImages = ImageLoader.loadBallImages();
        currentBallImage = ballImages.isEmpty() ? null : ballImages.get(0);
        brickImage = ImageLoader.loadImage("/brick.png");
        heartImage = ImageLoader.loadImage("/heart.png");
        gameOverImage = ImageLoader.loadImage("/gameover.png");
    }

    private void startGame() {
        score = 0;
        level = 1;
        lives = 3;
        resetBallAndPaddle();

        ballDx = (random.nextBoolean() ? 1 : -1) * GameConstants.INITIAL_BALL_SPEED;
        ballDy = -GameConstants.INITIAL_BALL_SPEED;

        bricks = LevelGenerator.generateLevel(level, random);

        gameState = GameState.PLAYING;
        requestFocusInWindow();
    }

    private void resetBallAndPaddle() {
        ballX = GameConstants.PANEL_WIDTH / 2 - GameConstants.BALL_DIAMETER / 2;
        ballY = GameConstants.PANEL_HEIGHT - GameConstants.PADDLE_HEIGHT - 100;
        paddleX = GameConstants.PANEL_WIDTH / 2 - GameConstants.PADDLE_WIDTH / 2;
    }

    private void gameLoop() {
        if (gameState == GameState.PLAYING) {
            movePaddle();
            moveBall();
            checkCollisions();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        switch (gameState) {
            case MENU:
                drawMenu(g);
                break;
            case BALL_SELECTION:
                drawBallSelection(g);
                break;
            case ABOUT:
                drawAbout(g);
                break;
            case PLAYING:
            case PAUSED:
            case GAME_OVER:
                drawGame(g);
                if (gameState == GameState.PAUSED) {
                    drawPauseMenu(g);
                } else if (gameState == GameState.GAME_OVER) {
                    drawGameOver(g);
                }
                break;
        }
    }

    private void drawGame(Graphics g) {
        // Draw paddle
        g.setColor(Color.BLUE);
        g.fillRect(paddleX, GameConstants.PANEL_HEIGHT - GameConstants.PADDLE_HEIGHT - 30, GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT);

        // Draw ball
        if (currentBallImage != null) {
            g.drawImage(currentBallImage, ballX, ballY, GameConstants.BALL_DIAMETER, GameConstants.BALL_DIAMETER, this);
        } else {
            g.setColor(Color.RED);
            g.fillOval(ballX, ballY, GameConstants.BALL_DIAMETER, GameConstants.BALL_DIAMETER);
        }

        // Draw bricks
        for (Rectangle brick : bricks) {
            if (brickImage != null) {
                g.drawImage(brickImage, brick.x, brick.y, brick.width, brick.height, this);
            } else {
                g.setColor(Color.GREEN);
                g.fillRect(brick.x, brick.y, brick.width, brick.height);
            }
        }

        // Draw HUD
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 25);

        if (heartImage != null) {
            int heartsWidth = lives * 35;
            int startX = (GameConstants.PANEL_WIDTH - heartsWidth) / 2;
            for (int i = 0; i < lives; i++) {
                g.drawImage(heartImage, startX + (i * 35), 5, 30, 30, this);
            }
        } else {
            String livesText = "Lives: " + lives;
            FontMetrics metrics = g.getFontMetrics();
            g.drawString(livesText, (GameConstants.PANEL_WIDTH - metrics.stringWidth(livesText)) / 2, 25);
        }

        String levelText = "Level: " + level;
        FontMetrics metrics = g.getFontMetrics();
        g.drawString(levelText, GameConstants.PANEL_WIDTH - metrics.stringWidth(levelText) - 10, 25);
    }

    private void drawMenu(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 70));
        FontMetrics metrics = g.getFontMetrics();
        String title = "Bouncing Ball";
        g.drawString(title, (GameConstants.PANEL_WIDTH - metrics.stringWidth(title)) / 2, 150);

        g.setFont(new Font("Arial", Font.BOLD, 30));
        metrics = g.getFontMetrics();
        String lastScoreText = "Last Score: " + lastScore;
        g.drawString(lastScoreText, (GameConstants.PANEL_WIDTH - metrics.stringWidth(lastScoreText)) / 2, 220);

        g.setFont(new Font("Arial", Font.PLAIN, 28));
        for (int i = 0; i < menuItems.length; i++) {
            g.setColor(i == selectedMenuItem ? Color.YELLOW : Color.WHITE);
            metrics = g.getFontMetrics();
            g.drawString(menuItems[i], (GameConstants.PANEL_WIDTH - metrics.stringWidth(menuItems[i])) / 2, 320 + i * 50);
        }
    }

    private void drawBallSelection(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        FontMetrics metrics = g.getFontMetrics();
        String title = "Choose Your Ball";
        g.drawString(title, (GameConstants.PANEL_WIDTH - metrics.stringWidth(title)) / 2, 100);

        int ballSize = 60;
        int spacing = 40;
        int totalWidth = (ballImages.size() * ballSize) + ((ballImages.size() - 1) * spacing);
        int startX = (GameConstants.PANEL_WIDTH - totalWidth) / 2;
        int yPos = GameConstants.PANEL_HEIGHT / 2 - ballSize / 2;

        for (int i = 0; i < ballImages.size(); i++) {
            int xPos = startX + i * (ballSize + spacing);
            if (i == selectedBallIndex) {
                g.setColor(Color.YELLOW);
                g.drawRect(xPos - 5, yPos - 5, ballSize + 10, ballSize + 10);
            }
            g.drawImage(ballImages.get(i), xPos, yPos, ballSize, ballSize, this);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        metrics = g.getFontMetrics();
        String instruction = "Use Left/Right Arrows to Select, Enter to Confirm";
        g.drawString(instruction, (GameConstants.PANEL_WIDTH - metrics.stringWidth(instruction)) / 2, yPos + ballSize + 80);
    }

    private void drawAbout(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        FontMetrics metrics = g.getFontMetrics();
        String title = "About";
        g.drawString(title, (GameConstants.PANEL_WIDTH - metrics.stringWidth(title)) / 2, 100);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        metrics = g.getFontMetrics();
        String line1 = "A simple brick breaker game created using Java Swing.";
        String line2 = "Use Arrow Keys or A/D to move the paddle.";
        String line3 = "Press 'P' during the game to pause.";
        String line4 = "Press Enter or ESC to return to the menu from other screens.";
        g.drawString(line1, (GameConstants.PANEL_WIDTH - metrics.stringWidth(line1)) / 2, 200);
        g.drawString(line2, (GameConstants.PANEL_WIDTH - metrics.stringWidth(line2)) / 2, 240);
        g.drawString(line3, (GameConstants.PANEL_WIDTH - metrics.stringWidth(line3)) / 2, 280);
        g.drawString(line4, (GameConstants.PANEL_WIDTH - metrics.stringWidth(line4)) / 2, 320);
    }

    private void drawGameOver(Graphics g) {
        if (gameOverImage != null) {
            int imgWidth = 400;
            int imgHeight = 200;
            g.drawImage(gameOverImage, (GameConstants.PANEL_WIDTH - imgWidth) / 2, (GameConstants.PANEL_HEIGHT / 2) - 150, imgWidth, imgHeight, this);
        } else {
            String gameOverMsg = "Game Over";
            Font font = new Font("Arial", Font.BOLD, 70);
            FontMetrics metrics = g.getFontMetrics(font);
            g.setColor(Color.YELLOW);
            g.setFont(font);
            g.drawString(gameOverMsg, (GameConstants.PANEL_WIDTH - metrics.stringWidth(gameOverMsg)) / 2, GameConstants.PANEL_HEIGHT / 2 - 50);
        }

        String restartMsg = "Press Enter or ESC to Return to Menu";
        Font font = new Font("Arial", Font.PLAIN, 25);
        FontMetrics metrics = g.getFontMetrics(font);
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(restartMsg, (GameConstants.PANEL_WIDTH - metrics.stringWidth(restartMsg)) / 2, GameConstants.PANEL_HEIGHT / 2 + 50);
    }

    private void drawPauseMenu(Graphics g) {
        // Draw a semi-transparent overlay to dim the background
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GameConstants.PANEL_WIDTH, GameConstants.PANEL_HEIGHT);

        // Draw the menu background box
        int boxWidth = 300;
        int boxHeight = 250;
        int x = (GameConstants.PANEL_WIDTH - boxWidth) / 2;
        int y = (GameConstants.PANEL_HEIGHT - boxHeight) / 2;
        g.setColor(new Color(40, 40, 40));
        g.fillRect(x, y, boxWidth, boxHeight);

        // Draw the "Paused" title
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        FontMetrics metrics = g.getFontMetrics();
        String pauseMsg = "Paused";
        g.drawString(pauseMsg, (GameConstants.PANEL_WIDTH - metrics.stringWidth(pauseMsg)) / 2, y + 60);

        // Draw the pause menu options
        g.setFont(new Font("Arial", Font.PLAIN, 28));
        for (int i = 0; i < pauseMenuItems.length; i++) {
            g.setColor(i == selectedPauseItem ? Color.YELLOW : Color.WHITE);
            metrics = g.getFontMetrics();
            g.drawString(pauseMenuItems[i], (GameConstants.PANEL_WIDTH - metrics.stringWidth(pauseMenuItems[i])) / 2, y + 120 + i * 40);
        }
    }

    private void moveBall() {
        ballX += ballDx;
        ballY += ballDy;

        if (ballX <= 0 || ballX >= getWidth() - GameConstants.BALL_DIAMETER) {
            ballDx = -ballDx;
        }
        if (ballY <= 0) {
            ballDy = -ballDy;
        }
        if (ballY >= getHeight()) {
            lives--;
            if (lives <= 0) {
                gameState = GameState.GAME_OVER;
                lastScore = score;
            } else {
                resetBallAndPaddle();
            }
        }
    }

    private void movePaddle() {
        if (leftPressed && paddleX > 0) {
            paddleX -= GameConstants.PADDLE_SPEED;
        }
        if (rightPressed && paddleX < getWidth() - GameConstants.PADDLE_WIDTH) {
            paddleX += GameConstants.PADDLE_SPEED;
        }
    }

    private void checkCollisions() {
        int paddleY = GameConstants.PANEL_HEIGHT - GameConstants.PADDLE_HEIGHT - 30;
        Rectangle ballRect = new Rectangle(ballX, ballY, GameConstants.BALL_DIAMETER, GameConstants.BALL_DIAMETER);
        Rectangle paddleRect = new Rectangle(paddleX, paddleY, GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT);

        if (ballRect.intersects(paddleRect)) {
            ballDy = -Math.abs(ballDy);
        }

        for (int i = 0; i < bricks.size(); i++) {
            if (ballRect.intersects(bricks.get(i))) {
                bricks.remove(i);
                ballDy = -ballDy;
                score += 10;
                break;
            }
        }

        if (bricks.isEmpty()) {
            level++;
            if (level > 20 && level % 3 == 0) {
                if (Math.abs(ballDx) < GameConstants.MAX_BALL_SPEED) {
                    ballDx += (ballDx > 0 ? 1 : -1);
                    ballDy += (ballDy > 0 ? 1 : -1);
                }
            }
            bricks = LevelGenerator.generateLevel(level, random);
            resetBallAndPaddle();
        }
    }

    private void handleKeyPress(KeyEvent e) {
        int key = e.getKeyCode();
        switch (gameState) {
            case MENU:
                handleMenuKeyPress(key);
                break;
            case BALL_SELECTION:
                handleBallSelectionKeyPress(key);
                break;
            case PLAYING:
                handlePlayingKeyPress(key);
                if (key == KeyEvent.VK_P) {
                    selectedPauseItem = 0; // Reset selection to "Resume"
                    gameState = GameState.PAUSED;
                }
                break;
            case PAUSED:
                handlePausedKeyPress(key);
                break;
            case GAME_OVER:
            case ABOUT:
                if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_ESCAPE) {
                    gameState = GameState.MENU;
                }
                break;
        }
    }

    private void handleMenuKeyPress(int key) {
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            selectedMenuItem = (selectedMenuItem - 1 + menuItems.length) % menuItems.length;
        }
        if (key == KeyEvent.VK_DOWN) {
            selectedMenuItem = (selectedMenuItem + 1) % menuItems.length;
        }
        if (key == KeyEvent.VK_ENTER) {
            switch (selectedMenuItem) {
                case 0: gameState = GameState.BALL_SELECTION; break;
                case 1: gameState = GameState.ABOUT; break;
                case 2: System.exit(0); break;
            }
        }
    }

    private void handleBallSelectionKeyPress(int key) {
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            selectedBallIndex = (selectedBallIndex - 1 + ballImages.size()) % ballImages.size();
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            selectedBallIndex = (selectedBallIndex + 1) % ballImages.size();
        }
        if (key == KeyEvent.VK_ENTER) {
            currentBallImage = ballImages.get(selectedBallIndex);
            startGame();
        }
        if (key == KeyEvent.VK_ESCAPE) {
            gameState = GameState.MENU;
        }
    }

    private void handlePausedKeyPress(int key) {
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            selectedPauseItem = (selectedPauseItem - 1 + pauseMenuItems.length) % pauseMenuItems.length;
        }
        if (key == KeyEvent.VK_DOWN) {
            selectedPauseItem = (selectedPauseItem + 1) % pauseMenuItems.length;
        }
        if (key == KeyEvent.VK_P) { // Allow 'P' to resume directly
            gameState = GameState.PLAYING;
        }
        if (key == KeyEvent.VK_ENTER) {
            switch (selectedPauseItem) {
                case 0: // Resume
                    gameState = GameState.PLAYING;
                    break;
                case 1: // Restart Level
                    lives--;
                    if (lives <= 0) {
                        gameState = GameState.GAME_OVER;
                        lastScore = score;
                    } else {
                        resetBallAndPaddle();
                        gameState = GameState.PLAYING;
                    }
                    break;
                case 2: // Main Menu
                    gameState = GameState.MENU;
                    break;
            }
        }
        if (key == KeyEvent.VK_ESCAPE) {
            gameState = GameState.PLAYING; // Also allow ESC to resume
        }
    }

    private void handlePlayingKeyPress(int key) {
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            rightPressed = true;
        }
    }

    private void handleKeyRelease(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            rightPressed = false;
        }
    }
}

