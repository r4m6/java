package td_arc_dev;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Tower extends Base {
	public int price, damage, speed, range, accuracy;
	public int bonusAcc;

	public JLabel label = new JLabel();
	
	List<Shot> shots = new ArrayList<Shot>();
	List<Integer> shotsToRm = new ArrayList<Integer>();
	
	public Tower() {
		
	}
	
	public Tower(String name) {
		
		//simple tower
		if (name.equalsIgnoreCase("simple tower")) {
			this.setName(name);
			this.setPrice(20);
			this.setDamage(10);
			this.setSpeed(1);
			this.setRange(150);
			this.setAccuracy(80);
			
			this.getLabel().setIcon(new ImageIcon(getClass().getResource("simpleTower.png")));
		}
		
		//grey tower
		if (name.equalsIgnoreCase("grey tower")) {
			this.setName(name);
			this.setPrice(70);
			this.setDamage(50);
			this.setSpeed(1);
			this.setRange(150);
			this.setAccuracy(70);
			
			this.getLabel().setIcon(new ImageIcon(getClass().getResource("greyTower.png")));
		}
		
		//bomb tower
		if (name.equalsIgnoreCase("bomb tower")) {
			this.setName(name);
			this.setPrice(100);
			this.setDamage(30);
			this.setSpeed(1);
			this.setRange(120);
			this.setAccuracy(60);
			
			this.getLabel().setIcon(new ImageIcon(getClass().getResource("bombTower.png")));
		
		}
	}
	
	public int getPrice() {
		return this.price;
	}
	
	public int getDamage() {
		return this.damage;
	}
	
	public int getSpeed() {
		return this.speed;
	}
	
	public int getRange() {
		return this.range;
	}
	
	public int getAccuracy() {
		return this.accuracy;
	}
	
	public int getBonusAcc() {
		return this.bonusAcc;
	}
	
	public JLabel getLabel() {
		return this.label;
	}
	
	public List<Shot> getShots() {
		return this.shots;
	}
	
	public List<Integer> getShotsToRm() {
		return this.shotsToRm;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public void setRange(int range) {
		this.range = range;
	}
	
	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}
	
	public void setBonusAcc(int bonusAcc) {
		this.bonusAcc = bonusAcc;
	}
	
	public void show() {
		getLabel().setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		getLabel().setVisible(true);
	}
	
	
	public void hide() {
		getLabel().setVisible(false);
	}
	
	public void addShot() {
		//adds a shot near the tower
		Shot shot = new Shot(this);
		getShots().add(shot);
		shot.setId(getShots().indexOf(shot));
	}
	
	public void rmShots() {
		List<Integer> idsToRm = new ArrayList<Integer>();
			
		int i = 0;
		for ( Shot shot : getShots() ) {
			shot.setId(getShots().size() - i - 1);
			i++;
		}
		
		for ( Shot shot : getShots() ) {
			if ( shot.getIsRip() ) {
				idsToRm.add(shot.getId());
			}
			
		}
	
		for ( int id : idsToRm ) {
			try {
				getShots().remove(id);
			} catch(Exception e) {
				e.printStackTrace();
			}
				
		}
		
	}
	
	public void upgrade(TowerDefenseArcade td_arc) {
		if ( td_arc.getCoins() >= (this.getPrice()) ) {
			td_arc.setCoins(td_arc.getCoins() - (this.getPrice()));
			
			if ( this.getName().equalsIgnoreCase("simple tower") ) {
				this.setDamage(this.getDamage() + 10);
				this.setSpeed(this.getSpeed() + 2);
				this.setRange(this.getRange() + 10);
				this.setAccuracy(this.getAccuracy() + 1);
			} else if ( this.getName().equalsIgnoreCase("grey tower") ) {
				this.setDamage(this.getDamage() + (this.getDamage()/2));
				this.setSpeed(this.getSpeed() + 1);
				this.setRange(this.getRange() + 1);
				this.setAccuracy(this.getAccuracy() + 1);
			} else if ( this.getName().equalsIgnoreCase("bomb tower") ) {
				this.setDamage(this.getDamage() + 15);
				this.setSpeed(this.getSpeed() + 1);
				this.setRange(this.getRange() + 3);
				this.setAccuracy(this.getAccuracy() + 1);
			}
						
			this.setPrice(this.getPrice() + (this.getPrice()/2));
		} else
			System.out.println("can't afford");

	}
	
	//moves shot towards enemy
	public void mvShot(TowerDefenseArcade td_arc, Shot shot, Enemy enemy) {
		int distance = getDistance(this, enemy); 
		int targetX = enemy.getX() + 6;	//offset to not hit enemyXY, but its center
		int targetY = enemy.getY() + 4;

		if ( distance < this.getRange() ) {

			if ( shot.getX() < targetX ) 
				shot.setX(shot.getX() + 1);
			else 
				shot.setX(shot.getX() - 1);
								
			if ( shot.getY() < targetY ) 
				shot.setY(shot.getY() + 1);
			else 
				shot.setY(shot.getY() - 1);
			
			if ( (shot.getX() == targetX) && (shot.getY() == targetY) ) {
				//checks if a random number is in the bigger half between accuracy and 100 f.e. if its >=90 if accuracy is 80 to make a hit
				if (randomInt(1, 100) <= (this.getAccuracy() + this.getBonusAcc())) {
					setBonusAcc(0);
					int oldHealth = enemy.getHealth();
					enemy.setHealth(enemy.getHealth() - (this.getDamage() - (randomInt(0, (this.getDamage() / 2)))));	//the damage of every hit is minored by max 50%
					td_arc.setScore(td_arc.getScore() + (oldHealth - enemy.getHealth())); 	//damage delt is added to score
					//TODO:green affordable, red isnt
					
					//additional area effect for bomb tower
					if (this.getName().equalsIgnoreCase("bomb tower")) {
						shot.setIsExploding(true);
						dealAreaDamage(shot.getX(), shot.getY(), td_arc);
					}
				//shot missed, next one gets more accuracy
				} else {
					setBonusAcc(getBonusAcc() + 1);
				}
				
				shot.setIsRip(true);
			}
				
		} else {
			shot.setIsRip(true);
		}
		
	}
	
	public void shootSave(TowerDefenseArcade td_arc) {
		td_arc.resetEnemyIds();
		int id = td_arc.getEnemys().get(getClosestEnemyIdToPlayer(td_arc.getEnemys())).getId();
		
		for ( Shot shot : getShots() )
			mvShot(td_arc, shot, td_arc.getEnemys().get(id));
	}
	
	public void shootNear(TowerDefenseArcade td_arc) {
		td_arc.resetEnemyIds();
		int id = td_arc.getEnemys().get(getClosestEnemyIdToPlayer(td_arc.getEnemys())).getId();

		for ( Shot shot : getShots() )
			mvShot(td_arc, shot, td_arc.getEnemys().get(id));
	}
	
	public void setTower(int x, int y) {
		setX(x);
		setY(y);
	}

	//returns closest enemy to player-tower in range of tower
	public int getClosestEnemyIdToPlayer(List<Enemy> enemys) {
		int id = 0;
		int steps = 0;
		int stepsToTower[] = new int[enemys.size()];
		boolean isInRange[] = new boolean[enemys.size()];

		List<Integer> ids = new ArrayList<Integer>();
		int distance = 0;
		
		int count = 0;
		for (Enemy enemy : enemys) {
		
			ids.add(enemy.getId());
			stepsToTower[count] = enemy.getStepsToTower();
			distance = getDistance(this, enemy);
			
			if ( distance > getRange() ) {
				isInRange[count] = false;
			} else if ( distance <= getRange() ) {
				isInRange[count] = true;
			} 
				
			count++;
		}

		count = 0;
		for ( int enemyId : ids) {
			if ( isInRange[count] && id == 0 ) {
				id = enemyId;
				steps = stepsToTower[count];
			} else if ( isInRange[count] && (stepsToTower[count] > steps)) {
				id = enemyId;
				steps = stepsToTower[count];
			}
			
			count++;
		}
		
		return id;
	}
	
	//returns closest enemy to executing tower
	public int getClosestEnemyIdToTower(List<Enemy> enemys) {
		int id = 0;
		int distance = 0;
		
		for (Enemy enemy : enemys) {
			if (distance == 0) {
				id = enemy.getId();
				distance = getDistance(this, enemy);
			} else if ( getDistance(this, enemy) <= getRange() && getDistance(this, enemy) < distance) {
				id = enemy.getId();
				distance = getDistance(this, enemy);
			}
			
		}
		
		return id;
	}
	
	public void dealAreaDamage(int x, int y, TowerDefenseArcade td_arc) {

		for (Enemy enemy : td_arc.getEnemys()) {
			if ((Math.pow((enemy.getX() - x),2) + Math.pow((enemy.getY() - y), 2)) < (Math.pow(50, 2))) {
				int oldHealth = enemy.getHealth();
				enemy.setHealth(enemy.getHealth() - getDamage());
				td_arc.setScore(td_arc.getScore() + (oldHealth - enemy.getHealth()));
				
				System.out.println(true);
			}
		}
	}
	
}

