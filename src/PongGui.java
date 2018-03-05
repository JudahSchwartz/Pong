import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PongGui extends JFrame {
    //HELLO WORLD GIT 
    public PongGui(){

        setTitle("PONG");
        setSize(1500,1000);
        PongPanel pongPanel = new PongPanel();
        add(pongPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        pongPanel.startBall();
        pongPanel.Paddle(Direction.LEFT);
        pongPanel.Paddle(Direction.RIGHT);
        pongPanel.requestFocus();

    }
}
enum Direction{
    RIGHT,LEFT
        }
class PongPanel extends JPanel //implements Runnable
{
    Set<Character> keysPressed = new ConcurrentHashMap<Character, Object>().newKeySet();
    int lPos = 450;
    int rPos = 450;
    int paddleSpeed = 4;

    PongPanel() {
        System.out.println();
        setBackground(Color.BLACK);
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getPreciseWheelRotation() > 0 && lPos >= 10 + paddleSpeed)
                    lPos -= paddleSpeed;
                if (e.getPreciseWheelRotation() < 0 && lPos <= 710 - paddleSpeed) {
                    lPos += paddleSpeed;
                }
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keysPressed.add(e.getKeyChar());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keysPressed.remove(e.getKeyChar());
            }
        });
    }

    public void changePosValues() {
//        if(keysPressed.contains('w'))
//        {
//
//            if(lPos >=14)
//                lPos-=paddleSpeed;
//        }
//        if(keysPressed.contains('s'))
//        {
//          if(lPos <= 706)
//              lPos+=paddleSpeed;
//        }
        if (keysPressed.contains('i')) {
            if (rPos >= 10 + paddleSpeed)
                rPos -= paddleSpeed;
        }
        if (keysPressed.contains('k')) {
            if (rPos <= 710 - paddleSpeed)
                rPos += paddleSpeed;
        }
    }

    public void Paddle(Direction d) {
        Graphics2D g = (Graphics2D) getGraphics();

        Timer timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                changePosValues();

                g.setColor(Color.black);
                switch (d) {
                    case LEFT:
                        g.fillRect(100, 10, 50, 900);
                        g.setColor(Color.white);
                        g.fillRoundRect(100, lPos, 50, 200, 10, 10);
                        break;
                    case RIGHT:
                        g.fillRect(1300, 10, 50, 900);
                        g.setColor(Color.white);
                        g.fillRoundRect(1300, rPos, 50, 200, 10, 10);
                        // System.out.println("hello from right timer");
                        break;
                }
            }
        });
        timer.start();
    }

    public void startBall() {
        Graphics2D g = (Graphics2D) getGraphics();
        Timer timer = new Timer(40, new ActionListener() {
            int x = 200, y = 500;
            int dx = 10, dy = 10;
            int ballRadius = 25;

            @Override
            public void actionPerformed(ActionEvent e) {

                g.setColor(Color.black);
                g.fillOval(x, y, 2 * ballRadius, 2 * ballRadius);
                g.setColor(Color.white);
                x += dx;
                y += dy;
                g.fillOval(x, y, 2 * ballRadius, 2 * ballRadius);
                g.drawRect(10, 9, 1450, 902);
                //keep ball bouncing in frame
                if (y <= 10 || y >= 900
                        - 2 * ballRadius)//to accomodate for the height of the ball
                {

                    dy *= -1;
                }
                if (x == 150) {
                    if ((y + ballRadius) - lPos < 225
                            && (y + ballRadius) - lPos > -ballRadius)//ball is not within range of the left paddle
                    {
                        dx *= -1;
                    }
                }

                if (x == 1250) {
                    if ((y + ballRadius) - rPos < 225
                            && (y + ballRadius) - rPos > -ballRadius)//ball is not within range of the right paddle
                    {
                        dx *= -1;
                    }
                }
                if (x < - (2 * ballRadius) || x > 1500)//the ball went out of bounds
                {
                    JOptionPane.showMessageDialog(PongPanel.this, "Game Over");
                    System.exit(0);
                }
            }
        });

        timer.start();
    }
}