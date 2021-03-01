package td_arc;

public class Base {

	boolean isRip;
	int id, x, y, width, height;
	
	public Base() {
		setIsRip(false);
		setId(-1);
		setX(-50);
		setY(-50);
		setWidth(18);
		setHeight(18);
	}
	
	public boolean getIsRip() {
		return this.isRip;
	}
	
	public int getId() {
		return id;
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
	
	public void setIsRip(boolean isRip) {
		this.isRip = isRip;
	}
	
	public void setId(int id) {
		this.id = id;
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
	
	public int getDistance(Tower tower, Enemy enemy) {
		int distance = 0;
		int tX = tower.getX();
		int tY = tower.getY();
		int eX = enemy.getX();
		int eY = enemy.getY();
		
		distance = (int) Math.sqrt((eY - tY) * (eY - tY) + (eX - tX) * (eX - tX));
		
		return distance;
	}
	
	public int randomInt(int min, int max) {
		int i = (int) ((Math.random() * (max - min + 1) + min));
		
		return i;
	}
}
