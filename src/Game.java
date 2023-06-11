import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends JFrame implements KeyListener {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int SQUARE_SIZE = 50;
    private static final int ENEMY_SIZE = 25;
    private static final int ENEMY_SPEED = 3;

    private boolean isGameOver;
    private boolean isKeyPressed;
    private Rectangle square;
    private List<Enemy> enemies;

    private class Enemy {
        private int x;
        private int y;
        private int dx;
        private int dy;
        private Color color;

        public Enemy() {
            Random random = new Random();
            x = WINDOW_WIDTH;
            y = random.nextInt(WINDOW_HEIGHT - ENEMY_SIZE);
            dx = -ENEMY_SPEED;
            dy = random.nextInt(ENEMY_SPEED * 2 + 1) - ENEMY_SPEED;
            color = getRandomColor();
        }

        public void move() {
            x += dx;
            y += dy;

            if (x < -ENEMY_SIZE) {
                x = WINDOW_WIDTH;
                y = new Random().nextInt(WINDOW_HEIGHT - ENEMY_SIZE);
                dx = -ENEMY_SPEED;
                dy = new Random().nextInt(ENEMY_SPEED * 2 + 1) - ENEMY_SPEED;
                color = getRandomColor();
            }
        }

        public Color getRandomColor() {
            Random random = new Random();
            float r = random.nextFloat();
            float g = random.nextFloat();
            float b = random.nextFloat();
            return new Color(r, g, b);
        }

        public void draw(Graphics g) {
            g.setColor(color);
            g.fillOval(x, y, ENEMY_SIZE, ENEMY_SIZE);
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, ENEMY_SIZE, ENEMY_SIZE);
        }
    }

    public Game() {
        setTitle("Game");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        addKeyListener(this);
        setFocusable(true);
        isGameOver = false;
        isKeyPressed = false;
        square = new Rectangle(0, 0, SQUARE_SIZE, SQUARE_SIZE);
        enemies = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            enemies.add(new Enemy());
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        if (isGameOver) {
            g.setColor(Color.WHITE);
            g.drawString("GAME OVER", WINDOW_WIDTH / 2 - 40, WINDOW_HEIGHT / 2 - 10);
            g.drawString("Press 'Q' to Quit or 'R' to Restart", WINDOW_WIDTH / 2 - 90, WINDOW_HEIGHT / 2 + 10);
            return;
        }

        g.setColor(Color.WHITE);
        g.fillRect(square.x, square.y, SQUARE_SIZE, SQUARE_SIZE);

        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }
    }

    public void checkCollisions() {
        for (Enemy enemy : enemies) {
            if (square.intersects(enemy.getBounds())) {
                isGameOver = true;
                break;
            }
        }
    }

    public void moveSquare() {
        if (isKeyPressed) {
            if (square.x > 0) {
                square.x -= 5;
            }
            if (square.y > 0) {
                square.y -= 5;
            }
            if (square.x < WINDOW_WIDTH - SQUARE_SIZE) {
                square.x += 5;
            }
            if (square.y < WINDOW_HEIGHT - SQUARE_SIZE) {
                square.y += 5;
            }
        }
    }

    public void moveEnemies() {
        for (Enemy enemy : enemies) {
            enemy.move();
        }
    }

    public void gameLoop() {
        while (true) {
            if (isGameOver && isKeyPressed) {
                if (getKeyChar() == 'q' || getKeyChar() == 'Q') {
                    System.exit(0);
                } else if (getKeyChar() == 'r' || getKeyChar() == 'R') {
                    isGameOver = false;
                    isKeyPressed = false;
                    square.setLocation(0, 0);
                    enemies.clear();
                    for (int i = 0; i < 5; i++) {
                        enemies.add(new Enemy());
                    }
                }
            }

            moveSquare();
            moveEnemies();
            checkCollisions();
            repaint();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private char getKeyChar() {
        return 0;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        isKeyPressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        isKeyPressed = false;
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.setVisible(true);
        game.gameLoop();
    }
}
