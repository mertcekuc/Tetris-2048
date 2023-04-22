import java.awt.Color; // the color type used in StdDraw
import java.awt.Font; // the font type used in StdDraw
import java.util.Random;

// A class used for modeling numbered tiles as in 2048
public class Tile {
   // Data fields: instance variables
   // --------------------------------------------------------------------------
   private int number; // the number on the tile
   private Color backgroundColor; // background (tile) color
   private Color foregroundColor; // foreground (number) color
   private Color boxColor; // box (boundary) color

   // Data fields: class variables
   // --------------------------------------------------------------------------
   // the value of the boundary thickness (for the boundaries around the tiles)
   private static double boundaryThickness = 0.004;
   // the font used for displaying the tile number
   private static Font font = new Font("Arial", Font.PLAIN, 14);

   // Methods
   // --------------------------------------------------------------------------
   // the default constructor that creates a tile with 2 as the number on it

   public Tile(char type){
      Random random= new Random();
      int numberpow=1 + random.nextInt(5);
      number=(int)  (Math.pow(2,(double) numberpow));
      foregroundColor = new Color(42, 26, 59, 255);
      boxColor = new Color(82, 49, 112);

      if (type == 'I'){
         backgroundColor = new Color(124, 229, 255);
      }
      else if(type == 'O'){
         backgroundColor = new Color(255, 255, 129);
      }
      else if (type == 'L'){
         backgroundColor = new Color(255, 175, 129);
      }
      else if (type == 'S'){
         backgroundColor = new Color(149, 255, 128);
      }
      else if (type == 'T'){
         backgroundColor = new Color(238, 128, 255);
      }
      else if (type == 'J'){
         backgroundColor = new Color(127, 155, 255);
      }
      else{
         backgroundColor = new Color(255, 110, 110);
      }
   }

   // a method for drawing the tile
   public void draw(Point position, double... sideLength) {
      // the default value for the side length (sLength) is 1
      double sLength;
      if (sideLength.length == 0) // sideLength is a variable-length parameter
         sLength = 1;
      else
         sLength = sideLength[0];
      // draw the tile as a filled square
      StdDraw.setPenColor(backgroundColor);
      StdDraw.filledSquare(position.getX(), position.getY(), sLength / 2);
      // draw the bounding box around the tile as a square
      StdDraw.setPenColor(boxColor);
      StdDraw.setPenRadius(boundaryThickness);
      StdDraw.square(position.getX(), position.getY(), sLength / 2);
      StdDraw.setPenRadius(); // reset the pen radius to its default value
      // draw the number on the tile
      StdDraw.setPenColor(foregroundColor);
      StdDraw.setFont(font);
      StdDraw.text(position.getX(), position.getY(), "" + number);
   }

   public int getNumber(){
      return number;
   }

   public void setNumber(int num){
      this.number=num;
   }
}