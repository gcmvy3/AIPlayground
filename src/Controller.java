import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/*
 * 	This class is used to listen for input from the keyboard and react accordingly
 */

public class Controller implements KeyListener, MouseListener
{
	// A boolean array that keeps track of which keys are currently pressed 
	boolean[] keys = new boolean[255];

	//	-Assign ASCII key codes to variables to make the code more readable

	final int W = 87;
	final int A = 65;
	final int S = 83;
	final int D = 68;

	final int ENTER = 10;

	private String entityType = "Food";

	// Registers when a key is pressed or released and updates the boolean array
	public void keyPressed(KeyEvent e) 
	{
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) 
	{
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e) {}

	// Handles mouse input
	public void mouseClicked(MouseEvent event)
	{
		if(entityType.equals("Food"))
		{
			GamePanel.addEntity(new Food(event.getX(), event.getY()));
		}
		else if(entityType.equals("Drone"))
		{
			GamePanel.addEntity(new Drone(event.getX(), event.getY(), new Faction(Color.RED)));
		}
		else if(entityType.equals("Queen"))
		{
			GamePanel.addEntity(new Queen(event.getX(), event.getY(), new Faction(Color.RED)));
		}
	}

	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}

	// Checks if important keys are pressed and performs the appropriate action if they are
	// This function is called from GamePanel's actionPerformed() method
	public void update()
	{	
		if(keys[ENTER])
		{
			GamePanel.resetWorld();
		}
	}
	
	public void setEntityType(String s)
	{
		entityType = s;
	}
}
