package Module.Form;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import com.sun.awt.AWTUtilities;

import Module.*;
import Module.Data.*;
import Module.DirectUI.*;

public abstract class BaseUI extends Controller {
	protected UserModule userModule = null; // 客户类
	protected STitleControl titleControl = null;
	protected SBackgroundControl backgroundControl = null;

	public BaseUI() {
		this.setLayout(null);

		// 设置统一字体
		Font font = new Font("宋体", Font.PLAIN, 12);
		UIManager.put("Button.font", font);
		UIManager.put("Label.font", font);
		UIManager.put("Panel.font", font);
		UIManager.put("List.font", font);

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

		} catch (Exception e) {

		}

		// 检查配置文件
		if (!BaseModule.Config()) {
			BaseModule.messageBox("无法读取配置文件！", -1);
			System.exit(0);
		} else {
			if (Controller.dataSource == null) {
				Controller.dataSource = new Oracle(BaseModule.databaseInfo.host, BaseModule.databaseInfo.port,
						BaseModule.databaseInfo.userName, BaseModule.databaseInfo.password);
				if (!Controller.dataSource.InitiallzData("orcl")) {
					BaseModule.messageBox("无法连接到服务器！", -1);
					System.exit(0);
				}
			}
		}

	}

	// 窗体关闭事件 默认为关闭所有窗体
	protected void close() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	protected void aboutButtonControl() {
	}

	protected void aboutForm() {
		new AboutForm();

	}

	protected void closeAboutControl() {
	}

	// 封装setBounds方法
	protected void setSize(Component obj, int x, int y, int width, int height) {
		obj.setBounds(new Rectangle(x, y + 35, width, height));
	}

	// 窗体初始化
	protected void setPosition(JFrame obj, int width, int height) {
		int x = (Toolkit.getDefaultToolkit().getScreenSize().width - width) / 2;
		int y = (Toolkit.getDefaultToolkit().getScreenSize().height - height) / 2;
		obj.setBounds(x, y, width, height + 10);
		obj.setResizable(false);

		this.close();
	}

	protected void setPosition(JDialog obj, int width, int height) {
		int x = (Toolkit.getDefaultToolkit().getScreenSize().width - width) / 2;
		int y = (Toolkit.getDefaultToolkit().getScreenSize().height - height) / 2;
		obj.setBounds(x, y, width, height);
		obj.setResizable(false);

	}

	// 设置UI库
	protected void initializeUI(String titleName, String titleSrc, String backgroundSrc) {
		this.setTitle(titleName, titleSrc);
		this.setBackground(backgroundSrc);

		this.setVisible(true);
	}

	protected void setTitle(String titleName, String src) {
		this.setUndecorated(true);
		AWTUtilities.setWindowOpaque(this, false);

		this.titleControl = new STitleControl(titleName, this, src);
		this.add(this.titleControl);

		super.setTitle(titleName);
	}

	public void setTitle(String title) {
		this.titleControl.titleName = title;
		this.titleControl.fresh();
	}

	private void setBackground(String src) {
		this.backgroundControl = new SBackgroundControl(this.getWidth(), this.getHeight(), src);
		this.add(this.backgroundControl);

		this.backgroundControl.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				repaint();
			}
		});
	}

	public void addRecord(int id) {

	}

	public void addFriend(int id) {

	}

	public void systemType(int id) {

	}
}
