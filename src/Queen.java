import java.awt.Color;
import java.awt.Graphics;


public class Queen extends Entity
{
	private int foodCount = 5;

	public Queen(double xPos, double yPos, String faction)
	{
		xPosition = xPos;
		yPosition = yPos;

		this.faction = faction;

		width = 30;
		height = 30;
		
		spawnDrones();
	}

	public void draw(Graphics g)
	{
		if(faction.equals("red"))
		{
			g.setColor(Color.RED);	
		}
		else if(faction.equals("blue"))
		{
			g.setColor(Color.BLUE);
		}

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
		if(foodCount > 2)
		{
			Drone newDrone = new Drone(xPosition, yPosition, faction);

			GamePanel.addEntity(newDrone);

			foodCount -= 2;
		}
		else
		{
			if(faction.equals("red"))
			{
				if(GamePanel.getRedCount() <= 1)
				{
					Drone newDrone = new Drone(xPosition, yPosition, faction);

					GamePanel.addEntity(newDrone);

				}
			}
			else if(faction.equals("blue"))
			{
				if(GamePanel.getBlueCount() <= 1)
				{
					Drone newDrone = new Drone(xPosition, yPosition, faction);

					GamePanel.addEntity(newDrone);

				}
			}
		}
	}
	
	public void giveFood()
	{
		foodCount++;
	}
}
