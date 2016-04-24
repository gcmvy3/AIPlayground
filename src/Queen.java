import java.awt.Color;
import java.awt.Graphics;


// When provided with food, a queen will spawn a drone
// Queens currently cannot be killed

public class Queen extends Entity
{
	// Amount of food the queen has stockpiled. Each food will spawn one drone
	private int foodCount = 5;

	public Queen(double xPos, double yPos, Faction f)
	{
		xPosition = xPos;
		yPosition = yPos;

		faction = f;

		width = 30;
		height = 30;
		
		spawnDrones();
	}

	public void draw(Graphics g)
	{
		g.setColor(faction.getColor());

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
			Drone newDrone = new Drone(xPosition, yPosition, faction);
			newDrone.setQueen(this);
			GamePanel.addEntity(newDrone);

			foodCount -= 2;
		}
		else
		{
			if(faction.getDroneCount() < 4)
			{
				Drone newDrone = new Drone(xPosition, yPosition, faction);
				newDrone.setQueen(this);
				GamePanel.addEntity(newDrone);
			}
		}
	}
	
	// Called when a drone brings food to the queen
	public void giveFood()
	{
		foodCount++;
	}
}
