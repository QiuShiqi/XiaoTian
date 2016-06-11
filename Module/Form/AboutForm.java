package Module.Form;

import java.awt.Color;
import javax.swing.*;

public class AboutForm extends BaseUI {
	
	public AboutForm(){
		
		//窗体设置
		this.setPosition(this, 350, 200);
		
		this.initialize();
		this.initializeUI("关于 ", "about-title.png", "about-background.png");
		this.titleControl.changeColor(new Color(255, 255, 255));
		this.setAlwaysOnTop(true);
	}
	
	public void initialize(){
		//初始化标签
		JLabel copy = new JLabel("<html><span style='color:blue;'>Sikei Jau[qiushiqi6@qq.com]</span></html>");
		this.setSize(copy, 40, 0, 300, 45);
		this.add(copy);
		
		JLabel mail = new JLabel("<html>本软件开源并遵守GNU通用公共授权条款第三版（<br />GNU General PublicLicense Lv.3）<br /><br /><span style='color:red;'>意味着您拥有免费使用、复制分发、研究改写、再<br />利用该软件的自由，但不允许使用修改后和衍生的<br />代码作为闭源的商业软件发布和销售。</span></html>");
		this.setSize(mail, 40, 50, 300, 100);
		this.add(mail);
	}
	
	protected void close(){

	}
}
