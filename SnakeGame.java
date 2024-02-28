import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private static final int UNIT_SIZE = 20;
    private static final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 100;

    private final int[] snakeX = new int[GAME_UNITS];
    private final int[] snakeY = new int[GAME_UNITS];
    private int snakeLength = 1;

    private int foodX;
    private int foodY;

    private char direction = 'R';
    private boolean isRunning = false;

    private final Random random;

    private Image snakeHeadImage;

    public SnakeGame() {
        random = new Random();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

        ImageIcon snakeHeadIcon = new ImageIcon("C:\\IntelliJ\\Projects\\untitled\\src\\snake_head.png"); // Dostosuj nazwę pliku obrazu
        snakeHeadImage = snakeHeadIcon.getImage();


        startGame();
    }

    public void startGame() {
        isRunning = true;
        newFood();
        snakeX[0] = WIDTH / 2;
        snakeY[0] = HEIGHT / 2;
        javax.swing.Timer timer = new javax.swing.Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (isRunning) {

            g.drawImage(snakeHeadImage, snakeX[0], snakeY[0], this);

            for (int i = 0; i < snakeLength; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(Color.yellow);
                    g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            g.setColor(Color.red);
            g.fillRect(foodX, foodY, UNIT_SIZE, UNIT_SIZE);
        } else {
            gameOver(g);
        }
    }

    public void newFood() {
        foodX = random.nextInt((int) (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        foodY = random.nextInt((int) (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for (int i = snakeLength; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        switch (direction) {
            case 'U':
                snakeY[0] -= UNIT_SIZE;
                break;
            case 'D':
                snakeY[0] += UNIT_SIZE;
                break;
            case 'L':
                snakeX[0] -= UNIT_SIZE;
                break;
            case 'R':
                snakeX[0] += UNIT_SIZE;
                break;
        }
    }

    public void checkFoodCollision() {
        if (snakeX[0] == foodX && snakeY[0] == foodY) {
            snakeLength++;
            newFood();
        }
    }

    public void checkCollision() {
        for (int i = snakeLength; i > 0; i--) {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                isRunning = false;
            }
        }

        if (snakeX[0] < 0 || snakeX[0] >= WIDTH || snakeY[0] < 0 || snakeY[0] >= HEIGHT) {
            isRunning = false;
        }

        if (!isRunning) {
            // Wywołaj metodę gameOver z obiektem Graphics
            gameOver(getGraphics());
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Helvetica", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String message = "Game Over";
        g.drawString(message, (WIDTH - metrics.stringWidth(message)) / 2, HEIGHT / 2);

        g.setColor(Color.white);
        g.setFont(new Font("Helvetica", Font.PLAIN, 20));
        String restartMessage = "Press 'R' to Restart";
        g.drawString(restartMessage, (WIDTH - metrics.stringWidth(restartMessage)) / 2, HEIGHT / 2 + 40);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (isRunning) {
            move();
            checkFoodCollision();
            checkCollision();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT && direction != 'R') {
            direction = 'L';
        } else if (key == KeyEvent.VK_RIGHT && direction != 'L') {
            direction = 'R';
        } else if (key == KeyEvent.VK_UP && direction != 'D') {
            direction = 'U';
        } else if (key == KeyEvent.VK_DOWN && direction != 'U') {
            direction = 'D';
        } else if (key == KeyEvent.VK_R && !isRunning) {
            restartGame();
        }
    }

    private void restartGame() {
        isRunning = true;
        snakeLength = 1;
        direction = 'R';
        newFood();
        snakeX[0] = WIDTH / 2;
        snakeY[0] = HEIGHT / 2;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}