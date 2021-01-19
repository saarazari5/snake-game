package Controllers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel  extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_WIDTH)/UNIT_SIZE;
    static final int DELAY = 75;

    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    int bodyParts = 6;
    int appleEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

  public GamePanel() {
        random = new Random();
        setPreferredSize(new Dimension(SCREEN_WIDTH , SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new SnakeKeyAdapter());
        startGame();
    }

    public void startGame(){
      newApple();
      running = true;
      timer=new Timer(DELAY, this);
      timer.start();

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
      if(running) {
          g.setColor(Color.RED);
          g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

          for (int i = 0; i < bodyParts; i++) {
              if (i == 0) {
                  g.setColor(Color.GREEN);
              } else {
                  g.setColor(new Color(45, 180, 0));
              }
              g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
          }
            drawScore(g);

      }else {
          gameOver(g);
      }

    }


    private  void drawScore(Graphics g){
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free" , Font.BOLD , 40));
        FontMetrics metrics =getFontMetrics(g.getFont());
        g.drawString("Score: "  + appleEaten  , (SCREEN_WIDTH - metrics.stringWidth("Score: "+ appleEaten))/2 , g.getFont().getSize());
    }

    //generate the coordinates of a new apple
    public void newApple(){
        appleX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE) * UNIT_SIZE;
    }

    public void move(){
        for(int i = bodyParts ; i>0 ; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch (direction){
            case 'U': //UP
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D': //DOWN
                y[0] = y[0] + UNIT_SIZE;
                break;

            case 'L': //Left
                x[0] = x[0] - UNIT_SIZE;
                break;

            case 'R': //Right
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple(){
        if((x[0] == appleX)  && (y[0] == appleY)){
            bodyParts++;
            appleEaten++;
            newApple();
        }
    }

    public void checkCollisions(){
        for (int i = bodyParts ; i>0 ; i--){
            if(x[0] == x[i] && y[0]==y[i]){
                running=false;
            }
            //check head touch any border
            if(x[0] < 0 || x[0] > SCREEN_WIDTH || y[0]<0 || y[0] > SCREEN_HEIGHT) running = false;

            if(!running) timer.stop();
        }
    }



    public void gameOver(Graphics g){
        drawScore(g);
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free" , Font.BOLD , 75));
        FontMetrics metrics =getFontMetrics(g.getFont());
        g.drawString("Game Over" , (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2 , SCREEN_HEIGHT/2 );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();
        }

        repaint();
    }


    public class SnakeKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R')  direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L')  direction = 'R';
                    break;

                case KeyEvent.VK_UP:
                    if(direction != 'D')  direction = 'U';
                    break;

                case KeyEvent.VK_DOWN:
                    if(direction != 'U')  direction = 'D';
                    break;
            }
        }
    }
}
