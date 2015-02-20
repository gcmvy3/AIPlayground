import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;


public class Drone extends Entity
{	
	private final int SPEED = 3;
	private final double MAX_HEALTH = 160;
	private final double DAMAGE = 5;
	private final double AGGRO_RANGE = 400;
	private final double FOOD_RANGE = 400;
	private final double SPEED_REDUCTION = 0.80;

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

		if(isDead)
		{
			g.setColor(Color.black);
		}

		g.fillRect((int)xPosition - (width / 2), (int)yPosition - (height / 2), width, height);

		if(isFighting)
		{
			outlineColor = Color.white;
		}
		else
		{
			outlineColor = Color.black;
		}
		
		g.setColor(outlineColor);
		g.drawRect((int)xPosition - (width / 2), (int)yPosition - (height / 2), width, height);

		if(hasFood)
		{
			drawFood(g);
		}
	}

	public void drawFood(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillRect((int)xPosition - 2, (int)yPosition - 2, 5, 5);
		g.setColor(Color.BLACK);
		g.drawRect((int)xPosition - 2, (int)yPosition - 2, 5, 5);
	}

	public void act()
	{
		isFighting = false;
		turnCounter++;

		if(!isDead)
		{
			queen = getQueen();

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

	public Queen getQueen()
	{
		Queen queen = null;

		for(Entity e : GamePanel.getEntities())
		{
			if(e instanceof Queen)
			{
				Queen possibleQueen = (Queen)e;

				if(possibleQueen.getFaction().equals(faction))
				{
					queen = possibleQueen;
				}
			}
		}

		return queen;
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
			double angleToTarget = calcAngleTo(queen);

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
			double damageModifier = random.nextDouble() * DAMAGE;
			
			enemy.takeDamage(DAMAGE + damageModifier);
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
		double angleToTarget = calcAngleTo(target);

		xVelocity = SPEED * Math.cos(angleToTarget);
		yVelocity = -SPEED * Math.sin(angleToTarget);
	}

	public double calcAngleTo(Entity target)
	{
		double targetAngleInRadians;

		double xDifference = getXDistanceFrom(target);
		double yDifference = getYDistanceFrom(target);

		if(xDifference < 0)
		{
			targetAngleInRadians = Math.PI + Math.atan(-yDifference / xDifference);
		}
		else if(xDifference > 0)
		{
			targetAngleInRadians = Math.atan(-yDifference / xDifference);
		}
		else
		{
			if(yDifference > 0)
			{
				targetAngleInRadians = Math.PI / 2;
			}
			else
			{
				targetAngleInRadians = -Math.PI / 2;
			}
		}

		return targetAngleInRadians;
	}

	public void randomMovement()
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

	public void bounceOffEdges()
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
}
