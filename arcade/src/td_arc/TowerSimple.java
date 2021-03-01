package td_arc;

import javax.swing.ImageIcon;

public class TowerSimple extends Tower {

	public TowerSimple() {
		setName("simple tower");
		setPrice(20);
		setDamage(10);
		setSpeed(1);
		setRange(150);
		
		getLabel().setIcon(new ImageIcon(getClass().getResource("simpleTower.png")));
	}
	
}
