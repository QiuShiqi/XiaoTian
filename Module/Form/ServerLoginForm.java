package Module.Form;

import java.awt.event.*;

import javax.swing.*;

import Module.BaseModule;
import Module.DirectUI.*;
import Module.User.*;

public class ServerLoginForm extends BaseUI {
	
	private JTextField userNameControl = new JTextField("帐号");
	private JTextField passwordControl = new JTextField("密码");
	private JPasswordField passwordRealControl = new JPasswordField();
	
	private SButtonControl submitButtonControl = new SButtonControl(90, 245, 225, 40, "server-login-*.png");	//登录
	
	public ServerLoginForm(){
		//初始化服务端类
		this.userModule = new Server();
		
		//窗体设置
		this.setPosition(this, 800, 440);
		
		this.initialize();
		this.initializeUI(BaseModule.projectName, "server-login-title.png", "server-login-background.png");
		this.setTitle("");

	}
	
	public void initialize(){
		this.userNameControl();
		this.passwordControl();
		this.submitButtonControl();
	}

	//帐号框
	private void userNameControl() {
		this.setSize(this.userNameControl, 100, 140, 205, 30);
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
		this.setSize(this.passwordControl, 100, 190, 205, 30);
		this.passwordControl.setBorder(null);
		this.add(this.passwordControl);
	
		
		this.setSize(this.passwordRealControl, 100, 190, 205, 30);
		this.passwordRealControl.setVisible(false);
		this.passwordRealControl.setBorder(null);
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
	
	//密码框获取文本
	private String getPassword(){
    	return new String(this.passwordRealControl.getPassword());
	}
	
	//状态控制器
	private void submitButtonControl() {
		//消除Swing的漏洞
		JLabel typeControl = new JLabel("");
		typeControl.setFocusable(true);
		this.add(typeControl);
		
		this.add(this.submitButtonControl);
		
		this.submitButtonControl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
    			if(userNameControl.getText().equals("") || getPassword().equals("")){
    				BaseModule.messageBox("不能提交空数据！", 0);
    			}else{
        			if(userModule.loginUser(userNameControl.getText(), getPassword())){
        				BaseModule.messageBox("登录成功！", 1);
        				new ServerMainForm(userModule.getUserID(), userModule.getUserName());
        				setVisible(false);
        			}else{
        				BaseModule.messageBox("无法登录到该帐号！", 0);
        			}
    			}
            }
        });

	}
}
