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
	private int speed = 2;					// How fast the drone moves
	private int maxHealth = 160;			// How much health the drone has
	private int baseDamage = 5;				// How much damage the drone deals to other drones
	private int aggroRange = 250;			// The distance at which it will move to attack other drones
	private int foodRange = 250;			// The distance at which it will move to collect food
	private double speedReduction = 0.80; 	// The speed modifier when carrying something (food)

	String seed;
	
	private int turnCounter;
	private double health = maxHealth;
	private boolean hasFood;
	private boolean isFighting;
	private boolean isDead;

	private Random random = new Random();

	private Drone enemy;

	protected Queen queen;
	
	public Drone(double x, double y, Queen q)
	{
		xPosition = x;
		yPosition = y;

		queen = q;

		width = 12;
		height = 12;
	}

	public void draw(Graphics g)
	{
		Color outlineColor;
		
		g.setColor(queen.getColor());
		
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
			queen.incrementFood();
			hasFood = false;
		}
		else
		{
			// The drone moves slower when carrying food
			double angleToTarget = getAngleTo(queen);

			xVelocity = (speed * Math.cos(angleToTarget)) * speedReduction;
			yVelocity = (-speed * Math.sin(angleToTarget)) * speedReduction;
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

				if(getDistanceFrom(d) <= aggroRange && (!d.getQueen().equals(queen)) && (!d.isDead()))
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
				if(getDistanceFrom(e) <= foodRange)
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
			double damageModifier = random.nextDouble() * baseDamage;
			
			enemy.takeDamage(baseDamage + damageModifier);
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

		xVelocity = speed * Math.cos(angleToTarget);
		yVelocity = -speed * Math.sin(angleToTarget);
		
		//Adds a sort of wibbly effect
		//xVelocity += (random.nextDouble() * (SPEED * 2)) - SPEED;
		//yVelocity += (random.nextDouble() * (SPEED * 2)) - SPEED;
	}

	public void randomMovement() // Moves the drone in a random direction
	{
		if(turnCounter >= 40)
		{
			Random random = new Random();
			int randXVel = (random.nextInt(speed + speed + 1) - speed);
			int randYVel = (random.nextInt(speed + speed + 1) - speed);

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
		queen.removeDrone(this);
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
