import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new PongGui();
    }
}
 class PongGui extends JFrame {

    public PongGui() throws IOException, ClassNotFoundException {

        setTitle("PONG");
        setSize(1500, 1000);
        boolean playAgain = true;


        PongPanel pongPanel = new PongPanel();
        add(pongPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        pongPanel.startBall();
        pongPanel.Paddle(Direction.LEFT);
        // pongPanel.Paddle(Direction.RIGHT);
        pongPanel.requestFocus();


    }
}

 class Score implements Comparable, Serializable  {
    public final int score;
    public final String name;
    public Score(int x, String y) {
        this.score = x;
        this.name = y;
    }

     @Override
     public int compareTo(Object o) {
         Score that = (Score) o;
         return that.score - score;
     }
 }
class ScoreBoard implements Serializable
{
    public ArrayList<Score> scores = new ArrayList<Score>();
    public void addScore(int x, String s)
    {
        scores.add(new Score(x,s));
        Collections.sort(scores);
    }
    public void addScore(Score s)
    {
        scores.add(s);
        Collections.sort(scores);
    }
    public boolean isHighScore(int score)
    {
        if(scores.size()<10)
            return true;
        return scores.get(scores.size()-1).score < score;
    }
    public void display(JComponent component)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (Score score: scores) {
            stringBuilder.append(score.name + ": " + score.score + '\n');
        }
        JOptionPane.showMessageDialog(component,stringBuilder);
    }

}
enum Direction{
    RIGHT,LEFT
        }
class PongPanel extends JPanel //implements Runnable
{
    ScoreBoard scoreBoard = new ScoreBoard();
  //  Set<Character> keysPressed = new ConcurrentHashMap<Character, Object>().newKeySet();
    int lPos = 450;
    int rPos = 450;
    int x = 200, y = 500;
    int dx = 10, dy = 10;
    int paddleSpeed = 4;
    int score = 0;

    PongPanel() throws IOException, ClassNotFoundException {

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("scores.obj"));
        scoreBoard = (ScoreBoard) ois.readObject();
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
//        addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                keysPressed.add(e.getKeyChar());
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//                keysPressed.remove(e.getKeyChar());
//            }
//        });
    }
    public void gameOver() throws IOException {
        switch(JOptionPane.showConfirmDialog(this, "Would you like to play again?"))
        {
            case 0:
                resetGame();
                break;
            case 1:
            case 2:
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("scores.obj"));
                oos.writeObject(scoreBoard);
                System.exit(0);
        }
    }

    private void resetGame() {
        rPos = lPos = 450;
        x = 200; y = 500;
        dx = dy = 10;
    }

//    public void changePosValues() {
//
//        if (keysPressed.contains('i')) {
//            if (rPos >= 10 + paddleSpeed)
//                rPos -= paddleSpeed;
//        }
//        if (keysPressed.contains('k')) {
//            if (rPos <= 710 - paddleSpeed)
//                rPos += paddleSpeed;
//        }
//    }

    public void Paddle(Direction d) {
        Graphics2D g = (Graphics2D) getGraphics();

        Timer timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //changePosValues();

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

    public void startBall()  {
        Graphics2D g = (Graphics2D) getGraphics();
        Timer timer = new Timer(40, new ActionListener() {

            int ballRadius = 25;


            @Override
            public void actionPerformed(ActionEvent e) {
                boolean playing = true;

                g.setColor(Color.black);
                g.fillOval(x, y, 2 * ballRadius, 2 * ballRadius);
                g.setColor(Color.white);
                x += dx;
                y += dy;
                g.fillOval(x, y, 2 * ballRadius, 2 * ballRadius);
                g.drawRect(10, 9, 1450, 902);
                //keep ball bouncing in frame
                if (y <= 10 || y >= 900
                        - 2 * ballRadius)//to accommodate for the height of the ball
                {

                    dy *= -1;
                }
                if (x == 150) {
                    if ((y + ballRadius) - lPos < 225
                            && (y + ballRadius) - lPos > -ballRadius)//ball is not within range of the left paddle
                    {
                        score++;
                        dx *= -1;
                    }
                }

//                if (x == 1250) {
//                    if ((y + ballRadius) - rPos < 225
//                            && (y + ballRadius) - rPos > -ballRadius)//ball is not within range of the right paddle
//                    {
//                        dx *= -1;
//                    }
//                }
                if (x == 500)// For the one player version
                {

                    dx *= -1;
                }
                if (x < -(2 * ballRadius) || x > 1500)//the ball went out of bounds
                {
                    if(scoreBoard.isHighScore(score))
                    {
                        System.out.println("HIGH SCO    eRE");
                        String name = JOptionPane.showInputDialog(null,"What is your name?");
                        scoreBoard.addScore(score,name);
                    }
                    scoreBoard.display(PongPanel.this);
                    try {
                        gameOver();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        });

        timer.start();
        }
}