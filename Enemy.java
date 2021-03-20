package td_arc_dev;

public class Enemy extends Base {

	public int health;
	
	public Enemy() {
		this.x = 700;
		this.y = randomInt(450, 520);
		this.width = 10;
		this.height = 10;
		this.health = 200;
	}
	
	public void paintEnemy(java.awt.Graphics g) {
		g.setColor(java.awt.Color.red);
		g.fillOval(getX(), getY(), getWidth(), getHeight());
	}

	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public void setHealth(int newHealth) {
		this.health = newHealth;
	}
	
	//returns true if coord isOutOfMap
	public boolean isOutOfMap(int x, int y) {
		if (x >= 550 && (y >= 450 && y <= 520)) {							//a
			return false;
		} else if ((x >= 550 && x <= 620) && (y >= 50 && y <= 520)) {		//b
			return false;
		} else if ((x >= 100 && x <= 620) && (y >= 50 && y <= 120)) {		//c
			return false;
		} else if ((x >= 50 && x <= 120) && (y >= 50 && y <= 550))	{		//d
			return false;
		} else if ((x >= 50 && x <= 450) && (y >= 550 && y <= 620))	{		//e
			return false;
		} else if ((x >= 380 && x <= 450) && (y >= 200 && y <= 550))	{	//f
			return false;
		} else if ((x >= 200 && x <= 380) && (y >= 200 && y <= 270))	{	//g
			return false;
		} else if ((x >= 200 && x <= 270) && (y >= 200 && y <= 450))	{	//h
			return false;
		}
		
		return true;
	}
	
	public int getStepsToTower() {
		int stepsToTower = 0;
		
		if (getX() >= 550 && (getY() >= 450 && getY() <= 520)) {							//a
			stepsToTower += 1;
		} else if ((getX() >= 550 && getX() <= 620) && (getY() >= 50 && getY() <= 520)) {		//b
			stepsToTower += 2;
		} else if ((getX() >= 100 && getX() <= 620) && (getY() >= 50 && getY() <= 120)) {		//c
			stepsToTower += 3;
		} else if ((getX() >= 50 && getX() <= 120) && (getY() >= 50 && getY() <= 550))	{		//d
			stepsToTower += 4;
		} else if ((getX() >= 50 && getX() <= 450) && (getY() >= 550 && getY() <= 620))	{		//e
			stepsToTower += 5;
		} else if ((getX() >= 380 && getX() <= 450) && (getY() >= 200 && getY() <= 550))	{	//f
			stepsToTower += 6;
		} else if ((getX() >= 200 && getX() <= 380) && (getY() >= 200 && getY() <= 270))	{	//g
			stepsToTower += 7;
		} else if ((getX() >= 200 && getX() <= 270) && (getY() >= 200 && getY() <= 450))	{	//h
			stepsToTower += 8;
		}
		
		return stepsToTower;
	}
	
	public int moveLeft() {
		return this.x--;
	}
	
	public int moveRight() {
		return this.x++;
	}
	
	public int moveUp() {
		return this.y--;
	}
	
	public int moveDown() {
		return this.y++;
	}
	
}
