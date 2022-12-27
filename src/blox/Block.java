/*
 * BLOX
 */
package blox;

/**
 * A block 4 blox!
 * 
 * @author Jonathan
 */
public class Block extends GameObject
{
   /** The game in which the block exists */
   private Game game;
   
   /**
    * Create a new block
    * 
    * @param game The game in which this block is being created
    * @param ref The sprite which should be displayed for this block
    * @param x The initial x location of this block
    * @param y The initial y location of this block
    */
   public Block(Game game, String ref, int x, int y)
   {
      super(ref, x, y);

      this.game = game;
   }
   
   public void move(){}
}
