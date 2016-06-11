package Module.DirectUI;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.imageio.ImageIO;

import Module.Main;

public class SButtonControl extends UIControl {
	
	public SButtonControl(int x, int y, int width, int height, String src) {
		super(x, y + 35, width, height, src);
		
		this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

            }
            
            public void mouseEntered(MouseEvent e){
            	setFocus(true);
            }
            
            public void mouseExited(MouseEvent e){
            	setFocus(false);
            }
        });
	}

	public SButtonControl(int x, int width, int height, String src) {
		this(x, -35, width, height, src);
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
		
		String tmp = this.focus ? "focus" : "default";
		
		try {
			String src = this.imageSrc.replace("*", tmp);
			this.imageTmp = ImageIO.read(Main.class.getResource(src));
		} catch (Exception e) {

		}
	}
	
}