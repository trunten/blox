/*
 * Blox
 */
package blox;

/**
 * A paddle for blox
 * 
 * @author Jonathan
 */
public class Paddle extends GameObject
{

   /** The game in which the paddle exists */
   private Game game;

   /**
    * Create a new paddle
    *  
    * @param game The game in which the paddle is being created
    * @param ref The reference to the sprite to show for the paddle
    * @param x The initial x location of the player's paddle
    * @param y The initial y location of the player's paddle
    */
   public Paddle(Game game, String ref, int x, int y)
   {
      super(ref, x, y);

      this.game = game;
   }

   /**
    * Request that the paddle move itself based on an elapsed amount of
    * time
    * 
    * @param delta The time that has elapsed since last move (ms)
    */
   @Override
   public void move(long delta)
   {
      // if the paddle is moving left and has reached the left hand side
      // of the screen, don't move
      if ((moveX < 0) && (xPos < 10))
      {
         return;
      }
      // if the paddle is moving right and has reached the right hand side
      // of the screen, don't move
      if ((moveX > 0) && (xPos > 690))
      {
         return;
      }
      // otherwise move!
      super.move(delta);
   }
}
