import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 700;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_HEIGHT*SCREEN_WIDTH)/UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int BodyLength = 6;
    int miceDevoured;
    int mouseX;
    int mouseY;
    String direction = "R";
    Boolean running = false;
    Timer timer;
    Random random;




    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newMice();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
        System.out.println("Made it 3");
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if(running) {
            // Create Gridlines
            g.setColor(Color.gray);
            for(int i = 0; i<SCREEN_HEIGHT/UNIT_SIZE; i++) {
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }
            
            // Create mouse
            g.fillOval(mouseX, mouseY, UNIT_SIZE, UNIT_SIZE);

            // Draw snake body
            for (int i = 0; i < BodyLength; i++) {
                if(i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(Color.orange);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            // Draw Score
            g.setColor(Color.red);
            g.setFont(new Font("Comic Sans", Font.BOLD, 50));
            FontMetrics metric = getFontMetrics(g.getFont());
            g.drawString("Score: " + miceDevoured, (SCREEN_WIDTH- metric.stringWidth("Score: " + miceDevoured))/2, g.getFont().getSize());
        } else {
            // end Game if running is false
            gameOver(g);
        }
    }

    // Method creates a new mouse in a random location on game Frame
    public void newMice() {
        mouseX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        mouseY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
    }

    // method draws snake moving forward and allows user to change directions
    public void move() {
        for(int i = BodyLength; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        // Switch to draw the next Body part in the correct direction
        switch(direction) {
            case("U"):
              y[0] = y[0] - UNIT_SIZE;
              break;
            case("D"):
              y[0] = y[0] + UNIT_SIZE;
              break;
            case("L"):
              x[0] = x[0] - UNIT_SIZE;
              break;
            case("R"):
              x[0] = x[0] + UNIT_SIZE;
              break;
        }

    }
    
    // Checks for head of snake colliding with mouse... game logic increment body and score
    public void checkMouse() {
        if ((x[0]==mouseX) && (y[0]==mouseY)) {
            miceDevoured++;
            BodyLength++;
            newMice();
        }

    }

    // Check if snake eats itself or runs out of bounds
    public void checkCollisions() {
        for (int i = BodyLength; i > 0; i--) {
            if ((x[0]==x[i]) && (y[0]==y[i])) {
                running = false;
            }
        }
        if (x[0] < 0) {
            running = false;
        } else if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        if (y[0] < 0) {
            running = false;
        } else if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (running == false) {
            timer.stop();
        }
    }

    // cAll to display final text
    public void gameOver(Graphics g) {
        // Display score
        g.setColor(Color.red);
        g.setFont(new Font("Comic Sans", Font.BOLD, 50));
        FontMetrics metric = getFontMetrics(g.getFont());
        g.drawString("Score: " + miceDevoured, (SCREEN_WIDTH- metric.stringWidth("Score: " + miceDevoured))/2, g.getFont().getSize());

        // Game over message
        g.setColor(Color.red);
        g.setFont(new Font("Comic Sans", Font.BOLD, 50));
        metric = getFontMetrics(g.getFont());
        g.drawString("Game Over bud :(", (SCREEN_WIDTH - metric.stringWidth("Game Over bud :("))/2, SCREEN_HEIGHT/2);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkMouse();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent key) {
            switch (key.getKeyCode()) {
                case (KeyEvent.VK_LEFT):
                    if(direction != "R") {
                        direction = "L";
                    }
                    break;
                case (KeyEvent.VK_RIGHT):
                    if(direction != "L") {
                        direction = "R";
                    }
                    break;
                case (KeyEvent.VK_UP):
                    if(direction != "D") {
                        direction = "U";
                    }
                    break;
                case (KeyEvent.VK_DOWN):
                    if(direction != "U") {
                        direction = "D";
                    }
                    break;
            }
        }
    }

}
