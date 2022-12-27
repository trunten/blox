/*
 * BLOX GAME
 */
package blox;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * BLOX the Game
 * @author Jonathan
 */
public abstract class GameObject
{
   protected Sprite sprite;
   protected double xPos;
   protected double yPos;
   protected double moveX;
   protected double moveY;
   protected Rectangle me = new Rectangle();
   protected Rectangle him = new Rectangle();

   /**
    * Construct a game object based on a sprite image and a location.
    * 
    * @param ref The reference to the image to be displayed for this game object
    * @param x The initial x location
    * @param y The initial y location
    */
   public GameObject(String ref,int x,int y) 
   {
      sprite = SpriteStore.get().getSprite(ref);
      xPos = x;
      yPos = y;
   }
	
   /**
    * Request that this object move itself
    * 
    * @param delta The amount of time that has passed in milliseconds
    */
   public void move(long delta) 
   {
   // update the location of the entity based on move speeds
      xPos += (delta * moveX) / 1000;
      yPos += (delta * moveY) / 1000;
   }
	
   /**
    * Set the horizontal speed
    * 
    * @param x The horizontal speed (pixels/sec)
    */
   public void setHorizontalMovement(double x) 
   {
      moveX = x;
   }

   /**
    * Set the vertical speed
    * 
    * @param dx The vertical speed (pixels/sec)
    */
   public void setVerticalMovement(double y) 
   {
      moveY = y;
   }
	
   /**
    * Get the horizontal speed
    * 
    * @return The horizontal speed (pixels/sec)
    */
   public double getHorizontalMovement() 
   {
      return moveX;
   }

   /**
    * Get the vertical speed
    * 
    * @return The vertical speed (pixels/sec)
    */
   public double getVerticalMovement() 
   {
      return moveY;
   }
  
   /**
    * Draw this game object to the graphics context provided
    * 
    * @param g The graphics context on which to draw
    */
   public void draw(Graphics g) 
   {
      sprite.draw(g,(int) xPos,(int) yPos);
   }
	
   /**
    * Do the logic associated with this game object. This method
    * will be called periodically based on game events
    */
   public void doLogic() {}
	
   /**
    * Get the x location of this game object
    * 
    * @return The x location of this game object
    */
   public int getX()
   {
      return (int) xPos;
   }

   /**
    * Get the y location of this game object
    * 
    * @return The y location of this game object
    */
   public int getY()
   {
      return (int) moveY;
   }
	
   /**
    * Check if this game object collided with another.
    * 
    * @param other The other game object to check collision against
    * @return True if the game objects collide with each other
    */
   public boolean collidesWith(GameObject other) 
   {
      me.setBounds((int) xPos,(int) yPos,sprite.getWidth(),
                    sprite.getHeight());
                
      him.setBounds((int) other.xPos,(int) other.yPos,
                     other.sprite.getWidth(),other.sprite.getHeight());
      return me.intersects(him);
   }
	
   /**
    * Notification that this game object collided with another.
    * 
    * @param other The game object with which this game object collided.
    */
   public void collidedWith(GameObject other){}
}

