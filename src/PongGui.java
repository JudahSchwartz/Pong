import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PongGui extends JFrame {
    //HELLO WORLD GIT 
    public PongGui(){
        super();
        setTitle("PONG");
        setSize(1500,1000);
        PongPanel pongPanel = new PongPanel();
        add(pongPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        pongPanel.setUp();
        pongPanel.startBall();
        pongPanel.Paddle(Direction.LEFT);
        pongPanel.Paddle(Direction.RIGHT);

    }
}
enum Direction{
    RIGHT,LEFT
        }
class PongPanel extends JPanel //implements Runnable
{
    Graphics2D g;
    Set<Character> keysPressed = new ConcurrentHashMap<Character,Object>().newKeySet();
    int lPos = 450;
    int rPos = 450;

    PongPanel() {
        System.out.println();
        setBackground(Color.BLACK);


    }
    public void setUp()
    {
        g = (Graphics2D) getGraphics();
        requestFocus();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {


                keysPressed.add( e.getKeyChar());

            }

            @Override
            public void keyReleased(KeyEvent e) {
                keysPressed.remove(e.getKeyChar());

            }
        });
    }
    public void changePosValues()
    {
        if(keysPressed.contains('w'))
        {

            if(lPos >=14)
                lPos-=4;
        }
        if(keysPressed.contains('s'))
        {
            if(lPos <= 706)
            lPos+=4;
        }
        if(keysPressed.contains('i'))
        {
            if(rPos>=14)
            rPos-=4;
        }
        if(keysPressed.contains('k'))
        {
            if(rPos <= 706)
            rPos+=4;
        }
    }
    public void Paddle(Direction d)
    {

        Timer timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                changePosValues();
                synchronized (g) {
                    g.setColor(Color.black);



                    switch (d) {
                        case LEFT:
                            g.fillRect(100,10,50,900);
                            g.setColor(Color.white);
                            g.fillRoundRect(100, lPos, 50, 200, 10, 10);
                            break;
                        case RIGHT:
                            g.fillRect(1300,10,50,900);
                            g.setColor(Color.white);
                            g.fillRoundRect(1300, rPos, 50, 200, 10, 10);
                           // System.out.println("hello from right timer");
                            break;
                    }
                }
            }
        });
        timer.start();

    }
    public void startBall()
    {

        Timer timer = new Timer(40, new ActionListener() {
            int x = 200,  y = 500;
            int dx = 10, dy = 10;
            @Override
            public void actionPerformed(ActionEvent e) {
               synchronized (g) {
                   g.setColor(Color.black);
                   g.fillOval(x, y, 50, 50);
                   g.setColor(Color.white);
                   x += dx;
                   y += dy;
                   g.fillOval(x, y, 50, 50);
                   g.drawRect(10,9,1450,902);
               }
                if(y <= 10 || y >= 850)
                {
                    dy *= -1;
                }
                if(x == 150 )
                {
                    if((y + 25) - lPos < 225 &&(y + 25) - lPos > -25)
                    {
                        dx*=-1;
                    }
                }

                if(x == 1250)
                {
                    if((y + 25) - rPos < 225 &&(y + 25) - rPos > -25)
                    {
                        dx*=-1;
                    }
                }
                if(x < -50 || x > 1500)
                {
                   JOptionPane.showMessageDialog(PongPanel.this,"Game Over");
                   System.exit(0);
                }

            }
        });

        timer.start();
    }


}