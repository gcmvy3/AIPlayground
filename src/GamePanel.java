import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

	private static ArrayList<Entity> entities = new ArrayList<Entity>(); //Keeps track of all entities in the game

	private static ArrayList<Faction> factions = new ArrayList<Faction>();
	
	public GamePanel(int x, int y)
	{	
		panelWidth = x;
		panelHeight = y;

		setPreferredSize(new Dimension(panelWidth, panelHeight));
		setFocusable(true);

		controller = new Controller();

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

	public static void resetWorld()
	{
		entities = new ArrayList<Entity>();

		initializeActors();
	}

	public static void initializeActors()
	{
		factions = new ArrayList<Faction>();
		
		factions.add(new Faction(Color.RED));
		factions.add(new Faction(Color.BLUE));
		
		Queen redQueen;
		Queen blueQueen;
		Queen greenQueen;

		if(greenFaction)
		{
			factions.add(new Faction(Color.GREEN));
			
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
		}

		this.repaint(); //Calls paintComponent()
	}

	public void spawnFood()
	{
		int bufferZone = 25;

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

		for(Entity entity : entities)
		{
			entity.draw(g);
		}
		
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
		entities.remove(e);
	}

	public static void togglePause()
	{
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
}
