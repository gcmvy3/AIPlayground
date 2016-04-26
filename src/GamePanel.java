import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

	private static boolean foodSpawn;
	private static boolean isPaused;
	private static boolean greenFaction;

	private static Random random = new Random();

	private static Timer timer;

	private static Controller controller;

	//Keeps track of all entities in the game
	private static ArrayList<Entity> entities = new ArrayList<Entity>(); 

	// Entities that will be deleted at the end of the step
	private static List<Entity> entitiesToRemove = new ArrayList<Entity>();

	private static ArrayList<Faction> factions = new ArrayList<Faction>();

	
	public GamePanel(int x, int y)
	{	
		panelWidth = x;
		panelHeight = y;

		setPreferredSize(new Dimension(panelWidth, panelHeight));
		setFocusable(true);

		controller = new Controller(); // For handling I/O

		addKeyListener(controller);
		addMouseListener(controller);

		setBackground(Color.LIGHT_GRAY);
		
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

	// Call this to initialize or reinitialize the game world
	public static void resetWorld()
	{
		entities = new ArrayList<Entity>();

		factions = new ArrayList<Faction>();

		factions.add(new Faction("Red", Color.RED));
		factions.add(new Faction("Blue", Color.BLUE));
		
		initializeActors();
		AIPlayground.topMenu.updateFactions();
	}

	public static void initializeActors()
	{
		Queen redQueen;
		Queen blueQueen;
		Queen greenQueen;

		if(greenFaction)
		{
			factions.add(new Faction("Green", Color.GREEN));

			redQueen = new Queen(20, 600, factions.get(0));
			blueQueen = new Queen(panelWidth - 20, 600, factions.get(1));
			greenQueen = new Queen(panelWidth / 2, 20, factions.get(2));
			addEntity(greenQueen);
		}
		else
		{
			redQueen = new Queen(20, 300, factions.get(0));
			blueQueen = new Queen(panelWidth - 20, 300, factions.get(1));
		}

		addEntity(redQueen);
		addEntity(blueQueen);
	}


	public void actionPerformed(ActionEvent e) //This method is run every time the timer fires
	{   
		this.requestFocus();

		controller.update();


		if(!isPaused)
		{
			if(foodSpawn)
			{
				spawnFood();
			}

			for(int i = 0; i < entities.size(); i++)
			{
				Entity entity = entities.get(i);
				
				entity.act();
			}

			for(Entity entity : entitiesToRemove)
			{
				entities.remove(entity);
			}

			entitiesToRemove = new ArrayList<Entity>();
		}

		this.repaint(); //Calls paintComponent()
	}

	public void spawnFood()
	{
		int bufferZone = 25; // Prevents food from spawning too close to the edge

		if(random.nextBoolean())
		{
			int xPos = random.nextInt(panelWidth - 2 * bufferZone) + bufferZone;
			int yPos = random.nextInt(panelHeight - 2 * bufferZone) + bufferZone;

			addEntity(new Food(xPos, yPos));
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		List<Entity> entitiesToDraw = new ArrayList<Entity>(entities);

		for(Entity entity : entitiesToDraw)
		{
			entity.draw(g);
		}

		drawScoreBoard(g);
	}

	// Draws a "scoreboard" in the corner that displays the drone count for each faction
	public void drawScoreBoard(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.drawLine(5, 25, 70, 25);

		for(int i = 0; i < factions.size(); i++)
		{
			Faction f = factions.get(i);

			g.setColor(f.getColor());
			g.fillRect(5, 25 * (i + 1), 25, 25);
			g.setColor(Color.BLACK);
			g.drawRect(5, 25 * (i + 1), 25, 25);
			g.drawString("" + f.getDroneCount(), 35, 25 * (i + 2) - 5);

			g.drawLine(5, 25 * (i + 2), 70, 25 * (i + 2));
		}
	}
	
	public static ArrayList<Entity> getEntities()
	{
		return entities;
	}

	public static void addEntity(Entity e)
	{		
		entities.add(e);
	}

	public static void removeEntity(Entity e)
	{
		entitiesToRemove.add(e);
	}
	
	public static ArrayList<Faction> getFactions()
	{
		return factions;
	}

	public static void togglePause()
	{
		// Pauses and unpauses the game
		if(isPaused)
		{
			isPaused = false;
		}
		else
		{
			isPaused = true;
		}
	}

	public static void toggleFoodSpawn()
	{
		// Toggles whether food randomly spawns around the map
		if(foodSpawn)
		{
			foodSpawn = false;
		}
		else
		{
			foodSpawn = true;
		}
	}

	public static void toggleGreenFaction()
	{
		// Adds an optional third faction
		if(greenFaction)
		{
			greenFaction = false;
			resetWorld();
		}
		else
		{
			greenFaction = true;
			resetWorld();
		}
	}

	public static int getPanelWidth()
	{
		return panelWidth;
	}

	public static int getPanelHeight()
	{
		return panelHeight;
	}
	
	public static int getNumFactions()
	{
		return factions.size();
	}
	
	public static Faction createFaction(Color c, String s)
	{
		Faction f = new Faction(s, c);
		factions.add(f);
		AIPlayground.topMenu.updateFactions();	
		return f;
	}
}
