package td_arc;

import java.awt.Color;
import java.awt.Graphics;

public class Shot extends Base {

	public Shot(Tower tower) {
		this.setX(tower.getX() + 9);
		this.setY(tower.getY() + 9);
		this.setWidth(4);
		this.setHeight(4);
	}
	
	public void paintShot(Graphics g) {
		g.setColor(Color.blue);
		g.fillOval(getX(), getY(), getWidth(), getHeight());
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void reset(Tower tower) {
		setX(tower.getX() + 9);
		setY(tower.getY() + 9);
	}

}
