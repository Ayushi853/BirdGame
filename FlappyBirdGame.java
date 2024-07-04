import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBirdGame extends JFrame implements ActionListener, KeyListener {

    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int DELAY = 20; // Milliseconds between each frame update

    private Timer timer;
    private boolean isGameOn;
    private boolean isGameStart;
    private int birdY;
    private int birdVelocity;
    private ArrayList<Rectangle> hurdles;
    private int score;

    public FlappyBirdGame() {
        setTitle("Flappy Bird Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        birdY = HEIGHT / 2;
        birdVelocity = 0;
        hurdles = new ArrayList<>();
        score = 0;
        isGameOn = false; // Game starts paused
        isGameStart = false;

        addKeyListener(this);

        timer = new Timer(DELAY, this);
        setVisible(true);
    }

    public void paint(Graphics g) {
        super.paint(g);

        // Background
        g.setColor(new Color(135, 206, 250)); // Light blue
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Hurdles
        g.setColor(new Color(34, 139, 34)); // Forest green
        for (Rectangle hurdle : hurdles) {
            g.fillRect(hurdle.x, hurdle.y, hurdle.width, hurdle.height);
        }

        // Bird
        g.setColor(new Color(255, 69, 0)); // Orange Red
        g.fillRect(100, birdY, 40, 40);

        // Score
        if (isGameStart) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Score: " + score, 20, 30);
        }

        // Game start message
        if (!isGameStart) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Press Space to Start", WIDTH / 2 - 190, HEIGHT / 2 - 20);
        }

        // Game over message
        if (!isGameOn && isGameStart) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over!", WIDTH / 2 - 100, HEIGHT / 2 - 20);
            g.drawString("Final Score: " + score, WIDTH / 2 - 120, HEIGHT / 2 + 40);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isGameOn) {
            moveBird();
            moveHurdles();
            checkCollision();
            repaint();
        }
    }

    private void moveBird() {
        birdVelocity += 1; // Gravity effect
        birdY += birdVelocity;
        if (birdY > HEIGHT - 40) {
            birdY = HEIGHT - 40;
            gameOver();
        }
    }

    private void moveHurdles() {
        for (int i = 0; i < hurdles.size(); i++) {
            Rectangle hurdle = hurdles.get(i);
            hurdle.x -= 5; // Move hurdle to the left
            if (hurdle.x + hurdle.width <= 0) {
                hurdles.remove(hurdle);
                score++; // Increase score when hurdle passes
            }
        }
        if (hurdles.isEmpty() || hurdles.get(hurdles.size() - 1).x <= WIDTH - 300) {
            int hurdleHeight = 50 + new Random().nextInt(300); // Random height for hurdle
            hurdles.add(new Rectangle(WIDTH, HEIGHT - hurdleHeight, 50, hurdleHeight));
        }
    }

    private void checkCollision() {
        Rectangle bird = new Rectangle(100, birdY, 40, 40); // Bird bounds
        for (Rectangle hurdle : hurdles) {
            if (bird.intersects(hurdle)) {
                gameOver();
            }
        }
    }

    private void gameOver() {
        isGameOn = false;
        timer.stop();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!isGameStart && e.getKeyCode() == KeyEvent.VK_SPACE) {
            isGameStart = true;
            isGameOn = true; // Start the game
            timer.start();
        } else if (isGameOn && e.getKeyCode() == KeyEvent.VK_SPACE) {
            birdVelocity = -12; // Jump effect
        } else if (!isGameOn && e.getKeyCode() == KeyEvent.VK_ENTER) {
            restartGame();
        }
    }

    private void restartGame() {
        birdY = HEIGHT / 2;
        birdVelocity = 0;
        hurdles.clear();
        score = 0; // Reset score
        isGameOn = false;
        isGameStart = false; // Reset game start state
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        new FlappyBirdGame();
    }
}
