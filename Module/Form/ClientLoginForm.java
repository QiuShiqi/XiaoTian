package Module.Form;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

import Module.*;
import Module.User.*;
import Module.DirectUI.*;

public class ClientLoginForm extends BaseUI {

	private JTextField userNameControl = new JTextField("帐号");
	private JTextField passwordControl = new JTextField("密码");
	private JPasswordField passwordRealControl = new JPasswordField();
	
	private SButtonControl typeButtonControl = new SButtonControl(135, 250, 180, 32, "client-login-*.png");	//登录
	private JLabel typeControl = new JLabel("注册帐号");
	private boolean type = false;

	private SImageControl userPhotoControl = new SImageControl(45, 165, 80, 80, "photo.png");
	
	private SButtonControl aboutControl = new SButtonControl(380, 260, 25, 25, "about-*.png");	//登录
	
	public ClientLoginForm() {
		//初始化客户端类
		this.userModule = new Client();
		
		//窗体设置
		this.setPosition(this, 420, 320);

		this.initialize();
		this.initializeUI(BaseModule.projectName, "client-login-title.png", "client-login-background.png");
		this.setTitle("");
	}

	private void initialize() {
		this.userPhotoControl();
		this.userNameControl();
		this.passwordControl();
		this.typeControl();
		this.aboutButtonControl();
	}

	//帐号框
	private void userNameControl() {
		this.setSize(this.userNameControl, 142, 165, 168, 29);
		this.userNameControl.setBorder(null);
		this.add(this.userNameControl);
		
		this.userNameControl.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e){
                if(userNameControl.getText().equals("帐号")){
                	userNameControl.setText("");
                }
                
            	if(getPassword().equals("")){
                	passwordControl.setVisible(true);
                	passwordRealControl.setVisible(false);
            	}
			}
			
            public void focusLost(FocusEvent e){
                if(userNameControl.getText().equals("")){
                	userNameControl.setText("帐号");
                }
            }
        });

	}
	
	//密码框
	private void passwordControl() {    	
		this.setSize(this.passwordControl, 142, 195, 168, 30);
		this.passwordControl.setBorder(null);
		this.add(this.passwordControl);
		
		this.setSize(this.passwordRealControl, 142, 195, 168, 30);
		this.passwordRealControl.setBorder(null);
		this.passwordRealControl.setVisible(false);
		this.add(this.passwordRealControl);
		
		this.passwordControl.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e){
            	passwordControl.setVisible(false);
            	passwordRealControl.setVisible(true);
            	passwordRealControl.setFocusable(true);
            	passwordRealControl.requestFocus();
			}

        });
		
		this.passwordRealControl.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e){
            	if(getPassword().equals("")){
            		passwordControl.setVisible(true);
            		passwordRealControl.setVisible(false);
            	}

            }
        });
	}
	
	//状态控制器
	private void typeControl() {
		this.add(this.typeButtonControl);
		
		this.setSize(this.typeControl, 325, 165, 50, 25);
    	this.typeControl.setForeground(new Color(229, 98, 129));
		this.add(this.typeControl);
		
		//控件事件
		this.typeControl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	typeAction();
            }
            
            public void mouseEntered(MouseEvent e){
            	typeControl.setForeground(new Color(249, 80, 112));
            	setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));  
            }
            
            public void mouseExited(MouseEvent e){
            	typeControl.setForeground(new Color(229, 98, 129));
            	setCursor(Cursor.getDefaultCursor());  
            }
        });
		
		this.typeButtonControl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
    			if(userNameControl.getText().equals("") || getPassword().equals("")){
    				BaseModule.messageBox("不能提交空数据！", 0);
    				return;
    			}
    			
        		if(!type){
        			//登录
        			if(userModule.loginUser(userNameControl.getText(), getPassword())){
        				BaseModule.messageBox("登录成功！", 1);
        				new ClientMainForm(userModule.getUserID(), userModule.getUserName());
        				setVisible(false);
        			}else{
        				BaseModule.messageBox("无法登录到该帐号！", 0);
        			}
        		}else{
        			//注册
    				if(userModule.registerUser(userNameControl.getText(), getPassword())){
        				type = true;
        				typeAction();
        				BaseModule.messageBox("注册成功！请使用注册的帐号和密码登录。", 1);
    				}else{
        				BaseModule.messageBox("用户已存在！", 0);
    				}
        		}
            }
        });

	}
	
	//用户头像
	private void userPhotoControl(){
		this.userPhotoControl.setFocusable(true);
		this.add(this.userPhotoControl);
	}
	
	protected void aboutButtonControl(){
		this.add(this.aboutControl);

		//控件事件
		this.aboutControl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
        		aboutForm();
            }
        });
	}

	private void typeAction(){
		if(this.type){
			this.typeButtonControl.changeImage("client-login-*.png");
			this.typeControl.setText("注册帐号");
			this.type = false;
		}else{
			this.typeButtonControl.changeImage("register-*.png");
			this.typeControl.setText("已有帐号");
			this.type = true;
		}
	}
	
	//密码框获取文本
	private String getPassword(){
    	return new String(this.passwordRealControl.getPassword());
	}

}
