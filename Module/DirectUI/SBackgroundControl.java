package Module.DirectUI;

import java.awt.*;
import java.awt.geom.*;

public class SBackgroundControl extends UIControl {

	public SBackgroundControl(int width, int height, String src) {
		super(0, 0, width, height, src);

	}
	
	/*
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(this.color);
		
		
		Area one = new Area(new RoundRectangle2D.Double(0, 0, this.width, this.height, 8, 8));
		Area two = new Area(new Rectangle2D.Double(0, 0, this.width, this.height /2));
		
		switch(this.type){
			case 0:	//下圆角
				one.add(two);
				break;
			case 1:	//上圆角
				one.intersect(two);
				break;
		}
        
		Area one = new Area(new Rectangle2D.Double(0, 0, this.position.width, this.position.height));
		g2d.fill(one);

	}
	*/

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		this.changeImage();
		
		Graphics2D g2d = (Graphics2D) g;
		
		Area one = new Area(new Rectangle2D.Double(0, 0, this.position.width, this.position.height));
		g2d.fill(one);
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
		g2d.drawImage(this.imageTmp, 0, 35, this.position.width, this.position.height - 35, null);
	}
	
	/*
	public void changeColor(Color color){
		this.color = color;
		this.repaint();
	}
	*/

}
