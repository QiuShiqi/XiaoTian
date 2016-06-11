package Module.DirectUI;


import java.awt.*;

public class SLabelControl extends UIControl {
	private String text;
	
	public SLabelControl(int x, int y, int width, int height, String text) {
		super(x, y + 35, width, height, "");
		this.text = text;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		this.changeImage();

		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
		g2d.drawString(text, 50, 10);
	}	
}