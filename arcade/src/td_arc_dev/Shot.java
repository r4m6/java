package td_arc_dev;

import java.awt.Color;
import java.awt.Graphics;

public class Shot extends Base {

	public Shot(Tower tower) {
		this.setName(tower.getName() + " shot");
		this.setX(tower.getX() + 9);
		this.setY(tower.getY() + 9);
		this.setWidth(4);
		this.setHeight(4);
		
	}
	
	public void paintShot(Graphics g) {
		if ( this.getName().equalsIgnoreCase("simple tower shot") ) {
			g.setColor(Color.blue);
			g.fillOval(getX(), getY(), getWidth(), getHeight());
		} else if ( this.getName().equalsIgnoreCase("grey tower shot") ) {
			g.setColor(new Color (102, 51, 0));//brown
			g.fillOval(getX(), getY(), getWidth() + 2, getHeight() + 2);
		}
		
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
