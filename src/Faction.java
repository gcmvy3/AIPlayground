import java.awt.Color;
import java.util.ArrayList;


public class Faction
{
	private Color color;
	
	private Queen queen;
	
	private ArrayList<Drone> drones = new ArrayList<Drone>();
	
	public Faction(Color c)
	{
		color = c;
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
		return color.toString();
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
