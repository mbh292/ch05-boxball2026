import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Class BallDemo - a short demonstration showing animation with the 
 * Canvas class. 
 *
 * @author Michael Kölling and David J. Barnes
 * @author Mustafa Has
 * @version 2026.11.01
 */

public class BallDemo   
{
    private static final int MIN_BALLS = 5;         // fewest balls boxBounce will run
    private static final int MAX_BALLS = 30;        // most balls boxBounce will run
    private static final int BALL_DIAMETER = 16;    // size of each BoxBall in pixels
    private static final int TICK_DELAY = 20;       // milliseconds between animations
    private static final int MAX_BRIGHTNESS = 180;  // 0 = black, 255 = white
 
    private Canvas myCanvas;
    private Box box;
    private Random rand = new Random();

    /**
     * Create a BallDemo object. Creates a fresh canvas and makes it visible.
     * 
     */
    public BallDemo()
    {
        myCanvas = new Canvas("Ball Demo", 600, 500);
        box=new Box (100,100,500,400, myCanvas);
        box.draw();
        
        Box box2 = new Box(myCanvas);
        box2.draw();
        
    }

    /**
     * boxBounce - simulate 5-30 balls bouncing within a box
     * Runs until the Canvas window is closed
     * 
     * @param numOfBalls number of balls to simulate bouncing, clamped between 5-30. 
     */
    public void boxBounce(int numOfBalls)
    {
        int count = Math.max(MIN_BALLS, Math.min(numOfBalls, MAX_BALLS));
 
        myCanvas.setVisible(true);
        drawBox();
 
        // create the balls, each with its own random position and color
        ArrayList<BoxBall> balls = new ArrayList<BoxBall>();
        Set<Color> usedColors = new HashSet<Color>();
        for (int i = 0; i < count; i++) {
            // keep the whole ball inside the walls, the ball's x,y is its upper left corner
            int x = randomBetween(box.getLeftWall() + 1, box.getRightWall() - BALL_DIAMETER - 1);
            int y = randomBetween(box.getTopWall() + 1, box.getBottomWall() - BALL_DIAMETER - 1);
            BoxBall ball = new BoxBall(x, y, BALL_DIAMETER, randomColor(usedColors), box, myCanvas);
            balls.add(ball);
            ball.draw();
        }
 
        // make them bounce
        while (myCanvas.isVisible()) {
            myCanvas.wait(TICK_DELAY);      // small delay
            for (BoxBall ball : balls) {
                ball.move();
            }
            drawBox();      // redraw the walls so they always stay clean
        }
 
        // window was closed, clear the balls so a later run starts fresh
        for (BoxBall ball : balls) {
            ball.erase();
        }
        drawBox();
    }
    
    /**
     * Draw the box in black. box.draw uses the canvas' current foreground color,
     * and each ball changes that color when it draws, so it is reset first.
     */
    private void drawBox()
    {
        myCanvas.setForegroundColor(Color.BLACK);
        box.draw();
    }
    
    /**
     * Make a random color that is not too close to white and that no other ball already has.
     * 
     * @param usedColors the colors already given to balls; the new color is added to it
     * @return a new random color
     */
    private Color randomColor(Set<Color> usedColors)
    {
        Color color;
        do {
            color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        } while (isTooLight(color) || usedColors.contains(color));
        usedColors.add(color);
        return color;
    }
    
    /**
     * Check whether a color is too bright to be seen well on a white background.
     * 
     * @param color the color to check
     * @return true if the color is too close to white
     */
    private boolean isTooLight(Color color)
    {
        double brightness = 0.299 * color.getRed() 
                          + 0.587 * color.getGreen() 
                          + 0.114 * color.getBlue();
        return brightness > MAX_BRIGHTNESS;
    }
    
    /**
     * @param min the smallest value allowed
     * @param max the largest value allowed
     * @return a random int from min to max, inclusive
     */
    private int randomBetween(int min, int max)
    {
        return min + rand.nextInt(max - min + 1);
    }
    
    /**
     * Simulate two bouncing balls
     */
    public void bounce()
    {
        int ground = 400;   // position of the ground line

        myCanvas.setVisible(true);

        // draw the ground
        myCanvas.setForegroundColor(Color.BLACK);
        myCanvas.drawLine(50, ground, 550, ground);

        // create and show the balls
        BouncingBall ball = new BouncingBall(50, 50, 16, Color.BLUE, ground, myCanvas);
        ball.draw();
        BouncingBall ball2 = new BouncingBall(70, 80, 20, Color.RED, ground, myCanvas);
        ball2.draw();

        // make them bounce
        boolean finished =  false;
        while (!finished) {
            myCanvas.wait(50);           // small delay
            ball.move();
            ball2.move();
            // stop once ball has travelled a certain distance on x axis
            if(ball.getXPosition() >= 550 || ball2.getXPosition() >= 550) {
                finished = true;
            }
        }
    }
}
