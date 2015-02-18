import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

/*
 * This class is a modified JPanel that runs a game timer and draws the graphics
 */

public class GamePanel extends JPanel implements ActionListener, Runnable
{
	private static final long serialVersionUID = 1L;

	private Graphics g;

	private static int panelWidth;
	private static int panelHeight;
	
	private static int redCount;
	private static int blueCount;
	
	private static Timer timer;
	
	private static Controller controller;
	
	private static ArrayList<Entity> entities = new ArrayList<Entity>(); //Keeps track of all entities in the game

	public GamePanel(int x, int y)
	{	
		panelWidth = x;
		panelHeight = y;

		setPreferredSize(new Dimension(panelWidth, panelHeight));
		setFocusable(true);
		
		//add(new TopMenu());
		
		controller = new Controller();
		
		addKeyListener(controller);
		addMouseListener(controller);

		g = getGraphics();
		paint(g);
	}

	public void run()	//Starts the game timer. Call this to start the game
	{	
		resetWorld();
		
		timer = new Timer(10, this);
		timer.setInitialDelay(0);

		timer.start();
	}
	
	public static void initializeActors()
	{
		Queen redQueen = new Queen(400, 400, "red");
		Queen blueQueen = new Queen(1000, 400, "blue");
		
		addEntity(redQueen);
		addEntity(blueQueen);
	}
	
	public static void resetWorld()
	{
		entities = new ArrayList<Entity>();
		
		initializeActors();
	}
	
	public void actionPerformed(ActionEvent e) //This method is run every time the timer fires
	{   
		this.requestFocus();
		
		controller.update();
		
		for(int i = 0; i < entities.size(); i++)
		{
			Entity entity = entities.get(i);
			entity.act();
		}
		
		this.repaint(); //Calls paintComponent()
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		for(Entity entity : entities)
		{
			entity.draw(g);
		}
		
		g.setColor(Color.BLACK);
		g.drawString("Red:  " + redCount, 5, 20);
		g.drawString("Blue: " + blueCount, 5, 50);
	}
	
	public static ArrayList<Entity> getEntities()
	{
		return entities;
	}
	
	public static void addEntity(Entity e)
	{		
		if(e.getFaction().equals("red"))
		{
			redCount++;
		}
		else if(e.getFaction().equals("blue"))
		{
			blueCount++;
		}
		
		entities.add(e);
	}

	public static void removeEntity(Entity e)
	{
		if(e.getFaction().equals("red"))
		{
			redCount--;
		}
		else if(e.getFaction().equals("blue"))
		{
			blueCount--;
		}
		
		entities.remove(e);
	}

	public static int getPanelWidth()
	{
		return panelWidth;
	}

	public static int getPanelHeight()
	{
		return panelHeight;
	}
	
	public static int getRedCount()
	{
		return redCount;
	}
	
	public static int getBlueCount()
	{
		return blueCount;
	}
}
