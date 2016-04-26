import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

// Drones are created by Queens and will behave in a predetermined way
// Current Drone priorities:
// 1. Gather food
// 2. Combat
// 3. Wander

public class Drone extends Entity
{	
	// Predefined attributes of drones
	private final int SPEED = 2;					// How fast the drone moves
	private final int MAX_HEALTH = 160;				// How much health the drone has
	private final int BASE_DAMAGE = 5;				// How much damage the drone deals to other drones
	private final int AGGRO_RANGE = 250;			// The distance at which it will move to attack other drones
	private final int FOOD_RANGE = 250;				// The distance at which it will move to collect food
	private final double SPEED_REDUCTION = 0.80; 	// The speed modifier when carrying something (food)

	private int turnCounter;
	private double health = MAX_HEALTH;
	private boolean hasFood;
	private boolean isFighting;
	private boolean isDead;

	private Random random = new Random();

	private Drone enemy;

	private Queen queen;

	public Drone(double x, double y, Faction f)
	{
		xPosition = x;
		yPosition = y;

		faction = f;

		width = 12;
		height = 12;
		
		faction.addDrone(this);
	}

	public void draw(Graphics g)
	{
		Color outlineColor;
		
		g.setColor(faction.getColor());
		
		// If the drone is dead, color it solid black
		if(isDead)
		{
			g.setColor(Color.black);
		}

		g.fillRect((int)getXOrigin(), (int)getYOrigin(), (int)getWidth(), (int)getHeight());

		// If the drone is aggroed, give it a white outline
		if(isFighting)
		{
			outlineColor = Color.white;
		}
		else
		{
			outlineColor = Color.black;
		}
		
		g.setColor(outlineColor);
		g.drawRect((int)getXOrigin(), (int)getYOrigin(), (int)getWidth(), (int)getHeight());

		// If the drone is carrying food, draw a visual indication
		if(hasFood)
		{
			drawFood(g);
		}
	}

	public void drawFood(Graphics g)
	{
		int foodSize = 6;
		
		// Draws a small food icon in the center of the drone
		g.setColor(Color.WHITE);
		g.fillRect((int)xPosition - foodSize / 2, (int)yPosition - foodSize / 2, foodSize, foodSize);
		g.setColor(Color.BLACK);
		g.drawRect((int)xPosition - foodSize / 2, (int)yPosition - foodSize / 2, foodSize, foodSize);
	}

	public void act()
	{
		isFighting = false;
		turnCounter++;

		if(!isDead)
		{
			decideBehavior();

			bounceOffEdges();
			updatePosition();
		}
		else
		{
			if(turnCounter > 200)
			{
				destroy();
			}
		}
	}

	public void decideBehavior()
	{
		if(hasFood && queen != null)
		{
			bringFoodToQueen();
		}
		else
		{
			if(isNearFood())
			{
				goTowardsFood();
			}
			else if(isNearEnemy())
			{
				fight(enemy);
			}
			else
			{
				randomMovement();
			}
		}
	}

	public void bringFoodToQueen()
	{
		if(isTouching(queen))
		{
			queen.giveFood();
			hasFood = false;
		}
		else
		{
			double angleToTarget = getAngleTo(queen);

			xVelocity = (SPEED * Math.cos(angleToTarget)) * SPEED_REDUCTION;
			yVelocity = (-SPEED * Math.sin(angleToTarget)) * SPEED_REDUCTION;
		}
	}

	public boolean isNearEnemy()
	{
		boolean foundEnemy = false;
		
		for(Entity e : GamePanel.getEntities())
		{
			if(e instanceof Drone)
			{
				Drone d = (Drone) e;

				if(getDistanceFrom(d) <= AGGRO_RANGE && (!d.getFaction().equals(faction)) && (!d.isDead()))
				{
					if(!foundEnemy)
					{
						foundEnemy = true;
						enemy = d;
					}
					else
					{
						if(getDistanceFrom(d) < getDistanceFrom(enemy))
						{
							enemy = d;
						}
					}
				}
			}
		}
		return foundEnemy;
	}
	
	public boolean isNearFood()
	{
		// Returns true if there is food within a predetermined range
		for(Entity e : GamePanel.getEntities())
		{
			if(e instanceof Food)
			{
				if(getDistanceFrom(e) <= FOOD_RANGE)
				{
					return true;
				}
			}
		}
		return false;
	}

	public void fight(Drone enemy)
	{
		isFighting = true;
		moveTowards(enemy);

		if(getDistanceFrom(enemy) <= 6)
		{
			double damageModifier = random.nextDouble() * BASE_DAMAGE;
			
			enemy.takeDamage(BASE_DAMAGE + damageModifier);
		}
	}

	public void goTowardsFood()
	{
		Entity target = null;

		target = getClosestFood();

		if(target != null)
		{
			if(isTouching(target))
			{
				Food foodTarget = (Food)target;
				foodTarget.reduce();
				hasFood = true;
			}
			else
			{
				moveTowards(target);
			}
		}
	}

	public Entity getClosestFood()
	{
		Entity target = null;

		ArrayList<Entity> foodSources = new ArrayList<Entity>();

		for(Entity e : GamePanel.getEntities())
		{
			if(e instanceof Food)
			{
				foodSources.add(e);
			}
		}

		if(foodSources.size() != 0)
		{
			target = foodSources.get(0);

			for(Entity e : foodSources)
			{
				if(this.getDistanceFrom(e) < this.getDistanceFrom(target))
				{
					target = e;
				}
			}
		}

		return target;
	}

	public void moveTowards(Entity target)
	{
		double angleToTarget = getAngleTo(target);

		xVelocity = SPEED * Math.cos(angleToTarget);
		yVelocity = -SPEED * Math.sin(angleToTarget);
		
		//Adds a sort of wibbly effect
		//xVelocity += (random.nextDouble() * (SPEED * 2)) - SPEED;
		//yVelocity += (random.nextDouble() * (SPEED * 2)) - SPEED;
	}

	public void randomMovement() // Moves the drone in a random direction
	{
		if(turnCounter >= 40)
		{
			Random random = new Random();
			int randXVel = (random.nextInt(SPEED + SPEED + 1) - SPEED);
			int randYVel = (random.nextInt(SPEED + SPEED + 1) - SPEED);

			xVelocity = randXVel;
			yVelocity = randYVel;

			turnCounter = 0;
		}
	}

	public void bounceOffEdges() // Contains the drone within the game area
	{
		int panelWidth = GamePanel.getPanelWidth();
		int panelHeight = GamePanel.getPanelHeight();

		if(xPosition <= 0)
		{
			xPosition = 0;
			xVelocity *= -1;
		}
		else if(xPosition + width >= panelWidth)
		{
			xPosition = panelWidth - width;
			xVelocity *= -1;
		}
		if(yPosition <= 0)
		{
			yPosition = 0;
			yVelocity *= -1;
		}
		else if(yPosition + height >= panelHeight)
		{
			yPosition = panelHeight - height;
			yVelocity *= -1;
		}	
	}

	public void updatePosition()
	{
		xPosition += xVelocity;
		yPosition += yVelocity;
	}

	public void takeDamage(double d)
	{
		health -= d;

		if(health <= 0)
		{
			isDead = true;
		}
	}

	public void destroy()
	{
		GamePanel.removeEntity(this);
		faction.removeDrone(this);
	}

	public boolean isDead()
	{
		return isDead;
	}
	
	public Queen getQueen()
	{
		return queen;
	}
	
	public void setQueen(Queen q)
	{
		// Should be called when the drone is created
		queen = q;
	}
}
