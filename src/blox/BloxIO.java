/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blox;

import java.util.*;
import java.io.*;

/**
 * Class to sort out the loading and creating of levels for the blox game.
 * The class has one public method which can only be accessed statically
 * which returns a map of levels.
 * @author jond
 */
public class BloxIO
{
   private static HashMap levels = new HashMap();
   
   private BloxIO(){}
   
   /**
    * Static method which returns a map of levels for blox
    * 
    * @param ref String reference for the levels file
    * @return HashMap<Integer, Level>
    */
   public static HashMap getLevels(String ref)
   {
      BloxIO.createLevels(ref);
      return BloxIO.levels;
   }
   
   /**
    * Read level data from a text file which is located at the String reference
    * ref. Returns an Map of rows with there associated level/row number as
    * a key.
    * 
    * @param ref String reference for the levels file
    * @return HashMap<String, String[]>
    */
   private static HashMap readLevelData(String ref)
   {
      
      BufferedReader bufferedFileReader = null;
      HashMap readLevelData = new HashMap();
      try
      {
         File aFile = new File(ref);
         String levelRowName;
         Scanner lineScanner;
         bufferedFileReader = new BufferedReader(new FileReader(aFile));
         String currentLine = bufferedFileReader.readLine();
         while (currentLine !=null)
         {
            String[] row = new String[9];
            lineScanner = new Scanner(currentLine);
            lineScanner.useDelimiter(", ");
            levelRowName = lineScanner.next();
            int i = 0;
            while (lineScanner.hasNext() && i < 9)
            {
               row[i] = lineScanner.next();
               i++;     
            }
            if (row.length == 9)
            {
               readLevelData.put(levelRowName, row);
            }
            currentLine = bufferedFileReader.readLine();
         }
      }
      catch (Exception e)
      {
         System.out.println("Ooops:" + e);
      }
      finally
      {
         try
         {
            bufferedFileReader.close();
         }
         catch (Exception e)
         {
            System.out.println("Ooops:" + e);
         }
      }
      return readLevelData;
   }
   
   /**
    * Uses the data red from the text file ref to create levels and store them
    * in a Map which will eventually be returned by the method getLevels
    * 
    * @param ref String reference for the levels file
    */
   private static void createLevels(String ref)
   {
      String[] defaultLevel = {"Block", "Block", "Block", "Block", "Block", "Block", "Block", "Block", "Block"}; 
      String[] temp;
      HashMap levelData = readLevelData(ref);
      for (int i = 0; i < (levelData.size() / 5); i++)
      {
         String[][] level = new String[5][9];
         for (int j = 0; j < 5; j++)
         {
            if (levelData.get((i + 1) + "-" + (j + 1)) != null)
            {
               temp = (String[]) levelData.get((i + 1) + "-" + (j + 1));
            }
            else
            {
               temp = defaultLevel;
            }
            System.arraycopy(temp, 0, level[j], 0, temp.length);
         }
         BloxIO.levels.put((i + 1), new Level((i + 1), level));
      }
   }
}
