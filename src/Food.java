import java.awt.Color;
import java.awt.Graphics;

// Food is spawned on the map, retrieved by drones, and brought to the queen

public class Food extends Entity
{
	// The amount of food each node provides
	private int foodValue = 5;
	
	public Food(double x, double y)
	{
		xPosition = x;
		yPosition = y;

		width = foodValue + 3;
		height = foodValue + 3;
	}

	public void draw(Graphics g)
	{
		// Food is drawn as a rectangle with a black outline
		g.setColor(Color.BLACK);	
		g.drawRect((int)xPosition - (width / 2), (int)yPosition - (height / 2), width, height);
	}
	
	public void reduce()
	{
		// Called when a drone takes food from the node
		foodValue--;
		width = foodValue + 3;
		height = foodValue + 3;
		
		if(foodValue == 0)
		{
			destroy();
		}
	}
	
	public void destroy()
	{
		GamePanel.removeEntity(this);
	}
}
