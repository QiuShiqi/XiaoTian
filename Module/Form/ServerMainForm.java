package Module.Form;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;

import Module.*;
import Module.Base.*;
import Module.Data.ServerPush;
import Module.DirectUI.*;
import Module.User.*;

public class ServerMainForm extends BaseUI {
	//数据
	private UserList userList = new UserList(new String[]{ "ID", "用户名", "用户组", "注册时间", "注册IP", "状态" });
	public static int userListSelectedIndex = 0;
	
	//控件
	private SButtonControl serviceControl = new SButtonControl(45, 121, 115, 30, "server-service-*.png");	//维护
	private SButtonControl restoreControl = new SButtonControl(181, 121, 115, 30, "server-restore-*.png");	//还原
	
	private JTextPane messageControl = new JTextPane();
	private JScrollPane messageScrollControl = new JScrollPane(this.messageControl);
	
	private SButtonControl clearControl = new SButtonControl(45, 301, 115, 30, "server-clear-*.png");	//清空
	private SButtonControl sendControl = new SButtonControl(181, 301, 115, 30, "server-send-*.png");	//发送
	
	private SButtonControl freezeControl = new SButtonControl(336, 301, 115, 30, "server-freeze-*.png");	//冻结
	private SButtonControl thawControl = new SButtonControl(471, 301, 115, 30, "server-thaw-*.png");	//解冻
	private SButtonControl deleteControl = new SButtonControl(640, 301, 115, 30, "server-delete-*.png");	//删除

	private JTable userListControl = new JTable(userList);
	private JScrollPane userListScrollControl = new JScrollPane(userListControl);

	public ServerMainForm(int userID, String userName) {
		//初始化服务端类
		this.userModule = new Server(userID, userName);
		this.pushFriend();	//新用户推送线程
		
		//窗体设置
		this.setPosition(this, 800, 440);
		
		this.initialize();
		this.initializeUI(BaseModule.projectName, "server-main-title.png", "server-main-background.png");
		this.setTitle("");

		//抓取用户数据
		UserList tmp = (UserList)this.userModule.listUser();
		for(int i = 0; i < tmp.getRowCount(); i++){
			Vector<String> item = new Vector<String>();
			for(int j = 0; j < tmp.getColumnCount(); j++){
				item.add(tmp.getValueAt(i, j).toString());
			}
			this.userList.addRow(item);
		}

		this.userListControl.repaint();
		
		//禁用还原功能
		this.restoreControl.setEnabled(false);
		
		if(!this.userModule.getSystemType()){
			serviceControl.changeImage("server-open-*.png");
		}
	}
	
	public void initialize(){
		this.serverControl();
		this.messageControl();
		this.userControl();
		this.userListControl();
		
	}

	public void serverControl(){
		this.add(this.serviceControl);
		this.add(this.restoreControl);
		
		this.serviceControl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	boolean type = true;
            	if(userModule.getSystemType()){
            		type = false;
            	}

        		if(userModule.changeSystemType(type)){
        			String tmp = type ? "开启" : "关闭";
        			String src = type ? "service" : "open";
        			
        			serviceControl.changeImage("server-" + src + "-*.png");
        			BaseModule.messageBox("服务器已经" + tmp + "！", 1);
        		}else{
            		BaseModule.messageBox("出现致命错误！", -1);
            		System.exit(0);
        		}
            }
        });
		
		this.restoreControl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

            }
        });
	}
	
	//消息推送
	public void messageControl(){
		this.messageScrollControl.setBorder(null);
		this.setSize(this.messageScrollControl, 47, 223, 247, 66);
		this.add(this.messageScrollControl);
		
		this.add(this.clearControl);
		this.add(this.sendControl);
		
		this.clearControl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	messageControl.setText("");
            }
        });
		
		this.sendControl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	String content = messageControl.getText();
            	if(!content.equals("")){
            		userModule.sendMessage(1, content);	//1为全局
            		messageControl.setText("");
            		BaseModule.messageBox("该消息已成功推送！", 1);
            	}
            }

        });

	}

	public void userControl(){
		this.add(this.freezeControl);
		this.add(this.thawControl);
		this.add(this.deleteControl);
		
		//冻结事件
		this.freezeControl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	int index = userListControl.getSelectedRow();
            	if(index != -1){
            		if(userModule.freezeUser(getUserID(index))){
                    	userList.setValueAt("冻结", index, 5);
            		}else{
                		BaseModule.messageBox("出现致命错误！", -1);
                		System.exit(0);
            		}

            	}else{
            		BaseModule.messageBox("请先选择需要冻结的帐号！", 0);
            	}

            }
        });
		
		//解冻事件
		this.thawControl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	int index = userListControl.getSelectedRow();
            	if(index != -1){
            		if(userModule.thawUser(getUserID(index))){
                    	userList.setValueAt("正常", index, 5);
            		}else{
                		BaseModule.messageBox("出现致命错误！", -1);
                		System.exit(0);
            		}

            	}else{
            		BaseModule.messageBox("请先选择需要解冻的帐号！", 0);
            	}

            }
        });
		
		//删除事件
		this.deleteControl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	
            	int index = userListControl.getSelectedRow();
            	if(index != -1){
            		if(userModule.removeUser(getUserID(index))){
            			userList.removeRow(index);
                		BaseModule.messageBox("该帐号已被删除！", 1);
            		}else{
                		BaseModule.messageBox("出现致命错误！", -1);
                		System.exit(0);
            		}
                	
            	}else{
            		BaseModule.messageBox("请先选择需要删除的帐号！", 0);
            	}

            }

        });
	}
	
	public void userListControl(){
		this.userListScrollControl.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.userListScrollControl.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.userListScrollControl.getViewport().setBackground(new Color(255, 255, 255));

		this.userListControl.setAutoResizeMode(0);
		this.userListControl.getTableHeader().setPreferredSize(new Dimension(10000, 30));	//10000为宽度
		this.userListControl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.userListControl.getTableHeader().setReorderingAllowed(false);	//拖动
		this.userListControl.getTableHeader().setResizingAllowed(false);	//拉动
		
		this.userListControl.setRowHeight(25);
		this.userListControl.setShowGrid(false);
		
		this.userListControl.getColumnModel().getColumn(0).setPreferredWidth(60);
		this.userListControl.getColumnModel().getColumn(1).setPreferredWidth(120);
		this.userListControl.getColumnModel().getColumn(2).setPreferredWidth(80);
		this.userListControl.getColumnModel().getColumn(3).setPreferredWidth(150);
		this.userListControl.getColumnModel().getColumn(4).setPreferredWidth(100);
		this.userListControl.getColumnModel().getColumn(5).setPreferredWidth(60);
		
		this.setSize(this.userListScrollControl, 336, 121, 419, 170);
		this.add(this.userListScrollControl);
	}
	
	//添加一行
	private void addRow(){
    	this.userList.addRow(new String[]{ "10001", "用户名", "user", "2016-01-10 20:16:01", "正常" });
    	this.userListControl.repaint();

    	//滚到最下面
    	int index = this.userListControl.getSelectedRow();
    	
        int rowCount = this.userListControl.getRowCount();
        Rectangle rect = this.userListControl.getCellRect(rowCount - 1, 0, true);
        this.userListControl.scrollRectToVisible(rect);
        
    	if(index != -1){
            this.userListControl.setRowSelectionInterval(index, index);
    	}
	}
	
	private int getUserID(int index){
		try{
			String tmp = this.userListControl.getValueAt(index, 0).toString();
			return Integer.parseInt(tmp) - 10000;
			
		}catch(Exception e){
			return -1;
		}
	}

	private void pushFriend(){
		(new ServerPush(this.userModule, this, 2)).start();
	}
	
	public void addFriend(int id){
		Vector tmp = (Vector)this.userModule.pushFriend(id);
		this.userList.addRow(tmp);

		this.userListControl.repaint();
		this.pushFriend();	//递归调用
	}
}
