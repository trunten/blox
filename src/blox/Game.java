/*
 * Blox
 */
package blox;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The main game.
 * 
 * @author Jonathan
 */
public class Game extends Canvas
{
   // instance variables
   private BufferStrategy strategy;
   private boolean gameRunning = true;
   private ArrayList gameObjects = new ArrayList();
   private ArrayList removeList = new ArrayList();
   private GameObject paddle;
   private GameObject ball;
   private double moveSpeed = 300;
   private int blockCount;
   private String message = "BLOX!";
   private boolean waitingForKeyPress = true;
   private boolean leftPressed = false;
   private boolean rightPressed = false;
   private boolean logicRequiredThisLoop = false;
   private int currentLevel;
   private HashMap levels = new HashMap();

   /**
    * Construct game and set it running.
    */
   public Game()
   {
      // create a frame to contain the game
      JFrame container = new JFrame("BLOX");

      // get the content of the frame and set up the resolution
      JPanel panel = (JPanel) container.getContentPane();
      panel.setPreferredSize(new Dimension(800, 600));
      panel.setLayout(null);

      // setup our canvas size and put it into the content of the frame
      setBounds(0, 0, 800, 600);
      panel.add(this);

      // Don't bother repainting
      setIgnoreRepaint(true);

      // finally make the window visible 
      container.pack(); //in case window has decoration
      container.setResizable(false);
      container.setVisible(true);

      // add a listener to respond to the user closing the window. 
      container.addWindowListener(new WindowAdapter(){
         public void windowClosing(WindowEvent e){System.exit(0);}});

      // add a key input system
      addKeyListener(new KeyInputHandler());

      // request the focus for key events
      requestFocus();

      // create the buffering strategy for accelerated graphics
      createBufferStrategy(2);
      strategy = getBufferStrategy();

      //load levels from file
      levels.clear();
      levels.putAll(BloxIO.getLevels("levels.txt"));
      currentLevel = 1;
      
      // initialise the game objects for our game
      if (levels.size() > 0)
      {
         initEntities();     
      }
      else
      {
         System.err.println("No level data available.");
         System.exit(0);
      }
   }

   /**
    * Start a fresh game
    */
   private void startGame()
   {
      // clear out any existing game objects and intialise a new set
      gameObjects.clear();
      initEntities();

      // reset keyboard settings
      leftPressed = false;
      rightPressed = false;
   }

   /**
    * Initialise the starting state of the game objects
    */
   private void initEntities()
   {
      //create the ball first so it is the bottom layer
      ball = new Ball(this, "images/ball.bmp", 390, 400);
      gameObjects.add(ball);

      // create the paddle and place it roughly in the centre
      paddle = new Paddle(this, "images/paddle.bmp", 350, 550);
      gameObjects.add(paddle);
      
      // create a block of blox (5 rows, by 9 columns, spaced evenly)
      blockCount = 0;
      Level level = (Level) levels.get(currentLevel);
      for (int row = 0; row < level.getLevelLayout().length; row++)
      {
         for (int x = 0; x < level.getLevelLayout()[row].length; x++)
         {
            if (level.getLevelLayout()[row][x].equals("Block"))
            {
               GameObject aBlock = new Block(this, getBlock(), 40 + (x * 80), (21) + row * 21);
               gameObjects.add(aBlock);
               blockCount++;
            }
         }
      }
   }

   /**
    * Notification from a game object that the logic of the game
    * should be run at the next opportunity 
    */
   public void updateLogic()
   {
      logicRequiredThisLoop = true;
   }

   /**
    * Remove an object from the game
    * 
    * @param O The object that should be removed
    */
   public void removeObject(GameObject O)
   {
      removeList.add(O);
   }

   /**
    * Notification that the player has lost. 
    */
   public void notifyLost()
   {
      message = "YOU LOSE! Try again?";
      waitingForKeyPress = true;
   }

   /**
    * Notification that the player has won
    */
   public void notifyWin()
   {
      currentLevel = (currentLevel < levels.size()) ? ++currentLevel : 1;
      message = "Well done! You Win!";
      waitingForKeyPress = true;
   }

   /**
    * Notification that a block has been hit
    */
   public void notifyBlockHit()
   {
      // reduce the block count, if there are none left, the player has won!
      blockCount--;
      if (Math.random() < 0.5)
      {
         //drop power-up
      }
      if (blockCount == 0)
      {
         notifyWin();
      }
   }
   
   public void notifyPowerUp(PowerUp powerUp)
   {
      //do stuff here depending on powerup
      //i.e change paddle size, add a gun etc.
   }

   /**
    * The main game loop. This loop is running during all game play.
    */
   public void gameLoop()
   {
      long lastLoopTime = System.currentTimeMillis();

      while (gameRunning)
      {
         // work out how long its been since the last update, this
         // will be used to calculate how far the paddel/ball should
         // move this loop
         long delta = System.currentTimeMillis() - lastLoopTime;
         lastLoopTime = System.currentTimeMillis();

         // Get hold of a graphics context for the accelerated 
         // surface and blank it out
         Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
         g.setColor(Color.white);
         g.fillRect(0, 0, 800, 600);

         // cycle round asking each object to move itself
         moveObjects(delta);

         // cycle round drawing all objects we have in the game
         drawObjects(g);

         // check if any objects have hit eacher other
         checkCollisions();

         // remove any object that has been marked for clear up
         gameObjects.removeAll(removeList);
         removeList.clear();

         // check to see if the game logic needs to be applied to any
         // of the objects in the game.
         doLogic();

         // if we are waiting for a key press display a message on the screen
         if (waitingForKeyPress)
         {
            g.setColor(Color.black);
            g.drawString(message, (800 - g.getFontMetrics().stringWidth(message)) / 2, 250);
            g.drawString("Press any key", (800 - g.getFontMetrics().stringWidth("Press any key")) / 2, 300);
         }

         // clear up the graphics and flip the buffer over
         g.dispose();
         strategy.show();

         paddleMove();
      }
   }
   
   /**
    * cycle round asking each object to move itself
    * @param delta The amount of time passed since last loop
    */
   private void moveObjects(long delta)
   {
      if (!waitingForKeyPress)
      {
         for (int i = 0; i < gameObjects.size(); i++)
         {
            GameObject obj = (GameObject) gameObjects.get(i);
            obj.move(delta);
         }
      }
   }
   
   /**
    * Cycle round drawing all objects in the game
    * @param g Graphics strategy
    */
   private void drawObjects(Graphics2D g)
   {
      for (int i = 0; i < gameObjects.size(); i++)
      {
         GameObject obj = (GameObject) gameObjects.get(i);
         obj.draw(g);
      }
   }
   
   /**
    * brute force collisions, compare every object against
    * every other object. If any of them collide notify 
    * both objects that the collision has occurred
    */
   private void checkCollisions()
   {
      for (int p = 0; p < gameObjects.size(); p++)
      {
         for (int s = p + 1; s < gameObjects.size(); s++)
         {
            GameObject me = (GameObject) gameObjects.get(p);
            GameObject him = (GameObject) gameObjects.get(s);
            if (me != null && him != null && me.collidesWith(him))
            {
               me.collidedWith(him);
               him.collidedWith(me);
            }   
         }
      }
   }
   
   /** 
    * Resolve the movement of the paddle. First assume the paddle
    * isn't moving. If either cursor key is pressed then
    * update the movement appropriately
    */
   private void paddleMove()
   {
      paddle.setHorizontalMovement(0);
      if ((leftPressed) && (!rightPressed))
      {
         paddle.setHorizontalMovement(-moveSpeed);
      }
      else if ((rightPressed) && (!leftPressed))
      {
         paddle.setHorizontalMovement(moveSpeed);
      }
   }
   
   /**
    * if a game event has indicated that game logic should
    * be resolved, cycle round every object requesting that
    * their personal logic should be considered.
    */
   private void doLogic()
   {
      if (logicRequiredThisLoop)
      {
         for (int i = 0; i < gameObjects.size(); i++)
         {
            GameObject obj = (GameObject) gameObjects.get(i);
            obj.doLogic();
         }
         logicRequiredThisLoop = false;
      }
   }
   
   private String getBlock()
   {
      String[] images = {"blue", "black", "yellow", "red", "green", "grey"};      
      for (int i = 0; i < images.length; i++)       
      {           
         images[i] = "images/" + images[i] + "_block.bmp";       
      }       
      int rnd = (int) (Math.random() * images.length);       
      return images[rnd];            
   }

   /**
    * A class to handle keyboard input from the user.
    * Used an internal class for the lulz!
    * 
    * @author Jonathan
    */
   private class KeyInputHandler extends KeyAdapter
   {
      /**
       * Notification of a key press. A key being pressed is equal to being 
       * pushed down but not released. Thats where keyTyped() comes in.
       *
       * @param event The details of the key that was pressed 
       */
      @Override
      public void keyPressed(KeyEvent event)
      {
         // if we're waiting for an "any key" typed then we don't 
         // want to do anything with just a "press"
         if (waitingForKeyPress)
         {
            return;
         }

         if (event.getKeyCode() == KeyEvent.VK_LEFT)
         {
            leftPressed = true;
         }
         if (event.getKeyCode() == KeyEvent.VK_RIGHT)
         {
            rightPressed = true;
         }
      }

      /**
       * Notification that a key has been released.
       *
       * @param event The details of the key that was released 
       */
      @Override
      public void keyReleased(KeyEvent event)
      {
         // if we're waiting for an "any key" typed then we don't 
         // want to do anything with just a "released"
         if (waitingForKeyPress)
         {
            return;
         }

         if (event.getKeyCode() == KeyEvent.VK_LEFT)
         {
            leftPressed = false;
         }
         if (event.getKeyCode() == KeyEvent.VK_RIGHT)
         {
            rightPressed = false;
         }
      }

      /**
       * Notification that a key has been typed.
       * Typing a key means to both press and then release it.
       *
       * @param event The details of the key that was typed. 
       */
      @Override
      public void keyTyped(KeyEvent event)
      {
         // if we hit escape, then quit the game
         if (event.getKeyChar() == 27)
         {
            System.exit(0);
         }
         if (waitingForKeyPress)
         {
            waitingForKeyPress = false;
            startGame();
         }
      }
   }
}