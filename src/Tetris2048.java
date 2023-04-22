import java.awt.Color; // the color type used in StdDraw
import java.awt.Font; // the font type used in StdDraw
import java.awt.event.KeyEvent; // for the key codes used in StdDraw
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

// The main class to run the Tetris 2048 game
public class Tetris2048 {
   static AtomicBoolean restart= new AtomicBoolean();
   public static void main(String[] args) {

      restart.set(false);
      // set the size of the game grid
      int gridH = 18, gridW = 12;
      // set the size of the drawing canvas
      int canvasH = 40 * gridH, canvasW = 50 * gridW;
      StdDraw.setCanvasSize(canvasW, canvasH);
      // set the scale of the coordinate system
      StdDraw.setXscale(-0.5, gridW + 4.5);
      StdDraw.setYscale(-0.5, gridH - 0.5);
      // double buffering enables computer animations, creating an illusion of
      // motion by repeating four steps: clear, draw, show and pause
      StdDraw.enableDoubleBuffering();

      // set the dimension values stored and used in the Tetromino class
      Tetromino.gridHeight = gridH;
      Tetromino.gridWidth = gridW;

      // create the game grid
      GameGrid grid = new GameGrid(gridH, gridW);
      // create the first tetromino to enter the game grid
      // by using the createTetromino method defined below

      Tetromino currentTetromino = createTetromino();
      Tetromino nextTetromino = createTetromino();
      grid.setNextTetromino(nextTetromino);
      grid.setCurrentTetromino(currentTetromino);
      // display a simple menu before opening the game
      // by using the displayGameMenu method defined below
      displayGameMenu(gridH, gridW);

      // the main game loop (using some keyboard keys for moving the tetromino)
      // -----------------------------------------------------------------------
      int iterationCount = 0; // used for the speed of the game
      while (true) {
         // check user interactions via the keyboard
         // --------------------------------------------------------------------
         // if the left arrow key is being pressed
         if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT))
            // move the active tetromino left by one
            currentTetromino.move("left", grid);
         // if the right arrow key is being pressed
         else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT))
            // move the active tetromino right by one
            currentTetromino.move("right", grid);
         // if the down arrow key is being pressed
         else if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN))
            // move the active tetromino down by one
            currentTetromino.move("down", grid);
            // rotate tetromino
         else if(StdDraw.isKeyPressed(KeyEvent.VK_UP)){
            currentTetromino.rotate();
         }

         else if(StdDraw.isKeyPressed(KeyEvent.VK_SPACE)){
            currentTetromino.goDown(grid);
         }

         else if(StdDraw.isKeyPressed(KeyEvent.VK_ESCAPE)){

            displayPauseMenu(gridH,gridW,restart);

         }

         if (restart.get()){
            grid=new GameGrid(gridH,gridW);
            grid.score=0;
            currentTetromino=createTetromino();
            nextTetromino=createTetromino();
            grid.setCurrentTetromino(currentTetromino);
            grid.setNextTetromino(nextTetromino);
            restart.set(false);
         }

         // move the active tetromino down by 1 once in 10 iterations (auto fall)
         boolean success = true;
         if (iterationCount % 10 == 0)
            success = currentTetromino.move("down", grid);
         iterationCount++;

         // place the active tetromino on the grid when it cannot go down anymore
         if (!success) {
            // get the tile matrix of the tetromino without empty rows and columns
            currentTetromino.createMinBoundedTileMatrix();
            Tile[][] tiles = currentTetromino.getMinBoundedTileMatrix();
            Point pos = currentTetromino.getMinBoundedTileMatrixPosition();
            // update the game grid by locking the tiles of the landed tetromino
            boolean gameOver = grid.updateGrid(tiles, pos);
            // end the main game loop if the game is over
            if (gameOver)
               gameOberMenu(gridH,gridW,restart);
            // create the next tetromino to enter the game grid
            // by using the createTetromino function defined below
            currentTetromino = nextTetromino;
            nextTetromino= createTetromino();
            grid.setCurrentTetromino(currentTetromino);
            grid.setNextTetromino(nextTetromino);
         }
         grid.mergeTiles();
         grid.dropTiles();
         grid.cleanRow();

         // display the game grid and the current tetromino
         grid.display();

      }

      // print a message on the console that the game is over
   }

   // A method for creating a random shaped tetromino to enter the game grid
   public static Tetromino createTetromino() {
      // the type (shape) of the tetromino is determined randomly
      char[] tetrominoTypes = { 'I', 'O', 'Z', 'J', 'L', 'S', 'T','.'};
      Random random = new Random();
      int randomIndex = random.nextInt(tetrominoTypes.length);
      char randomType = tetrominoTypes[randomIndex];
      // create and return the tetromino
      Tetromino tetromino = new Tetromino(randomType);
      return tetromino;
   }

   // A method for displaying a simple menu before starting the game
   public static void displayGameMenu(int gridHeight, int gridWidth) {
      // colors used for the menu
      Color backgroundColor = new Color(42, 26, 59, 255);
      Color buttonColor = new Color(82, 49, 112);
      Color textColor = new Color(175, 157, 241);
      // clear the background canvas to background_color
      StdDraw.clear(backgroundColor);
      // the relative path of the image file
      String imgFile = "images/menu_image.png";
      // center coordinates to display the image
      double imgCenterX = (gridWidth + 3.5) / 2.0, imgCenterY = gridHeight - 7;
      // display the image
      StdDraw.picture(imgCenterX, imgCenterY, imgFile);
      // the width and the height of the start game button
      double buttonW = gridWidth - 1.5, buttonH = 2;
      // the center point coordinates of the start game button
      double buttonX = imgCenterX, buttonY = 5;
      // display the start game button as a filled rectangle
      StdDraw.setPenColor(buttonColor);
      StdDraw.filledRectangle(buttonX, buttonY, buttonW / 2, buttonH / 2);
      // display the text on the start game button
      Font font = new Font("Arial",Font.BOLD, 35);
      StdDraw.setFont(font);
      StdDraw.setPenColor(textColor);
      String textToDisplay = "Start Game";
      StdDraw.text(buttonX, buttonY, textToDisplay);
      // menu interaction loop
      while (true) {
         // display the menu and wait for a short time (50 ms)
         StdDraw.show();
         StdDraw.pause(50);
         // check if the mouse is being pressed on the button
         if (StdDraw.isMousePressed()) {
            // get the x and y coordinates of the position of the mouse
            double mouseX = StdDraw.mouseX(), mouseY = StdDraw.mouseY();
            // check if these coordinates are inside the button
            if (mouseX > buttonX - buttonW / 2 && mouseX < buttonX + buttonW / 2)
               if (mouseY > buttonY - buttonH / 2 && mouseY < buttonY + buttonH / 2)
                  break; // break the loop to end the method and start the game
         }
      }
   }

   public static void showScore(double gridW, double gridH){
      double scoreX= gridW+1.0;
      double scorey= gridH-3;

      Font font=new Font("Arial",1,20);
      StdDraw.setFont(font);
      Color textColor = new Color(175, 157, 241);
      StdDraw.setPenColor(textColor);
      StdDraw.text(scoreX,scorey,"Score: " +GameGrid.score);

   }

   public static void displayPauseMenu(int gridHeight, int gridWidth, AtomicBoolean restart) {
      // clear the background canvas to background_color
      Color backgroundColor = new Color(42, 26, 59, 255);
      Color buttonColor = new Color(82, 49, 112);
      Color textColor = new Color(175, 157, 241);
      Font font = new Font("Arial",Font.BOLD, 35);

      StdDraw.clear(backgroundColor);
      // the relative path of the image file
      String imgFile = "images/menu_image.png";
      // center coordinates to display the image
      double imgCenterX = (gridWidth + 3.5) / 2.0, imgCenterY = gridHeight - 7;
      // display the image
      StdDraw.picture(imgCenterX, imgCenterY, imgFile);

      // the width and the height of the resume game button
      double buttonW = gridWidth - 1.5, buttonH = 2;
      // the center point coordinates of the start game button
      double buttonX = imgCenterX, buttonY = 5;
      // display the resume game button as a filled rectangle
      StdDraw.setPenColor(buttonColor);
      StdDraw.filledRectangle(buttonX, buttonY, buttonW / 2, buttonH / 2);
      // display the text on the resume game button
      StdDraw.setFont(font);
      StdDraw.setPenColor(textColor);
      String textToDisplay = "Resume Game";
      StdDraw.text(buttonX, buttonY, textToDisplay);

      double restartW = gridWidth - 1.5, restartH = 2;
      // the center point coordinates of the resume game button
      double restartX = imgCenterX, restartY = 2;
      // display the resume game button as a filled rectangle
      StdDraw.setPenColor(buttonColor);
      StdDraw.filledRectangle(restartX, restartY, (restartW / 2), restartH / 2);
      // display the text on the resume game button
      StdDraw.setFont(font);
      StdDraw.setPenColor(textColor);
      String textToDisplay2 = "Restart Game";
      StdDraw.text(restartX, restartY, textToDisplay2);

      // menu interaction loop
      while (true) {
         // display the menu and wait for a short time (50 ms)
         StdDraw.show();
         StdDraw.pause(50);
         // check if the mouse is being pressed on the button
         if (StdDraw.isMousePressed()) {
            // get the x and y coordinates of the position of the mouse
            double mouseX = StdDraw.mouseX(), mouseY = StdDraw.mouseY();
            // check if these coordinates are inside the button
            if (mouseX > buttonX - buttonW / 2 && mouseX < buttonX + buttonW / 2)
               if (mouseY > buttonY - buttonH / 2 && mouseY < buttonY + buttonH / 2) {
                  break; // break the loop to end the method and resume the game
               }
            else if (mouseX > restartX - restartH / 2 && mouseX < restartX + restartW / 2){
                  if (mouseY > restartY - restartH / 2 && mouseY < restartY + restartH / 2) {
                     restart.set(true);
                     break;

                  }
               }

         }
      }
   }
   public static void gameOberMenu(int gridHeight, int gridWidth, AtomicBoolean restart){
      // clear the background canvas to background_color
      Color backgroundColor = new Color(42, 26, 59, 255);
      Color buttonColor = new Color(82, 49, 112);
      Color textColor = new Color(175, 157, 241);
      Font font = new Font("Arial",Font.BOLD, 35);
      Font font2 = new Font("Arial",Font.BOLD, 80);

      StdDraw.clear(backgroundColor);
      // the relative path of the image file
      String imgFile = "images/menu_image.png";
      // center coordinates to display the image
      double imgCenterX = (gridWidth + 3.5) / 2.0, imgCenterY = gridHeight - 7;
      // display the image
      StdDraw.picture(imgCenterX, imgCenterY, imgFile);

      // the width and the height of the resume game button
      double buttonW = gridWidth - 1.5, buttonH = 2;
      // the center point coordinates of the start game button
      double buttonX = imgCenterX, buttonY = 5;
      // display the resume game button as a filled rectangle
      StdDraw.setPenColor(buttonColor);
      StdDraw.filledRectangle(buttonX, buttonY, buttonW / 2, buttonH / 2);
      // display the text on the resume game button
      StdDraw.setFont(font);
      StdDraw.setPenColor(textColor);
      String textToDisplay = "Score: "+ +GameGrid.score;
      StdDraw.text(buttonX, buttonY, textToDisplay);



      double restartW = gridWidth - 1.5, restartH = 2;
      // the center point coordinates of the resume game button
      double restartX = imgCenterX, restartY = 2;
      // display the resume game button as a filled rectangle
      StdDraw.setPenColor(buttonColor);
      StdDraw.filledRectangle(restartX, restartY, (restartW / 2), restartH / 2);
      // display the text on the resume game button
      StdDraw.setFont(font);
      StdDraw.setPenColor(textColor);
      String textToDisplay2 = "Restart Game";
      StdDraw.text(restartX, restartY, textToDisplay2);

      textColor = new Color(243, 248, 127);
      StdDraw.setPenColor(textColor);
      StdDraw.setFont(font2);
      String govertext= "GAME OVER";
      StdDraw.text(imgCenterX, imgCenterY+4, govertext);

      // menu interaction loop
      while (true) {
         // display the menu and wait for a short time (50 ms)
         StdDraw.show();
         StdDraw.pause(50);
         // check if the mouse is being pressed on the button
         if (StdDraw.isMousePressed()) {
            // get the x and y coordinates of the position of the mouse
            double mouseX = StdDraw.mouseX(), mouseY = StdDraw.mouseY();
            // check if these coordinates are inside the button
            if (mouseX > restartX - restartH / 2 && mouseX < restartX + restartW / 2){
                  if (mouseY > restartY - restartH / 2 && mouseY < restartY + restartH / 2) {
                     restart.set(true);
                     break;

                  }
               }
         }
      }
   }
}