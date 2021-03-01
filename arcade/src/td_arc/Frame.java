package td_arc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Frame extends JPanel implements MouseListener, KeyListener {
	private static final long serialVersionUID = 1L;
	
	public boolean isMall, isMenu, isHighscore, isScore, isOver, isSubmitted = false;
	public int 	score, coins, wave, time, sec, min,
				highlightX, highlightY,
				mallCount;
		
	JFrame app = new JFrame();
	
	JLabel mallBuyLabel = new JLabel();
	JLabel mallNextLabel = new JLabel();
	
	JLabel menuPlayLabel = new JLabel();
	JLabel menuHighscoresLabel = new JLabel();
	JLabel menuNewLabel = new JLabel();
	
	JLabel scoreLabel = new JLabel();
	JLabel finalScoreLabel = new JLabel();
	JLabel nameLabel = new JLabel();
	javax.swing.JTextField nameTextField = new javax.swing.JTextField();

	JLabel highscoreTitleLabel = new JLabel();
	JLabel scoreRanks = new JLabel();

	List<Enemy> enemys = new ArrayList<Enemy>();
	List<Tower> towers = new ArrayList<Tower>();
	List<Tower> mallTowers = new ArrayList<Tower>();
	
	public Frame() {
		this.app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.app.setTitle("td_arc");
		this.app.setSize(700,700);
		this.app.setResizable(false);
		this.app.setLocationRelativeTo(null);
		this.app.addKeyListener(this);
		this.app.getContentPane().add(this);
		this.app.setVisible(true);
		
		this.addMouseListener(this);

		//mall
		this.mallCount = 0;
		Tower mallSimpleTower = new Tower("simple tower");
		Tower mallGreyTower	= new Tower("grey tower");
		getMallTowers().add(mallSimpleTower);
		getMallTowers().add(mallGreyTower);
		for ( Tower tower : getMallTowers() ) {
			tower.setIsRip(true);
			tower.setX(560);
			tower.setY(600);
			tower.getLabel().setVisible(false);
			this.add(tower.getLabel());
		}
		
		this.mallBuyLabel.setIcon(new ImageIcon(getClass().getResource("buy.png")));
		this.mallBuyLabel.setVisible(false);
		this.add(this.mallBuyLabel);
		this.mallNextLabel.setIcon(new ImageIcon(getClass().getResource("next.png")));
		this.mallNextLabel.setVisible(false);
		this.add(this.mallNextLabel);
		
		//menu
		this.menuPlayLabel.setIcon(new ImageIcon(getClass().getResource("play.png")));
		this.menuPlayLabel.setVisible(false);
		this.add(this.menuPlayLabel);
		this.menuHighscoresLabel.setIcon(new ImageIcon(getClass().getResource("highscores.png")));
		this.menuHighscoresLabel.setVisible(false);
		this.add(this.menuHighscoresLabel);
		this.menuNewLabel.setIcon(new ImageIcon(getClass().getResource("new.png")));
		this.menuNewLabel.setVisible(false);
		this.add(this.menuNewLabel);
		
		//final score
		this.scoreLabel.setIcon(new ImageIcon(getClass().getResource("score.png")));
		this.scoreLabel.setVisible(false);
		this.add(this.scoreLabel);
		this.finalScoreLabel.setFont(new Font("Comic", Font.BOLD, 50));
		this.finalScoreLabel.setVisible(false);
		this.add(this.finalScoreLabel);
		//enter name
		this.nameLabel.setText("Enter name:");
		this.nameLabel.setFont(new Font("Comic", Font.PLAIN, 20));
		this.nameLabel.setVisible(false);
		this.add(this.nameLabel);
		this.nameTextField.setText("Anon");
		this.nameTextField.setVisible(false);
		this.add(this.nameTextField);
		
		//highscore
		this.highscoreTitleLabel.setIcon(new ImageIcon(getClass().getResource("highscoresTitle.png")));
		this.highscoreTitleLabel.setVisible(false);
		this.add(this.highscoreTitleLabel);
		this.scoreRanks.setIcon(new ImageIcon(getClass().getResource("ranks.png")));
		this.scoreRanks.setVisible(false);
		this.add(this.scoreRanks);
		
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
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//setBackroundColor
		g.setColor(Color.black);
		g.fillRect(0, 0, 1000, 1000);
		
		paintGrid(g);
		
		if ( !getIsMenu() && !getIsScore() && !getIsHighscore() ) {
			paintStage(g);
	
			//player-tower
			g.setColor(Color.yellow);
			g.fillRect(200, 450, 70, 70);
			
			//tower-shots
			for ( Tower tower : getTowers() ) {
				for ( Shot shot : tower.getShots() ) {
					shot.paintShot(g);
				}
			}
			
			//enemy
			for ( Enemy enemy : getEnemys() )
				enemy.paintEnemy(g);
			
			//mall
			if ( getIsMall() )
				showMall(g);
			else 
				hideMall();
				
		}
		
		paintHUD(g);

		//menu
		if ( getIsMenu() ) {
			g.setColor(Color.black);
			g.fillRect(0, 20, 1000, 1000);
		}
		
		//final score
		if ( getIsScore() ) {
			g.setColor(Color.yellow);
			g.fillRect(0, 0, 1000, 1000);
			
			g.setFont(new Font("Comic", Font.ITALIC + Font.BOLD, 20));
			g.setColor(Color.black);
			g.drawRect(450, 400, 200, 30);
			g.drawString("submit", 470, 420);
			g.drawRect(450, 440, 200, 30);
			g.drawString("menu", 470, 460);
		}
		
		//highscores
		if ( getIsHighscore() ) {
			paintHighscore(g);			
		}
		
	}
	
	public void paintGrid(Graphics g) {
		//paint grid for tower placement
		int gridX = 7;
		int gridY = 29;
		g.setColor(Color.gray);
		
		for (int i = 0; i < 34; i++) {
			g.drawRect(gridX, gridY, 19, 19);
			gridX += 20;
		}
		
		gridX = 7;
		
		for (int i = 0; i < 30; i++) {
			g.drawRect(gridX, gridY, 19, 19);
			g.drawRect(gridX + 20, gridY, 19, 19);
			gridY += 20;
		}
		
		for (int i = 0; i < 25; i++) {
			g.drawRect(gridX, gridY, 19, 19);
			gridX += 20;
		}
		
		gridX -= 40;
		
		for (int i = 0; i < 26; i++) {
			g.drawRect(gridX, gridY, 19, 19);
			g.drawRect(gridX + 20, gridY, 19, 19);
			g.drawRect(gridX + 40, gridY, 19, 19);
			g.drawRect(gridX + 60, gridY, 19, 19);
			gridY -= 20;
		}
		
		gridY += 20;
		
		for (int i = 0; i < 18; i++) {
			g.drawRect(gridX, gridY, 19, 19);
			g.drawRect(gridX, gridY + 20, 19, 19);
			g.drawRect(gridX, gridY + 40, 19, 19);
			gridX -= 20;
		}
		
		gridX += 20;
		gridY += 40;
		
		for (int i = 0; i < 18; i++) {
			g.drawRect(gridX, gridY, 19, 19);
			g.drawRect(gridX + 20, gridY, 19, 19);
			g.drawRect(gridX + 40, gridY, 19, 19);
			gridY += 20;
		}
		
		gridX = 275;
		gridY = 280;
		
		for (int i = 0; i < 13; i++) {
			g.drawRect(gridX, gridY, 19, 19);
			g.drawRect(gridX + 20, gridY, 19, 19);
			g.drawRect(gridX + 40, gridY, 19, 19);
			g.drawRect(gridX + 60, gridY, 19, 19);
			g.drawRect(gridX + 80, gridY, 19, 19);
			gridY += 20;
		}
		
		gridX = 627;
		gridY = 49;
		
		for (int i = 0; i < 20; i++) {
			g.drawRect(gridX, gridY, 19, 19);
			g.drawRect(gridX + 20, gridY, 19, 19);
			g.drawRect(gridX + 40, gridY, 19, 19);
			gridY += 20;
		}
		
		gridX = 547;
		gridY = 529;
		
		for (int i = 0; i < 3; i++) {
			g.drawRect(gridX, gridY, 19, 19);
			g.drawRect(gridX + 20, gridY, 19, 19);
			g.drawRect(gridX + 40, gridY, 19, 19);
			g.drawRect(gridX + 60, gridY, 19, 19);
			g.drawRect(gridX + 80, gridY, 19, 19);
			g.drawRect(gridX + 100, gridY, 19, 19);
			gridY += 20;
		}
	}
	
	public void paintStage(Graphics g) {
		//enemy stage						//areas
		g.setColor(Color.white);
		g.fillRect(550, 450, 150, 70);		//a
		g.fillRect(550, 50, 70, 450);		//b
		g.fillRect(100, 50, 450, 70);		//c
		g.fillRect(50, 50, 70, 500);		//d
		g.fillRect(50, 550, 400, 70);		//e
		g.fillRect(380, 200, 70, 350);		//f
		g.fillRect(200, 200, 180, 70);		//g
		g.fillRect(200, 200, 70, 250);		//h
	}
	
	public void paintHUD(Graphics g) {
		g.setColor(Color.red);
		g.drawString("score: " + getScore(), 60, 10);
		g.drawString(" wave: " + getWave(), 60, 21);
		g.drawString(getTimeStr(), 500, 10);
		g.drawString("coins: " + getCoins(), 350, 10);
		g.setColor(Color.orange);
		g.drawRect(1, 1, 50, 10);
		g.drawString("menu", 10, 10);	
	}
	
	public void paintHighscore(Graphics g) {
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
			scoreX = getScoreX(s.split(";")[1]);
			g.drawString(s.split(";")[1], scoreX, rankY);
			g.setColor(Color.blue);
			g.drawString("wave: " + s.split(";")[2], 490, waveY);
			g.setColor(Color.black);
			g.drawString(s.split(";")[3], 390, rankY);
			g.drawString(s.split(";")[4], 490, rankY);
			

			i++;
		}
		
	}

	public void highlightGrid(Graphics g, int x, int y) {
		g.setColor(Color.cyan);
		int[] grid = getGrid(x, y);
		int range = getMallTowers().get(this.mallCount).getRange();
		
		if ( !getIsOccupied(grid[0], grid[1]) ) {
			g.fillRect(grid[0], grid[1], 18, 18);
		} else { 

			for ( Tower tower : getTowers() ) {
				if ( (tower.getX() == grid[0]) && (tower.getY() == grid[1]) ) {
					range = tower.getRange();
				}

			}
		}
		
		g.drawOval(grid[0] - range + 9, grid[1] - range + 9, range*2, range*2);
	}
	
	public void rungame() {
		System.out.println(getManual());
		newgame();
		long now = System.nanoTime();
		long later = 0;
		boolean halfsec = false;
		
		while (true) {

			if ( !getIsHighscore() ) {
				hideHighscores();

				if ( !getIsMenu() ) {
					hideMenu();
					later = System.nanoTime();
					
					//what happens every sec*0.01?
					if(later > (now+10000000)) {
						showTowers();
						
						if ( !getEnemys().isEmpty() ) {
							for ( Tower tower : getTowers() ) {
								tower.rmShots();
//								tower.shootSave(this);
								tower.shootNear(this);
							}
							
							enemyHealthcheck();
						} else {
							clearStage();
							nextWave();
						}
						
						
						for ( Enemy enemy : getEnemys() ) {
							moveEnemy(enemy);
						}
						
						now = System.nanoTime();
						setTime(getTime() + 10000000);
						if (getTime() >= 500000000) {
							setTime(0);
							
							for ( Tower tower : getTowers() ) {
								if ( tower.getSpeed() > 4)
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
						
						repaint();
					}
				} else {
					if ( !getIsScore() )
						showMenu();
					else 
						showScore();
					
					repaint();
				}
			} else {
				showHighscores();
				
				repaint();
			}
			
		}
		
	}
	
	public void resetStats() {
		setScore(0);
		setWave(-1);
		setCoins(420);
		setTime(0);
		setMin(0);
		setSec(0);
	}
	
	public void newgame() {
		resetStats();
		setIsMenu(false);
		setIsScore(false);
		setIsHighscore(false);
		setIsOver(false);
		setIsSubmitted(false);
		
		getTowers().clear();
		getEnemys().clear();
		hideMenu();
		hideScore();
		hideHighscores();
		
		nextWave();
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
	
	public int getHighlightX() {
		return this.highlightX;
	}
	
	public int getHighlightY() {
		return this.highlightY;
	}
	
	public boolean getIsMall() {
		return this.isMall;
	}
	
	public boolean getIsOver() {
		return this.isOver;
	}
	
	public boolean getIsMenu() {
		return this.isMenu;
	}
	
	public boolean getIsHighscore() {
		return this.isHighscore;
	}
	
	public boolean getIsScore() {
		return this.isScore;
	}
	
	public boolean getIsSubmitted() {
		return this.isSubmitted;
	}
	
	public List<Tower> getMallTowers() {
		return this.mallTowers;
	}
	
	public List<Tower> getTowers() {
		return this.towers;
	}
	
	public List<Enemy> getEnemys() {
		return this.enemys;
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
	
	public void setHighlightX(int highlightX) {
		 this.highlightX = highlightX;
	}
	
	public void setHighlightY(int highlightY) {
		 this.highlightY = highlightY;
	}
	
	public void setIsMall(boolean isMall) {
		this.isMall = isMall;
	}
	
	public void setIsOver(boolean isOver) {
		 this.isOver = isOver;
	}
	
	public void setIsMenu(boolean isMenu) {
		 this.isMenu = isMenu;
	}
	
	public void setIsHighscore(boolean isHighscore) {
		 this.isHighscore = isHighscore;
	}
	
	public void setIsScore(boolean isScore) {
		 this.isScore = isScore;
	}
	
	public void setIsSubmitted(boolean isSubmitted) {
		 this.isSubmitted = isSubmitted;
	}
	
	public void nextWave() {
		clearStage();
		setWave(getWave() + 1);
		
		addEnemy();
	}
	
	public boolean getIsInGrid(int x, int y) {
		boolean isInGrid = false;
		
		if ( (x > 8 && x < 50) && (y > 30 && y < 650) )			isInGrid = true;
		if ( (x > 8 && x < 550) && (y > 630 && y < 650) )		isInGrid = true;
		if ( (x > 8 && x < 690) && (y > 30 && y < 50) )			isInGrid = true;
		if ( (x > 130 && x < 540) && (y > 130 && y < 190) )		isInGrid = true;
		if ( (x > 130 && x < 190) && (y > 130 && y < 530) )		isInGrid = true;
		if ( (x > 470 && x < 545) && (y > 130 && y < 650) )		isInGrid = true;
		if ( (x > 630 && x < 690) && (y > 30 && y < 450) )		isInGrid = true;
		if ( (x > 550 && x < 670) && (y > 530 && y < 590) )		isInGrid = true;
		
		return isInGrid;
	}
	
	public int[] getGrid(int x, int y) {
		int[] grid = {-50, -50};
		int newX = -50;
		int newY = -50;
		
		boolean isGrid = getIsInGrid(x, y);
		
		if ( isGrid ) {
			newX = 8;
			newY = 30;
	
		} else if ( (x > 275 && x < 375) && (y > 280 && y < 540) ) {
			//special area 
			newX = 276;
			newY = 281;
		} else {
			grid[0] = -50;
			return grid;
		}
		
		//x
		while ( newX < x )
			newX += 20;
		grid[0] = newX - 20;
		
		//y
		while ( newY < y )
			newY += 20;
 		grid[1] = newY - 20;
		
		return grid;
	}
	
	public int getScoreX(String scoreStr) {
		int x = 230;
		
		if ( scoreStr.length() == 10)
			x = 230;
		else if ( scoreStr.length() == 9)
			x = 240;
		else if ( scoreStr.length() == 8)
			x = 255;
		else if ( scoreStr.length() == 7)
			x = 270;
		else if ( scoreStr.length() == 6)
			x = 280;
		else if ( scoreStr.length() == 5)
			x = 300;
		else
			x = 310;
		
		return x;
	}
	
	public boolean getIsOccupied(int x, int y) {
		if ( x != -50 && !getIsMenu() && !getIsHighscore() ) {
			for ( Tower tower : getTowers() ) {
				if ( tower.getX() == x ) {
					if ( tower.getY() == y ) {
						return true;
					}
				}
				
			}
		} 
		
		return false;
	}
	
	public void buyTower(int x, int y) {
		boolean occupied = getIsOccupied(x, y);
		
		if ( !occupied && (x != -50) ) {
			if ( getCoins() >= getMallTowers().get(this.mallCount).getPrice() ) {
				setCoins(getCoins() - getMallTowers().get(this.mallCount).getPrice());
				addTower(x, y);
				System.out.println("bought " + getTowers().get(getTowers().size() - 1).getName());
			} else
				System.out.println("can't afford");
		}
		
	}
	
	//removes all shots from stage
	public void clearStage() {
		for ( Tower tower : getTowers() ) {
			tower.getShots().clear();
		}
	}
	
	//moves enemy towards player-tower; improvable 
	//TODO: instead only areas use 3 sets of moves, 1 for each area
	public void moveEnemy(Enemy enemy) {
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
			lost();
			
	}
	
	
	public void lost() {
		System.out.println("Tower destroyed!");
		gameover();
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
	
	public void addTower(int x, int y) {
		Tower newTower = new Tower(getMallTowers().get(this.mallCount).getName());
		newTower.setTower(x, y);
		getTowers().add(newTower);
		this.add(newTower.getLabel());
		
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
	
	public void showMall(Graphics g) {
		int[] grid = getGrid(getHighlightX(), getHighlightY());
		int x = grid[0];
		int y = grid[1];
		
		int price = 0;
		int damage = 0;
		int speed = 0;
		int range = 0;
		
		if ( getIsMall() && (!getIsHighscore() && !getIsScore() && !getIsMenu()) ) {
			
			if ( x != -50 && !getIsOccupied(x, y) ) {
				
				for ( Tower mallTower : getMallTowers() ) {
					if ( getMallTowers().indexOf(mallTower) == this.mallCount ) {
						price = mallTower.getPrice();
						damage = mallTower.getDamage();
						speed = mallTower.getSpeed();
						range = mallTower.getRange();
						mallTower.show();
					} else
						mallTower.hide();
				}
				
				highlightGrid(g, getHighlightX(), getHighlightY());
				this.mallBuyLabel.setBounds(557, 620, 23, 31);
				this.mallBuyLabel.setVisible(true);
				this.mallNextLabel.setBounds(664, 610, 36, 26);
				this.mallNextLabel.setVisible(true);
				g.setColor(Color.lightGray);
				g.drawRect(585, 595, 105, 55);
				g.setColor(Color.magenta);
				g.drawString("coins:	" + price, 590, 610);
				g.drawString("damage:	" + damage, 590, 621);
				g.drawString("speed:	" + speed, 590, 632);
				g.drawString("range:	" + range, 590, 643);
			} else if ( getIsOccupied(x, y) ) {
				highlightGrid(g, getHighlightX(), getHighlightY());
				
				for ( Tower tower : getTowers() ) {
					if ( (tower.getX() == x) && (tower.getY() == y)) {
						this.mallBuyLabel.setBounds(557, 620, 23, 31);
						this.mallBuyLabel.setVisible(true);
						this.mallNextLabel.setBounds(664, 610, 36, 26);
						this.mallNextLabel.setVisible(true);
						
						price = tower.getPrice();
						if ( tower.getName().equalsIgnoreCase("simple tower") ) {
							damage = tower.getDamage() + 10;
							speed = tower.getSpeed() + 1;
							range = tower.getRange() + 10;
						} else if ( tower.getName().equalsIgnoreCase("grey tower") ) {
							damage = tower.getDamage() + (tower.getDamage()/2);
							range = tower.getRange() + 1;
						}
						g.setColor(Color.lightGray);
						g.drawRect(585, 595, 105, 55);
						g.setColor(Color.magenta);
						g.drawString("upgrade:	" + price, 590, 610);
						g.drawString("damage:	" + damage, 590, 621);
						g.drawString("speed:	" + speed, 590, 632);
						g.drawString("range:	" + range, 590, 643);
						
						for ( Tower mallTower : getMallTowers() )
							if ( tower.getName().equalsIgnoreCase(mallTower.getName()) )
								mallTower.show();
						
					} 
				}
			}
			
		} else {
			hideMall();
		}
		
		
	}
	
	public void nextTowerMall() {
		if ( this.mallCount == 0)
			this.mallCount++;
		else
			this.mallCount--;
	}
	
	public void hideMall() {
		for (Tower tower : getMallTowers() )
			tower.hide();
		
		this.mallBuyLabel.setVisible(false);
		this.mallNextLabel.setVisible(false);
	}
	
	public void showMenu() {
		hideMall();
		hideTowers();
		hideScore();
		
		this.menuPlayLabel.setBounds(60, 50, 293, 156);
		this.menuHighscoresLabel.setBounds(400, 420, 255, 209);
		this.menuNewLabel.setBounds(220, 220, 246, 181);
		
		this.menuPlayLabel.setVisible(true);
		this.menuHighscoresLabel.setVisible(true);
		this.menuNewLabel.setVisible(true);
	}
	
	public void hideMenu() {
		this.menuPlayLabel.setVisible(false);
		this.menuHighscoresLabel.setVisible(false);
		this.menuNewLabel.setVisible(false);
	}
	
	public void showScore() {
		hideMall();
		hideTowers();
		hideMenu();
		
		int x = 150;
		
		//format score to x.xxx.xxx String
		java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0.00");
		df.setDecimalFormatSymbols(new java.text.DecimalFormatSymbols(java.util.Locale.ITALY));
		String scoreStr = df.format(new java.math.BigDecimal(getScore()));
		scoreStr = scoreStr.substring(0, scoreStr.length()-3);
		
		//set finalScoreLabel x
		if ( scoreStr.length() > 10)
			x = 135;
		else if ( scoreStr.length() == 10)
			x = 165;
		else if ( scoreStr.length() == 9)
			x = 190;
		else if ( scoreStr.length() == 8)
			x = 210;
		else if ( scoreStr.length() == 7)
			x = 230;
		else if ( scoreStr.length() == 6)
			x = 250;
		else if ( scoreStr.length() == 5)
			x = 265;
		else
			x = 280;
		
		this.scoreLabel.setBounds(150, 30, 387, 236);
		this.finalScoreLabel.setBounds(x, 280, 500, 50);
		this.nameLabel.setBounds(50, 400, 140, 30);
		this.nameTextField.setBounds(200, 400, 200, 30);
		
		this.finalScoreLabel.setText("" + scoreStr);
		
		this.scoreLabel.setVisible(true);
		this.finalScoreLabel.setVisible(true);
		this.nameLabel.setVisible(true);
		this.nameTextField.setVisible(true);
	}
	
	public void hideScore() {
		this.scoreLabel.setVisible(false);
		this.finalScoreLabel.setVisible(false);
		this.nameLabel.setVisible(false);
		this.nameTextField.setVisible(false);
	}
	
	public void showHighscores() {
		hideTowers();
		hideMenu();
		hideScore();
		
		this.scoreRanks.setBounds(0, 70, 70, 600);
		this.scoreRanks.setVisible(true);
		this.highscoreTitleLabel.setBounds(180, 10, 386, 103);
		this.highscoreTitleLabel.setVisible(true);
	}
	
	public void hideHighscores() {
		this.highscoreTitleLabel.setVisible(false);
		this.scoreRanks.setVisible(false);
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
	
	public String[] getSortedHighscore() throws IOException {
		String[] sortedHighscore = getHighscoreStr();
		
		String name = getPlayerName();
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
	
	public void writeHighscore() throws IOException {
		String[] sortedHighscore = getSortedHighscore();
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
	
	public String getPlayerName() {
		String player = "Anon";
		
		player = this.nameTextField.getText();
		if (player.length() > 10) {
			System.out.println("Only first 10 characters were submitted.");
			player = player.substring(0, 10);
		}
		
		if (!player.matches("([a-zA-Z0-9]| |ä|ö|ü|Ä|Ö|Ü){1,10}") || player.equalsIgnoreCase("")) {
			System.out.println("Name was changed to 'Anon', only 10 alphanumeric characters allowed.");
			player = "Anon";
		}	
		
		return player;
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
		else if (min > 10 && sec >= 10) 
			timeStr = timeStr.concat(min + ":" + sec);
		else if (min > 10 && sec < 10) 
			timeStr = timeStr.concat(min + ":0"+ sec);
				
		return timeStr;
	}
	
	public void submitScore() {
		try {
			if ( isHighscore() )
				writeHighscore();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		resetStats();
		setIsSubmitted(true);
		setIsScore(false);
		setIsHighscore(true);
	}
	
	public void gameover() {
		setIsOver(true);
		setIsMenu(true);
		setIsScore(true);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Coord X: " + e.getX() + " Coord Y: " + e.getY());

		//highlight field for tower placement
		setHighlightX(e.getX());
		setHighlightY(e.getY());
				
				
		//menu-button
		if ( !getIsScore() && (e.getX() <= 50 && e.getY() <= 20) && !getIsHighscore() && !getIsOver() ) {
			if (!getIsMenu())
				setIsMenu(true);
			else
				setIsMenu(false);
		//menu-play-button
		} else if (!getIsScore() && (e.getX() >= 60 && e.getX() <= 350) && (e.getY() >= 50 && e.getY() <= 200) && !getIsHighscore() && !getIsOver() ) {
			setIsMenu(false);
		//menu-highscores-button
		} else if (!getIsScore() && ((e.getX() >= 400 && e.getX() <= 650) && (e.getY() >= 420 && e.getY() <= 630)) && getIsMenu()) {
			setIsHighscore(true);
		//menu-new-button
		} else if (!getIsScore() && getIsMenu() && (e.getX() >= 220 && e.getX() <= 460) && (e.getY() >= 220 && e.getY() <= 400) && !getIsHighscore()) {
			newgame();
		}
		
		//submit score if game is over
		if ( getIsOver() && (e.getX() >= 450 && e.getX() <= 670) && (e.getY() >= 400 && e.getY() <= 430) && !getIsSubmitted() ) {
			submitScore();
		//TODO: back to menu if game is over ("button" is ready...)
		} else if ( getIsOver() && (e.getX() >= 450 && e.getX() <= 670) && (e.getY() >= 440 && e.getY() <= 470) ) {
			;
			
		}
		
		//mall
		if ( getIsInGrid(e.getX(), e.getY()) )
			setIsMall(true);
		else 
			setIsMall(false);
				
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {

	}
	
	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
//		System.out.println(e.getKeyCode());
		
		//keys to menu: p || space || return
		if ( ((e.getKeyCode() == 80 || e.getKeyCode() == 32 || e.getKeyCode() == 10)) && !getIsOver() ) {
			if (!getIsOver())
				setIsMenu(true);
			else
				setIsMenu(false);
		}
		
		//hide highscores with any key (except esc)
		if ( getIsHighscore() && !(e.getKeyCode() == 27)) {
			setIsHighscore(false);
			hideHighscores();
		}
			
		
		//esc
		if ( e.getKeyCode() == 27 ) {
			System.out.println("escaped");
			if (getIsScore() || getIsHighscore())
				System.exit(0);
			else 
				gameover();
		}
		
		//select tower with arrows
		if ( e.getKeyCode() == 37 || e.getKeyCode() == 39 ) {
			nextTowerMall();
		}
		
		//buy a tower with b
		if ( e.getKeyCode() == 66 ) {
			int[] grid = getGrid(getHighlightX(), getHighlightY());
			
			if ( !getIsOccupied(grid[0], grid[1]) )
				buyTower(grid[0], grid[1]);			
			else {
				for ( Tower tower : getTowers() ) {
					if ( (tower.getX() == grid[0]) && (tower.getY() == grid[1]) )
						tower.upgrade(this);
				}
			}
		}
		
		//t - test
		if ( e.getKeyCode() == 84 ) {

		}
	}

	
}
