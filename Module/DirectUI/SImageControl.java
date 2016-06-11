package Module.DirectUI;


import java.awt.*;

import javax.imageio.ImageIO;

import Module.Main;

public class SImageControl extends UIControl {
	public SImageControl(int x, int y, int width, int height, String src) {
		super(x, y + 35, width, height, src);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		this.changeImage();

		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
		g2d.drawImage(this.imageTmp, 0, 0, this.position.width, this.position.height, null);
	}
	
	
	protected void changeImage() {
		if(!this.enabled){
			return;
		}
		
		try {
			this.imageTmp = ImageIO.read(Main.class.getResource(this.imageSrc));
		} catch (Exception e) {

		}
	}
	
}