package Module.DirectUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;

import Module.Form.BaseUI;

public class STitleControl extends UIControl {
	private BaseUI form;
	
	private SButtonControl minButton = null;
	private SButtonControl closeButton = null;
	
	public String titleName = "";
	private Color color = Color.BLACK;

	public STitleControl(String titleName, BaseUI form, String src) {
		super(0, 0, 0, 0, src);
		
		this.setLayout(null);
		this.titleName = titleName;
		this.form = form;
		this.setBounds(new Rectangle(0, 0, this.form.getWidth(), 35));

		WindowListener moveListener = new WindowListener(form);
		this.addMouseListener(moveListener);
		this.addMouseMotionListener(moveListener);
		
		this.setOpaque(false);
		this.initialize();
		
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(this.imageSrc == ""){
			return;
		}

		this.changeImage();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));

		g2d.drawImage(this.imageTmp, 0, 0, this.form.getWidth(), 35, null);
		
		g2d.setColor(this.color);
		g2d.drawString(this.titleName, 10, 21);

	}
	
	public void changeColor(Color color){
		this.color = color;
		this.repaint();
	}
	
	private void initialize() {
		
		this.minButton = new SButtonControl(this.form.getWidth() - 56, 28, 28, "title-min-*.png");
		this.closeButton = new SButtonControl(this.form.getWidth() - 28, 28, 28, "title-close-*.png");
		
		this.add(this.minButton);
		this.add(this.closeButton);
		
		this.minButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	form.setExtendedState(JFrame.ICONIFIED);
            }

            public void mouseEntered(MouseEvent e){
            	minButton.setFocus(true);
            }
            
            public void mouseExited(MouseEvent e){
            	minButton.setFocus(false);
            }
        });
		
		this.closeButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	form.dispatchEvent(new WindowEvent(form, WindowEvent.WINDOW_CLOSING));
            }
            
            public void mouseEntered(MouseEvent e){
            	closeButton.setFocus(true);
            }
            
            public void mouseExited(MouseEvent e){
            	closeButton.setFocus(false);
            }
        });

	}
	
	public void changeMinImages(String src){
		this.minButton.changeImage(src);

	}
	
	public void changeCloseImages(String src){
		this.closeButton.changeImage(src);

	}
}

//消息处理
class WindowListener extends MouseAdapter {
	private Point lastPoint = null;
	private BaseUI window = null;

	public WindowListener(BaseUI window) {
		this.window = window;
	}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			this.lastPoint = e.getLocationOnScreen();
		} else {
			this.lastPoint = null;
		}

	}

	public void mouseDragged(MouseEvent e) {
		if (this.lastPoint != null) {
			Point point = e.getLocationOnScreen();
			int offsetX = point.x - this.lastPoint.x;
			int offsetY = point.y - this.lastPoint.y;

			Rectangle bounds = window.getBounds();
			bounds.x += offsetX;
			bounds.y += offsetY;
			window.setBounds(bounds);
			lastPoint = point;
		}

	}
}