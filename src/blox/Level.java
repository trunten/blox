/*
 * BLOX
 */
package blox;

/**
 * A Level object provides layout information for blocks in the main game
 * 
 * @author Jonathan
 */
public class Level
{
   //instance variables
   private int levelNumber;
   private String[][] levelLayout;
   
   /**
    * Construct a new level
    * 
    * @param level The level number ie. 5
    * @param layout The layout of blocks in a 5-9 2d array
    */
   public Level(int level, String[][] layout)
   {
      levelNumber = level;
      levelLayout = layout;
   }
   
   /**
    * Returns the level number for the receiver
    * 
    * @return levelNumber
    */
   public int getLevelNumber()
   {
      return levelNumber;
   }
   
   /**
    * Returns a two dimensional array of of the level layout
    * 
    * @return levelLayout
    */
   public String[][] getLevelLayout()
   {
      return levelLayout;
   }
}
