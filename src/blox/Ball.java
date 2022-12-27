/*
 * BLOX!
 */
package blox;

/**
 * A ball for blox
 * 
 * @author Jonathan
 */
public class Ball extends GameObject
{

   /** The speed at which the ball moves (pixels/sec) */
   private double moveSpeed = -250;
   private Game game;
   /** last time paddle was hit. Used to try and stop annoying jiggle */
   private long paddleHit;

   /**
    * Create a new ball
    * 
    * @param game The game in which the shot has been created
    * @param sprite The sprite representing the ball
    * @param x The initial x location of the ball
    * @param y The initial y location of the ball
    */
   public Ball(Game game, String sprite, int x, int y)
   {
      super(sprite, x, y);
      this.game = game;
      this.moveY = moveSpeed;
      this.moveX = moveSpeed;
      this.paddleHit = System.currentTimeMillis();
   }

   /**
    * Move the ball
    * 
    * @param delta The time that has elapsed since last move
    */
   @Override
   public void move(long delta)
   {
      // proceed with normal move
      super.move(delta);

      // if ball hits the side change direction
      if (yPos < 10 && moveY < 0)
      {
         moveY = -moveY;
      }
      if ((xPos <10 && moveX <0) || (xPos > 780 && moveX > 0))
      {
         moveX = -moveX;
      }
      
      // if ball is below the bottom of the screen then you lose
      if (yPos > 610)
      {
         game.notifyLost();
      }
   }

   /**
    * Notification that the ball has hit something
    * 
    * @param other The other object it collided with
    */
   @Override
   public void collidedWith(GameObject obj)
   {
      if (obj instanceof Block)
      {
         // remove the blcok
         game.removeObject(obj);

         // notify the game that the block has been hit
         game.notifyBlockHit();
         game.updateLogic();
         moveX = -moveX;
      }
      if (obj instanceof Paddle && moveY > 0)
      {
         if (System.currentTimeMillis() - paddleHit > 2000)
         paddleHit = System.currentTimeMillis();
         moveX = -moveX;
         game.updateLogic();
      }
   }
   
   private boolean isMovingUp(){return (moveY < 0);}
   private boolean isMovingDown(){return (moveY > 0);}
   private boolean isMovingLeft(){return (moveX < 0);}
   private boolean isMovingRight(){return (moveX > 0);}
   
   @Override
   public void doLogic()
   {
      boolean up = isMovingUp();
      boolean down = isMovingDown();
      boolean left = isMovingLeft();
      boolean right = isMovingRight();
      if (up || down)
      {
         moveY = -moveY;
      }
      if (right || left)
      {
         moveX = -moveX;
      }
   }
}
