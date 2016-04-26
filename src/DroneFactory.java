
public class DroneFactory 
{
	// Predefined attributes of drones
	private static int speed;				// How fast the drone moves
	private static int maxHealth;			// How much health the drone has
	private static int baseDamage;			// How much damage the drone deals to other drones
	private static int aggroRange;			// The distance at which it will move to attack other drones
	private static int foodRange;			// The distance at which it will move to collect food
	private static double speedReduction; 	// The speed modifier when carrying something (food)
	
	public static Drone getDroneFromSeed(String seed)
	{
		speed = Integer.parseInt(seed.substring(0, 1), 36);
		maxHealth = Integer.parseInt(seed.substring(1, 2), 36);
		baseDamage = Integer.parseInt(seed.substring(2, 3), 36);
		aggroRange = Integer.parseInt(seed.substring(3, 4), 36);
		foodRange = Integer.parseInt(seed.substring(4, 5), 36);
		speedReduction = Integer.parseInt(seed.substring(5, 6), 36);
		
		return null; // TODO: finish this
	}
}
