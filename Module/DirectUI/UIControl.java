package Module.DirectUI;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Module.BaseModule;
import Module.Main;

public abstract class UIControl extends JPanel {

	protected Rectangle position;
	protected String imageSrc;
	protected BufferedImage imageTmp = null;
	
	protected boolean focus = false;
	protected boolean enabled = true;
	
	public UIControl(int x, int y, int width, int height, String src){
		this.position = new Rectangle(x, y, width, height);
		this.imageSrc = BaseModule.imageLoader(src);
		
		this.setBounds(new Rectangle(this.position));
		this.setOpaque(false);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

	}
	
	public void setFocus(boolean focus) {
		this.focus = focus;
		this.repaint();

	}
	
	public void changeImage(String src) {
		this.imageSrc = BaseModule.imageLoader(src);
		this.repaint();
	}
	
	protected void changeImage() {
		
		try {
			this.imageTmp = ImageIO.read(Main.class.getResource(this.imageSrc));
		} catch (Exception e) {

		}
	}

	
	public void setEnabled(boolean enable){
		this.enabled = enable;
		if(!this.enabled){
	        int imageWidth = this.imageTmp.getWidth();
	        int imageHeight = this.imageTmp.getHeight();
	  
	        BufferedImage newPic = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_4BYTE_ABGR);  
	  
	        ColorConvertOp cco = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);  
	        cco.filter(this.imageTmp, newPic);
	        this.imageTmp = newPic;
	        this.repaint();
		}

	}
	
	public boolean getEnabled(){
		return this.enabled;
	}
	
	public void changeSize(int width, int height){
		this.position.width = width;
		this.position.height = height;
        this.repaint();
	}
	
	public void changePosition(int x, int y){
		this.position.x = x;
		this.position.y = y;
        this.repaint();
	}

	public void fresh(){
		this.repaint();
	}
}
