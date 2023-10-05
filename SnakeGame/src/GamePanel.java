import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 800;
    public static final int UNIT_SIZE = 20;
    public static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    public static final int DELAY = 80;
    public int x[] = new int[GAME_UNITS];
    public int y[] = new int[GAME_UNITS];
    public int bodyParts = 6;
    public int applesEaten;
    public int appleX;
    public int appleY;
    public int direction = 'R';
    public boolean running = false;
    public Timer timer;
    public Random random;

    GamePanel() {

        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.gray);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    // initiate game
    public void startGame() {

        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    // paint the panel
    public void paintComponent(Graphics graphics) {

        super.paintComponent(graphics);
        draw(graphics);
    }

    // draw the game
    public void draw(Graphics graphics) {

        if (running) {

            /* draw a grid in the panel
            for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {

                graphics.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                graphics.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }*/

            // set the apple position
            graphics.setColor(Color.red);
            graphics.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {

                if (i == 0) {

                    // draw the head of the snake
                    graphics.setColor(Color.orange);
                    graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);


                } else {

                    // draw the rest of the body of the snake in random colors
                    graphics.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            // show score in game
            graphics.setColor(Color.red);
            graphics.setFont(new Font("Ink Free", Font.BOLD, 50));
            FontMetrics fMetrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + applesEaten, (SCREEN_WIDTH - fMetrics.stringWidth("Score: " + applesEaten))/2, graphics.getFont().getSize());

        } else {

            gameOver(graphics);
        }
    }

    // set apple in random position
    public void newApple() {

        appleX = random.nextInt((SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    // allow snake to move by itself
    public void move() {

        for (int i = bodyParts; i > 0; i--) {

            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction) {
            case 'U':

                y[0] = y[0] - UNIT_SIZE;
                break;

            case 'D':

                y[0] = y[0] + UNIT_SIZE;
                break;

            case 'L':

                x[0] = x[0] - UNIT_SIZE;
                break;

            case 'R':

                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    // check if apple was eaten
    public void checkApple() {

        if ((x[0] == appleX) && (y[0] == appleY)) {

            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    // check collisions to game over
    public void checkCollisions() {

        // check if head collides with body
        for (int i = bodyParts; i > 0 ; i--) {

            if ((x[0] == x[i]) && (y[0] == y[i])) {

                running = false;
            }
        }

        // check if head touches left border
        if (x[0] < 0) {

            running = false;
        }

        // check if head touches right border
        if (x[0] > SCREEN_WIDTH) {

            running = false;
        }

        // check if head touches top border
        if (y[0] < 0) {

            running = false;
        }

        // check if head touches bottom border
        if (y[0] > SCREEN_HEIGHT) {

            running = false;
        }

        // stop game
        if (!running) {

            timer.stop();
        }
    }

    // show that the game is over
    public void gameOver(Graphics graphics) {

        // show score after game over
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 50));
        FontMetrics fMetrics2 = getFontMetrics(graphics.getFont());
        graphics.drawString("Your Score was: " + applesEaten, (SCREEN_WIDTH - fMetrics2.stringWidth("Your Score was: " + applesEaten))/2, graphics.getFont().getSize());

        // show game over text
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics fMetrics1 = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (SCREEN_WIDTH - fMetrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
    }

    // check which action is performed
    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {

            move();
            checkApple();
            checkCollisions();
        }

        repaint();
    }

    // allow movement with keyboard arrows
    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:

                    if (direction != 'R') {

                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:

                    if (direction != 'L') {

                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_UP:

                    if (direction != 'D') {

                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN:

                    if (direction != 'U') {

                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
