package td_arc_dev;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Frame extends JPanel implements MouseListener, KeyListener {
	private static final long serialVersionUID = 1L;

	public boolean isMall, isMenu, isSubmitted, isScore, isFast = false;
	
	public int highlightX, highlightY, mallCount;
	
	TowerDefenseArcade td_arc = new TowerDefenseArcade();
	
	JFrame app = new JFrame();
	
	List<Tower> mallTowers = new ArrayList<Tower>();
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

	public Frame() {
		this.app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.app.setTitle("td_arc_dev");
		this.app.setSize(700,700);
		this.app.setResizable(false);
		this.app.setLocationRelativeTo(null);
		this.app.addKeyListener(this);
		this.app.getContentPane().add(this);
		this.app.setVisible(true);
		
		addMouseListener(this);

		//mall
		this.mallCount = 0;
		Tower mallSimpleTower = new Tower("simple tower");
		Tower mallGreyTower	= new Tower("grey tower");
		Tower mallBombTower = new Tower("bomb tower");
		getMallTowers().add(mallSimpleTower);
		getMallTowers().add(mallGreyTower);
		getMallTowers().add(mallBombTower);
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
		
		this.td_arc.rungame(this);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//setBackroundColor
		g.setColor(Color.black);
		g.fillRect(0, 0, 1000, 1000);
		
		paintGrid(g);
		
		if ( !getIsMenu() && !getIsScore() && !this.td_arc.getIsHighscore() ) {
			paintStage(g);
	
			//player-tower
			g.setColor(Color.yellow);
			g.fillRect(200, 450, 70, 70);
			
			//tower-shots
			for ( Tower tower : this.td_arc.getTowers() ) {
				for ( Shot shot : tower.getShots() ) {
					shot.paintShot(g);
				}
			}
			
			//enemy
			for ( Enemy enemy : this.td_arc.getEnemys() )
				enemy.paintEnemy(g);
			
			//mall
			if ( getIsMall() )
				showMall(g);
			else 
				hideMall();
				
		}
		
		paintHUD(g, this.td_arc);
		
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
		if ( this.td_arc.getIsHighscore() ) {
			this.td_arc.paintHighscore(g, this);			
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
		
		for (int i = 0; i < 13; i++) {				//S
			g.drawRect(gridX, gridY, 19, 19);		//P
			g.drawRect(gridX + 20, gridY, 19, 19);	//E
			g.drawRect(gridX + 40, gridY, 19, 19);	//C
			g.drawRect(gridX + 60, gridY, 19, 19);	//I
			g.drawRect(gridX + 80, gridY, 19, 19);	//A
			gridY += 20;							//L
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
	
	public void paintHUD(Graphics g, TowerDefenseArcade td_arc) {
		g.setColor(Color.orange);
		g.drawRect(1, 1, 50, 10);
		g.drawString("menu", 10, 10);
		if (!getIsFast()) {
			g.setColor(Color.green);
			g.drawRect(6, 16, 25, 10);
			g.drawString(">>", 10, 25);
		} else {
			g.setColor(Color.green);
			g.drawRect(6, 16, 35, 10);
			g.drawString(">>>", 10, 25);
		}
		g.setColor(Color.red);
		g.drawString("score: " + td_arc.getScore(), 60, 10);
		g.drawString("wave: " + td_arc.getWave(), 230, 10);
		g.drawString("health: " + td_arc.getCombinedEnemyHealth(), 230, 21);
		g.drawString("coins: " + td_arc.getCoins(), 320, 10);
		g.drawString(td_arc.getTimeStr(), 500, 10);
	}
	
	public void highlightGrid(Graphics g, int x, int y) {
		g.setColor(Color.cyan);
		int[] grid = getGrid(x, y);
		int range = getMallTowers().get(this.mallCount).getRange();
		
		if ( !getIsOccupied(grid[0], grid[1]) ) {
			g.fillRect(grid[0], grid[1], 18, 18);
		} else { 

			for ( Tower tower : this.td_arc.getTowers() ) {
				if ( (tower.getX() == grid[0]) && (tower.getY() == grid[1]) ) {
					range = tower.getRange();
				}

			}
		}
		
		g.drawOval(grid[0] - range + 9, grid[1] - range + 9, range*2, range*2);
	}
	
	//does it work?
	public Frame getFrame() {
		return this;
	}
	
	public boolean getIsMall() {
		return this.isMall;
	}
	
	public boolean getIsMenu() {
		return this.isMenu;
	}
	
	public boolean getIsScore() {
		return this.isScore;
	}
	
	public boolean getIsSubmitted() {
		return this.isSubmitted;
	}
	
	public boolean getIsFast() {
		return this.isFast;
	}

	public int getMallCount() {
		return this.mallCount;
	}
	
	public int getHighlightX() {
		return this.highlightX;
	}
	
	public int getHighlightY() {
		return this.highlightY;
	}
	
	public List<Tower> getMallTowers() {
		return this.mallTowers;
	}
	
	public void setMallCount(int mallCount) {
		this.mallCount = mallCount;
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
	
	public void setIsMenu(boolean isMenu) {
		 this.isMenu = isMenu;
	}
	
	public void setIsScore(boolean isScore) {
		 this.isScore = isScore;
	}
	
	public void setIsSubmitted(boolean isSubmitted) {
		 this.isSubmitted = isSubmitted;
	}
	
	public void setIsFast(boolean isFast) {
		this.isFast = isFast;
	}
	
	public boolean getIsInGrid(int x, int y) {		
		if ( (x > 8 && x < 50) && (y > 30 && y < 650) )			return true;
		if ( (x > 8 && x < 550) && (y > 630 && y < 650) )		return true;
		if ( (x > 8 && x < 690) && (y > 30 && y < 50) )			return true;
		if ( (x > 130 && x < 540) && (y > 130 && y < 190) )		return true;
		if ( (x > 130 && x < 190) && (y > 130 && y < 530) )		return true;
		if ( (x > 470 && x < 545) && (y > 130 && y < 650) )		return true;
		if ( (x > 630 && x < 690) && (y > 30 && y < 450) )		return true;
		if ( (x > 550 && x < 670) && (y > 530 && y < 590) )		return true;
		if ( (x > 275 && x < 375) && (y > 280 && y < 540) )		return true; //SPECIAL
		
		return false;
	}
	
	public int[] getGrid(int x, int y) {
		int[] grid = {-50, -50};
		int newX = -50;
		int newY = -50;
		
		boolean isGrid 		= getIsInGrid(x, y);
		boolean isSpecial	= false;
		
		if ( (x > 275 && x < 375) && (y > 280 && y < 540) )
			isSpecial = true;
		
		if (isGrid && !isSpecial) {
			newX = 8;
			newY = 30;
		} else if (isGrid && isSpecial) {
			//SPECIAL
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
		if ( x != -50 && !getIsMenu() && !this.td_arc.getIsHighscore() ) {
			for ( Tower tower : this.td_arc.getTowers() ) {
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
			if ( this.td_arc.getCoins() >= getMallTowers().get(this.getMallCount()).getPrice() ) {
				this.td_arc.setCoins(this.td_arc.getCoins() - getMallTowers().get(this.getMallCount()).getPrice());
				this.td_arc.addTower(x, y, this);
				System.out.println("bought " + this.td_arc.getTowers().get(this.td_arc.getTowers().size() - 1).getName());
			} else
				System.out.println("can't afford");
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
		int accuracy = 0;
		
		if ( getIsMall() && (!this.td_arc.getIsHighscore() && !getIsScore() && !getIsMenu()) ) {
			
			if ( x != -50 && !getIsOccupied(x, y) ) {
				
				for ( Tower mallTower : getMallTowers() ) {
					if ( getMallTowers().indexOf(mallTower) == this.getMallCount() ) {
						price = mallTower.getPrice();
						damage = mallTower.getDamage();
						speed = mallTower.getSpeed();
						range = mallTower.getRange();
						accuracy = mallTower.getAccuracy();
						mallTower.show();
					} else
						mallTower.hide();
				}
				
				highlightGrid(g, getHighlightX(), getHighlightY());
				this.mallBuyLabel.setBounds(557, 620, 23, 31);
				this.mallBuyLabel.setVisible(true);
				g.setColor(Color.lightGray);
				g.drawRect(585, 595, 105, 65);
				g.setColor(Color.magenta);
				g.drawString("coins:	" + price, 590, 609);
				g.drawString("damage:	" + damage, 590, 620);
				g.drawString("speed:	" + speed, 590, 631);
				g.drawString("range:	" + range, 590, 642);
				g.drawString("accuracy:	" + accuracy, 590, 653);
			} else if (getIsOccupied(x, y)) {
				highlightGrid(g, getHighlightX(), getHighlightY());
				
				for ( Tower tower : this.td_arc.getTowers() ) {
					if ( (tower.getX() == x) && (tower.getY() == y)) {
						this.mallBuyLabel.setBounds(557, 620, 23, 31);
						this.mallBuyLabel.setVisible(true);
						
						price = tower.getPrice();
						if ( tower.getName().equalsIgnoreCase("simple tower") ) {
							damage = tower.getDamage() + 10;
							speed = tower.getSpeed() + 2;
							range = tower.getRange() + 10;
							accuracy = tower.getAccuracy() + 1;
						} else if ( tower.getName().equalsIgnoreCase("grey tower") ) {
							damage = tower.getDamage() + (tower.getDamage()/2);
							speed = tower.getSpeed() + 1;
							range = tower.getRange() + 1;
							accuracy = tower.getAccuracy() + 1;
						} else if ( tower.getName().equalsIgnoreCase("bomb tower") ) {
							damage = tower.getDamage() + 15;
							speed = tower.getSpeed() + 1;
							range = tower.getRange() + 2;
							accuracy = tower.getAccuracy() + 1;
						}
						g.setColor(Color.lightGray);
						g.drawRect(585, 595, 105, 65);
						g.setColor(Color.magenta);
						g.drawString("coins:	" + price, 590, 609);
						g.drawString("damage:	" + damage, 590, 620);
						g.drawString("speed:	" + speed, 590, 631);
						g.drawString("range:	" + range, 590, 642);
						g.drawString("accuracy:	" + accuracy, 590, 653);
						
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
	
	public void nextTowerMall(int direction) {
		if (direction == 1) {
			if ( getMallCount() != (getMallTowers().size()-1) )
				setMallCount(getMallCount() + 1);
			else
				setMallCount(0);
		} else if (direction == 0) {
			if (getMallCount() != 0)
				setMallCount(getMallCount() - 1);
			else
				setMallCount(getMallTowers().size()-1);
		}
	}
	
	public void hideMall() {
		for (Tower tower : getMallTowers() )
			tower.hide();
		
		this.mallBuyLabel.setVisible(false);
	}
	
	public void showMenu() {
		hideMall();
		this.td_arc.hideTowers();
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
		this.td_arc.hideTowers();
		hideMenu();
		
		int x = 150;
		
		//format score to x.xxx.xxx String
		java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0.00");
		df.setDecimalFormatSymbols(new java.text.DecimalFormatSymbols(java.util.Locale.ITALY));
		String scoreStr = df.format(new java.math.BigDecimal(this.td_arc.getScore()));
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
		this.td_arc.hideTowers();
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
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		System.out.println("X: " + x + " Y: " + y);

		//highlight field for tower placement
		setHighlightX(e.getX());
		setHighlightY(e.getY());	
				
		//menu-button
		if (!getIsScore() && (x <= 50 && y <= 15) && !this.td_arc.getIsHighscore() && !this.td_arc.getIsOver()) {
			if (!getIsMenu())
				setIsMenu(true);
			else
				setIsMenu(false);
		//speedup-button
		} else if (!getIsScore() && (x <= 50 && y <= 30) && y > 15 && !this.td_arc.getIsHighscore() && !this.td_arc.getIsOver()) {
			if ( this.td_arc.getTempo() == 10000000 ) {
				this.td_arc.setTempo(5000000);
				setIsFast(true);
			} else {
				this.td_arc.setTempo(10000000);
				setIsFast(false);
			}
		//menu-play-button
		} else if (!getIsScore() && (x >= 60 && x <= 350) && (y >= 50 && y <= 200) && !this.td_arc.getIsHighscore() && !this.td_arc.getIsOver() ) {
			setIsMenu(false);
		//menu-highscores-button
		} else if (!getIsScore() && ((x >= 400 && x <= 650) && (y >= 420 && y <= 630)) && getIsMenu()) {
			this.td_arc.setIsHighscore(true);
		//menu-new-button
		} else if (!getIsScore() && getIsMenu() && (x >= 220 && x <= 460) && (y >= 220 && y <= 400) && !this.td_arc.getIsHighscore()) {
			this.td_arc.newgame(this);
		}
		
		//submit score if game is over
		if ( this.td_arc.getIsOver() && (x >= 450 && x <= 670) && (y >= 400 && y <= 430) && !getIsSubmitted() ) {
			this.td_arc.submitScore(this);
		//TODO: back to menu if game is over ("button" is ready...)
		} else if ( this.td_arc.getIsOver() && (x >= 450 && x <= 670) && (y >= 440 && y <= 470) ) {
			;
			
		}
		
		//mall
		if ( getIsInGrid(x, y) )
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
		if ( ((e.getKeyCode() == 80 || e.getKeyCode() == 32 || e.getKeyCode() == 10)) && !this.td_arc.getIsOver() ) {
			if (!this.td_arc.getIsOver())
				setIsMenu(true);
			else
				setIsMenu(false);
		}
		
		//hide highscores with any key (except esc)
		if ( this.td_arc.getIsHighscore() && !(e.getKeyCode() == 27)) {
			this.td_arc.setIsHighscore(false);
			hideHighscores();
		}
			
		
		//esc
		if ( e.getKeyCode() == 27 ) {
			System.out.println("escaped");
			if (getIsScore() || this.td_arc.getIsHighscore())
				System.exit(0);
			else 
				this.td_arc.gameover(this);
		}
		
		//select tower with arrows
		if ( e.getKeyCode() == 37 ) {
			nextTowerMall(0);
		} else if ( e.getKeyCode() == 39 ) {
			nextTowerMall(1);
		}
		
		//buy a tower with b
		if ( e.getKeyCode() == 66 ) {
			int[] grid = getGrid(getHighlightX(), getHighlightY());
			
			if ( !getIsOccupied(grid[0], grid[1]) )
				buyTower(grid[0], grid[1]);			
			else {
				for ( Tower tower : this.td_arc.getTowers() ) {
					if ( (tower.getX() == grid[0]) && (tower.getY() == grid[1]) )
						tower.upgrade(this.td_arc);
				}
			}
		}
		
		//t - test
		if ( e.getKeyCode() == 84 ) {

		}
	}

	
}
