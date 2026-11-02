import java.awt.*;
import java.awt.geom.*;
import java.util.Random;

/**
 * Class BoxBall - a graphical ball that moves similar to the ball in PONG.
 * It bounces around the confines of a box as defined by the Box class.
 * Details of movement are determined by the ball itself. It will move to
 * the right when xspeed is positive and to the left when xspeed is negative.
 * Due to the way computer graphics are drawn using awt, the ball moves
 * DOWN when yspeed is positive and UP when yspeed is negative.
 * 
 * It is the ball's responsibility to determine if it has hit a wall and 
 * to reverse direction
 *
 * This movement can be initiated by repeated calls to the "move" method.
 * 
 * @author Michael Kölling (mik)
 * @author David J. Barnes
 * @author Bruce Quig
 * @author William Crosbie
 * @author Mustafa Has
 *
 * @version 2026.11.01
 */

public class BoxBall
{
    private static final int MAX_SPEED = 7;            // largest speed per tick
    private static final int WALL_OFFSET = 1;          // gap between ball and wall
    private static final Random rand = new Random();   // random number generator
    
    private Box myBox;

    private Ellipse2D.Double circle;    // represents the ball
    private Color color;        // color of the ball (can be rgb value)
    private int diameter;       // width of ball in number of pixels
    private int xPosition;      // horizontal position of bounding square
    private int yPosition;      // vertical position of bounding square
    private Canvas canvas;
    private int ySpeed;         // vertical speed
    private int xSpeed;         // horizontal speed

    /**
     * Constructor for objects of class BoxBall
     *
     * @param xPos  the horizontal coordinate of the ball
     * @param yPos  the vertical coordinate of the ball
     * @param ballDiameter  the diameter (in pixels) of the ball
     * @param ballColor  the color of the ball
     * @param box  the bounding box (where the ball will bounce)
     * @param drawingCanvas  the canvas to draw this ball on
     */
    public BoxBall(int xPos, int yPos, int ballDiameter, Color ballColor,
                        Box box, Canvas drawingCanvas)
    {
        myBox = box;
        
        color = ballColor;
        diameter = ballDiameter;

        canvas = drawingCanvas;
        
        xPosition = Math.max(minX(), Math.min(xPos, maxX()));
        yPosition = Math.max(minY(), Math.min(yPos, maxY()));
 
        xSpeed = randomSpeed();
        ySpeed = randomSpeed();

    }

    /**
     * Draw this ball at its current position onto the canvas.
     **/
    public void draw()
    {
        canvas.setForegroundColor(color);
        canvas.fillCircle(xPosition, yPosition, diameter);
    }

    /**
     * Erase this ball at its current position.
     **/
    public void erase()
    {
        canvas.eraseCircle(xPosition, yPosition, diameter);
    }    

    /**
     * Move this ball according to its position and speed and redraw.
     **/
    public void move()
    {
        // remove from canvas at the current position
        erase();
            
        // compute new position
        xPosition += xSpeed;
        yPosition += ySpeed;
  
        // figure out if it has hit the left or right wall
        if (xPosition < minX()) {
            xPosition = minX() + (minX() - xPosition);   // bounce off left wall
            xSpeed = -xSpeed;
        }
        else if (xPosition > maxX()) {
            xPosition = maxX() - (xPosition - maxX());   // bounce off right wall
            xSpeed = -xSpeed;
        }
        
        // figure out if it has hit the top or bottom wall
        if (yPosition < minY()) {
            yPosition = minY() + (minY() - yPosition);   // bounce off top wall
            ySpeed = -ySpeed;
        }
        else if (yPosition > maxY()) {
            yPosition = maxY() - (yPosition - maxY());   // bounce off bottom wall
            ySpeed = -ySpeed;
        }
        
        draw();
    }    

    /**
     * return the horizontal position of this ball
     */
    public int getXPosition()
    {
        return xPosition;
    }

    /**
     * return the vertical position of this ball
     */
    public int getYPosition()
    {
        return yPosition;
    }

    /**
     * Pick a random speed for one axis.
     * 
     * @return a value from -7 to 7, never 0
     */
    private int randomSpeed()
    {
        int speed = rand.nextInt(MAX_SPEED) + 1;
        if (rand.nextBoolean()) {
            speed = -speed;
        }
        return speed;
    }
 
    /**
     * @return the smallest x the ball may have
     */
    private int minX()
    {
        return myBox.getLeftWall() + WALL_OFFSET;
    }
 
    /**
     * The ball's x is its left edge, so the limit on the right is offset
     * by the diameter to keep the whole ball inside the wall.
     * 
     * @return the largest x the ball may have
     */
    private int maxX()
    {
        return myBox.getRightWall() - diameter - WALL_OFFSET;
    }
 
    /**
     * @return the smallest y the ball may have
     */
    private int minY()
    {
        return myBox.getTopWall() + WALL_OFFSET;
    }
 
    /**
     * The ball's y is its top edge, so the limit at the bottom is offset
     * by the diameter to keep the whole ball inside the wall.
     * 
     * @return the largest y the ball may have
     */
    private int maxY()
    {
        return myBox.getBottomWall() - diameter - WALL_OFFSET;
    }
}
