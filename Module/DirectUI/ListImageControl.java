package Module.DirectUI;


import java.awt.*;

import javax.imageio.ImageIO;

import Module.Main;

public class ListImageControl extends UIControl {
	private String text;
	
	public ListImageControl(int x, int y, int width, int height, String src, String text) {
		super(x, y + 35, width, height, src);
		this.text = text;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		this.changeImage();

		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
		g2d.drawImage(this.imageTmp, 3, 3, this.position.width, this.position.height, null);
		g2d.drawString(this.text, this.position.width + 5 + 8, this.position.height / 2 + 8);
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