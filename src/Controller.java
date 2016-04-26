import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

/*
 * 	This class is used to listen for input from the keyboard and react accordingly
 */

public class Controller implements KeyListener, MouseListener
{
	Random random = new Random();
	
	// A boolean array that keeps track of which keys are currently pressed 
	boolean[] keys = new boolean[255];

	// Assign ASCII key codes to variables to make the code more readable
	final int ENTER = 10;

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
		// Adds an instance of the currently selected entity
		if(TopMenu.entityType.equals("Food"))
		{
			GamePanel.addEntity(new Food(event.getX(), event.getY()));
		}
		else if(TopMenu.entityType.equals("Drone"))
		{
			GamePanel.addEntity(new Drone(event.getX(), event.getY(), TopMenu.currentFaction));
		}
		else if(TopMenu.entityType.equals("Queen"))
		{
			// Generates a new faction with a random color and corresponding name
			// Then adds a queen of that faction
			
			int r = random.nextInt(255);
			int g = random.nextInt(255);
			int b = random.nextInt(255);
			
			Color randomColor = new Color(r, g, b);
			
			int factionNumber = GamePanel.getNumFactions() + 1;
			
			String factionName = "Faction" + factionNumber;
			
			GamePanel.addEntity(new Queen(event.getX(), event.getY(), GamePanel.createFaction(randomColor, factionName)));
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
}
