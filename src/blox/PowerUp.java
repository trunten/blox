/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blox;

/**
 *
 * @author jond
 */
public class PowerUp extends GameObject
{
   private String name;
   private Game game;
   
   /**
    * Create a new PowerUp
    *  
    * @param game The game in which the PowerUp is being created
    * @param ref The reference to the sprite to show for the PowerUp
    * @param x The initial x location of the PowerUp
    * @param y The initial y location of the PowerUp
    */
   public PowerUp(Game game, String name, String ref, int x, int y)
   {
      super(ref, x, y);
      this.moveY = 150;
      this.game = game;
      this.name = ref;
   }
   
   /**
    * Make the power-up fall
    */
   @Override
   public void move(long delta)
   {
      super.move(delta);
      // if power is below the bottom of the screen then remove
      if (yPos > 610)
      {
         game.removeObject(this);
      }
   }
   
   /**
    * Notification that the power-up has hit something
    * 
    * @param other The other object it collided with
    */
   @Override
   public void collidedWith(GameObject obj)
   {
      if (obj instanceof Paddle)
      {
         // remove the blcok
         game.removeObject(this);

         // notify the game that the block has been hit
         game.notifyPowerUp(this);
      }
   }
}
