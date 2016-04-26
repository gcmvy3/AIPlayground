import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


// When provided with food, a queen will spawn a drone
// Queens currently cannot be killed

public class Queen extends Drone
{	
	String factionName;
	
	private Color color;	// Color of the queen's faction, extends to drones
	
	private ArrayList<Drone> drones = new ArrayList<Drone>();
	
	// Amount of food the queen has stockpiled. Each food will spawn one drone
	private int foodCount = 5;

	public Queen(double xPos, double yPos, Color c, String name)
	{
		super(xPos, yPos, null);
		
		queen = this;
		
		xPosition = xPos;
		yPosition = yPos;

		width = 30;
		height = 30;
		
		color = c;
		
		factionName = name;
		
		spawnDrones();
	}

	public void draw(Graphics g)
	{
		g.setColor(color);

		g.fillRect((int)xPosition - (width / 2), (int)yPosition - (height / 2), width, height);

		g.setColor(Color.BLACK);
		g.drawRect((int)xPosition - (width / 2), (int)yPosition - (height / 2), width, height);
	}

	public void act()
	{
		spawnDrones();
	}

	public void spawnDrones()
	{
		//Spawn a drone if the foodcount is above a certain threshold
		if(foodCount > 2)
		{
			Drone newDrone = new Drone(xPosition, yPosition, this);
			GamePanel.addEntity(newDrone);
			drones.add(newDrone);

			foodCount -= 2;
		}
		else
		{
			if(getDroneCount() < 4)
			{
				Drone newDrone = new Drone(xPosition, yPosition, this);
				GamePanel.addEntity(newDrone);
				drones.add(newDrone);
			}
		}
	}
	
	// Called when a drone brings food to the queen
	public void incrementFood()
	{
		foodCount++;
	}
	
	public void addDrone(Drone d)
	{
		drones.add(d);
	}
	
	public void removeDrone(Drone d)
	{
		drones.remove(d);
	}
	
	public String getName()
	{
		return factionName;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public int getDroneCount()
	{
		return drones.size();
	}
}
