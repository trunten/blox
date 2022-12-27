/*
 * Blox
 */
package blox;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 * A resource manager for sprites in the game.
 * <p>
 * [singleton]
 * <p>
 * @author Jonathan
 */
public class SpriteStore
{

   /** The single instance of this class */
   private static SpriteStore single = new SpriteStore();

   /**
    * Get the single instance of this class 
    * 
    * @return The single instance of this class
    */
   public static SpriteStore get()
   {
      return single;
   }
   /** The cached sprite map, from reference to sprite instance */
   private HashMap sprites = new HashMap();

   /**
    * Retrieve a sprite from the store
    * 
    * @param ref The reference to the image to use for the sprite
    * @return A instance of Sprite
    */
   public Sprite getSprite(String ref)
   {
      // if sprite already exists return the existing version
      if (sprites.get(ref) != null)
      {
         return (Sprite) sprites.get(ref);
      }

      // get sprite
      BufferedImage sourceImage = null;
      try
      {
         URL url = this.getClass().getClassLoader().getResource(ref);
         if (url == null)
         {
            fail("Can't find ref: " + this.getClass().toString() + "--" + ref);
         }
         
         // use ImageIO to read the image in
         sourceImage = ImageIO.read(url);
      }
      catch (IOException e)
      {
         fail("Failed to load: " + ref);
      }

      // create an accelerated image of the right size to store sprite in
      GraphicsConfiguration gc 
              = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                   .getDefaultScreenDevice()
                                   .getDefaultConfiguration();
      Image image 
              = gc.createCompatibleImage(sourceImage.getWidth(),
                                         sourceImage.getHeight(),
                                         Transparency.BITMASK);

      // draw source image into the accelerated image
      image.getGraphics().drawImage(sourceImage, 0, 0, null);

      // create a sprite, add it the cache then return it
      Sprite sprite = new Sprite(image);
      sprites.put(ref, sprite);

      return sprite;
   }

   /**
    * Utility method to handle resource loading failure
    * 
    * @param message The message to display on failure
    */
   private void fail(String message)
   {
      System.err.println(message);
      System.exit(0);
   }
}
