import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;


public class Drone extends Entity
{	
	private final double SPEED = 2.5;
	private final double MAX_HEALTH = 160;
	private final double DAMAGE = 5;
	private final double AGGRO_RANGE = 100;

	private int turnCounter;
	private double health = MAX_HEALTH;
	private boolean hasFood;
	private boolean isFighting;
	private boolean isDead;

	private Random random = new Random();
	
	private Drone enemy;

	private Queen queen;

	public Drone(double x, double y, String faction)
	{
		xPosition = x;
		yPosition = y;

		this.faction = faction;

		width = 15;
		height = 15;
	}

	public void draw(Graphics g)
	{
		Color color;
		Color outlineColor;

		if(faction.equals("red"))
		{
			color = Color.RED;
		}
		else if(faction.equals("blue"))
		{
			color = Color.BLUE;
		}
		else
		{
			color = Color.BLACK;
		}

		if(isDead)
		{
			color = Color.black;
		}

		g.setColor(color);
		g.fillRect((int)xPosition - (width / 2), (int)yPosition - (height / 2), width, height);

		if(isFighting)
		{
			outlineColor = Color.cyan;
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
			if(isNearEnemy())
			{
				fight(enemy);
			}
			else
			{
				goTowardsFood();
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

			xVelocity = SPEED * Math.cos(angleToTarget);
			yVelocity = -SPEED * Math.sin(angleToTarget);
		}
	}

	public boolean isNearEnemy()
	{
		for(Entity e : GamePanel.getEntities())
		{
			if(e instanceof Drone)
			{
				Drone d = (Drone) e;

				if(getDistanceFrom(d) <= AGGRO_RANGE && (!d.getFaction().equals(faction)) && (!d.isDead()))
				{
					enemy = d;
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

		if(getDistanceFrom(enemy) <= 5)
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
		else
		{
			randomMovement();
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
			int randXVel = (random.nextInt(5)-2);
			int randYVel = (random.nextInt(5)-2);

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
	}

	public boolean isDead()
	{
		return isDead;
	}
}
