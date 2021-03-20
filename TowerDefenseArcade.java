package td_arc_dev;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TowerDefenseArcade {
	
	public boolean isHighscore, isOver  = false;
	
	//TODO:int is way to small for score, think of the coins, too...
	public int 	score, coins, wave, time, sec, min;
	
	List<Enemy> enemys = new ArrayList<Enemy>();
	List<Tower> towers = new ArrayList<Tower>();
	
	public TowerDefenseArcade() {
		
	}
	
	public String getManual() {
		String manual =
				"welcome to td_arc"
				+ "\nclick on the grid to choose a field"
				+ "\npress b to buy the selected tower"
				+ "\non the bottom right you can see the selected tower"
				+ "\nif the selected field is already occupied you can upgrade the tower"
				+ "\nuse arrows to switch buy/upgrade options"
				+ "\npress p|space|enter or click menu on the top left to pause"
				+ "\npress esc to end game"
				+ "\ngood luck :-)";
		
		return manual;
	}
	
	public void paintHighscore(Graphics g, Frame frame) {
		g.setColor(Color.green);
		g.fillRect(0, 0, 1000, 1000);
		
		g.setColor(Color.black);
		g.fillRect(0, 0, 1000, 70);
		g.fillRect(220, 20, 6, 700);
		g.fillRect(370, 20, 6, 700);
		g.fillRect(480, 20, 6, 700);
		g.fillRect(70, 165, 700, 8);
		g.fillRect(70, 255, 700, 6);
		g.fillRect(70, 360, 700, 6);
		g.fillRect(70, 430, 700, 5);
		g.fillRect(70, 505, 700, 5);
		g.fillRect(70, 570, 700, 4);
		g.fillRect(70, 635, 700, 3);
		g.setColor(Color.white);
		g.drawRect(179, 9, 387, 104);
		
		
		g.setColor(Color.black);
		g.setFont(new Font("Comic", Font.BOLD + Font.PLAIN, 20));
		String[] scoretable = {"no scores avilable atm"," "," "," "," "," "," "," "};
		int scoreX = 230; int rankY = 150; int waveY = 130;
		try {
			scoretable = getHighscoreStr();
		} catch (IOException e) {
			e.printStackTrace();
			g.drawString("no scores available atm", 75, 75);
		}
		
		int i = 0;
		for ( String s : scoretable ) {
			if (i==1) {rankY = 248; waveY = rankY - 20;}
			if (i==2) {rankY = 350; waveY = rankY - 20;}
			if (i==3) {rankY = 420; waveY = rankY - 20;}
			if (i==4) {rankY = 500; waveY = rankY - 20;}
			if (i==5) {rankY = 555; waveY = rankY - 20;}
			if (i==6) {rankY = 625; waveY = rankY - 20;}
			if (i==7) {
				g.setFont(new Font("Comic",Font.BOLD + Font.PLAIN, 12));
				rankY = 658;
				waveY = rankY - 10;
			}
			
			g.drawString(s.split(";")[0], 80, rankY);
			scoreX = frame.getScoreX(s.split(";")[1]);
			g.drawString(s.split(";")[1], scoreX, rankY);
			g.setColor(Color.blue);
			g.drawString("wave: " + s.split(";")[2], 490, waveY);
			g.setColor(Color.black);
			g.drawString(s.split(";")[3], 390, rankY);
			g.drawString(s.split(";")[4], 490, rankY);
			

			i++;
		}
		
	}

	public boolean getIsHighscore() {
		return this.isHighscore;
	}
	
	public boolean getIsOver() {
		return this.isOver;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public int getWave() {
		return this.wave;
	}
	
	public int getCoins() {
		return this.coins;
	}
	
	public int getTime() {
		return this.time;
	}
	
	public int getSec() {
		return this.sec;
	}
	
	public int getMin() {
		return this.min;
	}
	
	public void setIsHighscore(boolean isHighscore) {
		 this.isHighscore = isHighscore;
	}
	
	public void setIsOver(boolean isOver) {
		 this.isOver = isOver;
	}
	
	public void setScore(int score) {
		 this.score = score;
	}
	
	public void setWave(int wave) {
		 this.wave = wave;
	}
	
	public void setCoins(int coins) {
		 this.coins = coins;
	}
	
	public void setTime(int time) {
		 this.time = time;
	}
	
	public void setSec(int sec) {
		 this.sec = sec;
	}
	
	public void setMin(int min) {
		 this.min = min;
	}
	
	public List<Tower> getTowers() {
		return this.towers;
	}
	
	public List<Enemy> getEnemys() {
		return this.enemys;
	}
	
	public String getCombinedEnemyHealth() {
		double health = 0;
		String healthSTR = "";
		for (Enemy enemy : getEnemys() )
			health += enemy.getHealth();
			
		healthSTR += health;
		healthSTR = healthSTR.substring(0, healthSTR.length() - 2);
		
		return healthSTR;
	}
	
	public void resetStats() {
		setScore(0);
		setWave(-1);
		setCoins(420);
		setTime(0);
		setMin(0);
		setSec(0);
	}
	
	public void rungame(Frame frame) {
		System.out.println(getManual());
		newgame(frame);
		long now = System.nanoTime();
		long later = 0;
		boolean halfsec = false;
		
		while (true) {

			if ( !getIsHighscore() ) {
				frame.hideHighscores();

				if ( !frame.getIsMenu() ) {
					frame.hideMenu();
					later = System.nanoTime();
					
					//what happens every sec*0.01?
					if(later > (now+10000000)) {
						showTowers();
						
						if ( !getEnemys().isEmpty() ) {
							for ( Tower tower : getTowers() ) {
								tower.rmShots();
//								tower.shootSave(frame);
								tower.shootNear(this);
							}
							
							enemyHealthcheck();
						} else {
							clearStage();
							nextWave();
						}
						
						
						for ( Enemy enemy : getEnemys() ) {
							moveEnemy(enemy, frame);
						}
						
						now = System.nanoTime();
						setTime(getTime() + 10000000);
						if (getTime() >= 500000000) {
							setTime(0);
							
							for ( Tower tower : getTowers() ) {
								if ( tower.getSpeed() > 8)
									tower.addShot();
							}
							
							if ( halfsec ) {
								setSec(getSec() + 1);
								
								for ( Tower tower : getTowers() ) {
									tower.addShot();
								}
								
								halfsec = false;
							} else
								halfsec = true;
							
						}
						
						if (getSec() >= 60) {
							setSec(0);
							setMin(getMin() + 1);
						}
						
						frame.repaint();
					}
				} else {
					if ( !frame.getIsScore() )
						frame.showMenu();
					else 
						frame.showScore();
					
					frame.repaint();
				}
			} else {
				frame.showHighscores();
				
				frame.repaint();
			}
			
		}
		
	}
	
	public void newgame(Frame frame) {
		resetStats();
		frame.setIsMenu(false);
		frame.setIsScore(false);
		setIsHighscore(false);
		setIsOver(false);
		frame.setIsSubmitted(false);
		
		getTowers().clear();
		getEnemys().clear();
		frame.hideMenu();
		frame.hideScore();
		frame.hideHighscores();
		
		nextWave();
	}
	
	public void nextWave() {
		clearStage();
		setWave(getWave() + 1);
		setCoins(getCoins() + (getWave() * 10));
		
		addEnemy();
	}
	
	//removes all shots from stage
	public void clearStage() {
		for ( Tower tower : getTowers() ) {
			tower.getShots().clear();
		}
	}
	
	//moves enemy towards player-tower; improvable 
	//TODO: instead only areas use 3 sets of moves, 1 for each area
	public void moveEnemy(Enemy enemy, Frame frame) {
		int random = enemy.randomInt(1, 3);
		int x = enemy.getX();
		int y = enemy.getY();
		
		if (x >= 620 && (y >= 450 && y <= 520)) {															//a		
			
			if (random == 1) {
				
				if (!enemy.isOutOfMap(x-5,y))
					enemy.moveLeft();
				else if (!enemy.isOutOfMap(x,y-5))
					enemy.moveUp();
				else 
					enemy.moveDown();
				
			} else if (random == 2) {
				
				if (!enemy.isOutOfMap(x,y+5))
					enemy.moveDown();
				else if (!enemy.isOutOfMap(x-5,y))
					enemy.moveLeft();
				else 
					enemy.moveUp();
				
			} else {
				
				if (!enemy.isOutOfMap(x,y-5))
					enemy.moveUp();
				else if (!enemy.isOutOfMap(x,y+5))
					enemy.moveDown();
				else 
					enemy.moveLeft();
				
			} 
			
		} else if ((x >= 550 && x <= 620) && (y >= 120 && y <= 520)) {										//b
		
			if (random == 1) {

				if (!enemy.isOutOfMap(x,y-5))
					enemy.moveUp();
				else if (!enemy.isOutOfMap(x+5,y))
					enemy.moveRight();
				else 
					enemy.moveLeft();
				
			} else if (random == 2) {
				
				if (!enemy.isOutOfMap(x-5,y))
					enemy.moveLeft();
				else if (!enemy.isOutOfMap(x,y-5))
					enemy.moveUp();
				else 
					enemy.moveRight();
				
			} else {
				
				if (!enemy.isOutOfMap(x+5,y))
					enemy.moveRight();
				else if (!enemy.isOutOfMap(x-5,y))
					enemy.moveLeft();
				else 
					enemy.moveUp();
				
			} 
			
		} else if ((x >= 120 && x <= 620) && (y >= 50 && y <= 120)) {										//c
					
			if (random == 1) {
				
				if (!enemy.isOutOfMap(x-5,y))
					enemy.moveLeft();
				else if (!enemy.isOutOfMap(x,y-5))
					enemy.moveUp();
				else 
					enemy.moveDown();
				
			} else if (random == 2) {
				
				if (!enemy.isOutOfMap(x,y-5))
					enemy.moveUp();
				else if (!enemy.isOutOfMap(x,y+5))
					enemy.moveDown();
				else 
					enemy.moveLeft();
				
			} else {
				
				if (!enemy.isOutOfMap(x,y+5))
					enemy.moveDown();
				else if (!enemy.isOutOfMap(x-5,y))
					enemy.moveLeft();
				else 
					enemy.moveUp();
				
			} 
			
		} else if ((x >= 50 && x <= 120) && (y >= 50 && y <= 550))	{										//d
			
			if (random == 1) {
				
				if (!enemy.isOutOfMap(x-5,y))
					enemy.moveLeft();
				else if (!enemy.isOutOfMap(x+5,y))
					enemy.moveRight();
				else 
					enemy.moveDown();
				
			} else if (random == 2) {
				
				if (!enemy.isOutOfMap(x+5,y))
					enemy.moveRight();
				else if (!enemy.isOutOfMap(x,y+5))
					enemy.moveDown();
				else 
					enemy.moveLeft();
				
			} else {
				
				if (!enemy.isOutOfMap(x,y+5))
					enemy.moveDown();
				else if (!enemy.isOutOfMap(x+5,y))
					enemy.moveLeft();
				else 
					enemy.moveRight();
				
			} 
			
		} else if ((x >= 50 && x <= 380) && (y >= 550 && y <= 620))	{										//e
			
			if (random == 1) {
				
				if (!enemy.isOutOfMap(x+5,y))
					enemy.moveRight();
				else if (!enemy.isOutOfMap(x,y-5))
					enemy.moveUp();
				else 
					enemy.moveDown();
				
			} else if (random == 2) {
				
				if (!enemy.isOutOfMap(x,y-5))
					enemy.moveUp();
				else if (!enemy.isOutOfMap(x,y+5))
					enemy.moveDown();
				else 
					enemy.moveRight();
				
			} else {
				
				if (!enemy.isOutOfMap(x,y+5))
					enemy.moveDown();
				else if (!enemy.isOutOfMap(x+5,y))
					enemy.moveRight();
				else 
					enemy.moveUp();
				
			} 
			
		} else if ((x >= 380 && x <= 450) && (y >= 270 && y <= 620))	{									//f
			
			if (random == 1) {
				
				if (!enemy.isOutOfMap(x-5,y))
					enemy.moveLeft();
				else if (!enemy.isOutOfMap(x,y-5))
					enemy.moveUp();
				else 
					enemy.moveRight();
				
			} else if (random == 2) {
				
				if (!enemy.isOutOfMap(x+5,y))
					enemy.moveRight();
				else if (!enemy.isOutOfMap(x-5,y))
					enemy.moveLeft();
				else 
					enemy.moveUp();
				
			} else {
				
				if (!enemy.isOutOfMap(x,y-5))
					enemy.moveUp();
				else if (!enemy.isOutOfMap(x+5,y))
					enemy.moveRight();
				else 
					enemy.moveLeft();
				
			} 
			
		} else if ((x >= 270 && x <= 450) && (y >= 200 && y <= 270))	{									//g
			
			if (random == 1) {
				
				if (!enemy.isOutOfMap(x-5,y))
					enemy.moveLeft();
				else if (!enemy.isOutOfMap(x,y-5))
					enemy.moveUp();
				else 
					enemy.moveDown();
				
			} else if (random == 2) {
				
				if (!enemy.isOutOfMap(x,y-5))
					enemy.moveUp();
				else if (!enemy.isOutOfMap(x-5,y))
					enemy.moveLeft();
				else 
					enemy.moveDown();
				
			} else {
				
				if (!enemy.isOutOfMap(x,y+5))
					enemy.moveDown();
				else if (!enemy.isOutOfMap(x,y-5))
					enemy.moveUp();
				else 
					enemy.moveLeft();
				
			} 
			
		} else if ((x >= 200 && x <= 270) && (y >= 200 && y <= 440))	{									//h
			
			if (random == 1) {
				
				if (!enemy.isOutOfMap(x-5,y))
					enemy.moveLeft();
				else if (!enemy.isOutOfMap(x+5,y))
					enemy.moveRight();
				else 
					enemy.moveDown();
				
			} else if (random == 2) {
				
				if (!enemy.isOutOfMap(x+5,y))
					enemy.moveRight();
				else if (!enemy.isOutOfMap(x,y+5))
					enemy.moveDown();
				else 
					enemy.moveLeft();
				
			} else {
				
				if (!enemy.isOutOfMap(x,y+5))
					enemy.moveDown();
				else if (!enemy.isOutOfMap(x-5,y))
					enemy.moveLeft();
				else 
					enemy.moveRight();
				
			} 
			
		}
		
		//enemy reaches tower
		if ((x >= 200 && x <= 270) && (y > 440 && y <= 500))
			lost(frame);
			
	}

	public void lost(Frame frame) {
		System.out.println("Tower destroyed!");
		gameover(frame);
	}
	
	public void enemyHealthcheck() {
		for ( Enemy enemy : getEnemys() ) {
			if ( enemy.getHealth() <= 0 ) {
				enemy.setIsRip(true);
				setCoins(getCoins() + 10);
			}
			
		}
		
		rmDeadEnemys();
	}
	
	public void addTower(int x, int y, Frame frame) {
		Tower newTower = new Tower(frame.getMallTowers().get(frame.getMallCount()).getName());
		newTower.setTower(x, y);
		getTowers().add(newTower);
		frame.add(newTower.getLabel());
		
		for ( Tower tower : getTowers() ) {
			tower.setId(getTowers().indexOf(tower));
		}
		
	}
	
	public void addEnemy() {
		for ( int i = 0; i < getWave(); i++) {
			Enemy enemy = new Enemy();
			enemy.setHealth(enemy.getHealth() + getWave());
			getEnemys().add(enemy);
		}
	}
	
	public void resetEnemyIds() {
		for ( Enemy enemy : getEnemys() ) {
			enemy.setId(getEnemys().indexOf(enemy));
		}
	}
	
	public void rmTowers() {
		List<Integer> idsToRm = new ArrayList<Integer>();
		
		for ( Tower tower : getTowers() ) {
			if ( tower.getIsRip() ) {
				idsToRm.add(tower.getId());
			}
			
		}

		for ( int id : idsToRm ) {
			try {
				getTowers().remove(id);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	//removes dead enemys
	public void rmDeadEnemys() {
		List<Integer> idsToRm = new ArrayList<Integer>();
		
		for ( Enemy enemy : getEnemys() ) {
			if ( enemy.getIsRip() ) {
				idsToRm.add(enemy.getId());
			}
			
		}

		for ( int id : idsToRm ) {
			try {
				getEnemys().remove(id);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void showTowers() {
		for ( Tower tower : getTowers() ) {
			tower.show();
		}
	}
	
	public void hideTowers() {
		for ( Tower tower : getTowers() ) {
			tower.hide();
		}
	}
	
	public String[] getHighscoreStr() throws IOException {
		//returns a String array, each field containing a row from highscore.csv
		String path = "/home/silver/Documents/eclipse-workspace/arcade/src/td_arc_dev/highscores.csv";
		String[] data = new String[8];
		String row;
		
		java.io.BufferedReader csvReader = new java.io.BufferedReader(new java.io.FileReader(path));
		int i = 0;
		while ((row = csvReader.readLine()) != null) {
		    data[i] = row;
		    i++;
		}
		csvReader.close();
		
		return data;
	}
	
	public int[] getHighscores() throws IOException {
		int[] scores = new int[8];
		String[] scoresString = getHighscoreStr();
		
		int i = 0;
		int count = 0;
		String load = "";
		for (String s : scoresString) {
			for (char c : s.toCharArray()) {
				if (c != ';') {
					load = load + c;
				} else {
					//after the first ; in every line is the score
					if (count == 1) {
						scores[i] = Integer.parseInt(load);
						i++;
					}
					
					count++;
					load = "";
				}
			}
			
			count = 0;
		}
		
		return scores;
	}
	
	public String[] getSortedHighscore(Frame frame) throws IOException {
		String[] sortedHighscore = getHighscoreStr();
		
		String name = frame.getPlayerName();
		int score = getScore();
		int wave = getWave();
		String time = getTimeStr();
		String date = new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm").format(new java.util.Date());
		
		int[] scores = getHighscores();
		int rank = 0;
	
		for ( int i : scores ) {
			
			if ( getScore() == i) {
				rank++;
				break;
			} else if ( getScore() > i) {
				break;
			}
			
			rank++;
		}
		
		for ( int i = 7; i > 0; i--) {
			if ( i >= rank )
				sortedHighscore[i] = sortedHighscore[i-1];
		}
		
		sortedHighscore[rank] = name + ";" 
								+ score + ";" 
								+ wave + ";" 
								+ time.substring(6) + ";"
								+ date;
			
		return sortedHighscore;
	}
	
	public void writeHighscore(Frame frame) throws IOException {
		String[] sortedHighscore = getSortedHighscore(frame);
		java.io.FileWriter csvWriter = new java.io.FileWriter("src/td_arc_dev/highscores.csv");
		
		int i = 0;
		for (String score : sortedHighscore) {
			csvWriter.append(score);
			if ( i != 7)
				csvWriter.append("\n");
			
			i++;
		}
		
		csvWriter.flush();
		csvWriter.close();
	}
	
	public boolean isHighscore() throws IOException {
		boolean isHighscore = false;
		
		for (int i : getHighscores()) {
			if (getScore() > i) {
				isHighscore = true;
			}
		}
		
		return isHighscore;
	}
	
	public String getTimeStr() {
		String timeStr = "time: ";
		int sec = getSec();
		int min = getMin();
		
		//mm:ss
		if (min == 0 && sec < 10)
			timeStr = timeStr.concat("00:0" + sec);
		else if (min == 0 && sec >= 10)
			timeStr = timeStr.concat("00:" + sec);
		else if ((min > 0 && min < 10) && sec >= 10) 
			timeStr = timeStr.concat("0" + min + ":" + sec);
		else if ((min > 0 && min < 10) && sec < 10) 
			timeStr = timeStr.concat("0" + min + ":0"+ sec);
		else if (min >= 10 && sec >= 10) 
			timeStr = timeStr.concat(min + ":" + sec);
		else if (min >= 10 && sec < 10) 
			timeStr = timeStr.concat(min + ":0"+ sec);
				
		return timeStr;
	}
	
	public void submitScore(Frame frame) {
		try {
			if ( isHighscore() )
				writeHighscore(frame);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		resetStats();
		frame.setIsSubmitted(true);
		frame.setIsScore(false);
		setIsHighscore(true);
	}
	
	public void gameover(Frame frame) {
		setIsOver(true);
		frame.setIsMenu(true);
		frame.setIsScore(true);
	}
}
