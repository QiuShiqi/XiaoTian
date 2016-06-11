package Module.Form;

import javax.swing.*;
import javax.swing.plaf.basic.BasicListUI;

import java.awt.event.*;
import java.awt.*;
import java.util.Vector;
import java.util.HashMap;

import Module.*;
import Module.Base.*;
import Module.Data.*;
import Module.DirectUI.*;
import Module.User.*;

public class ClientMainForm extends BaseUI {
	//数据
	private HashMap<String, String> userPhotoList = new HashMap<String, String>();	//好友头像控件的容器
	private DefaultListModel<String> userList = new DefaultListModel<String>();	//好友控件的容器
	private Vector<Integer> userIDList = new Vector<Integer>();	//好友ID列表
	private HashMap<Integer, Vector<RecordArray>> userRocordList = null;	//聊天记录容
	
	//控件
	private JTextPane contentControl = new JTextPane();
	private JScrollPane contentScrollControl = new JScrollPane(this.contentControl);
	
	private JTextPane sendControl = new JTextPane();
	private JScrollPane sendScrollControl = new JScrollPane(this.sendControl);

	
	private SButtonControl submitControl = new SButtonControl(375, 425, 75, 23, "send-*.png");	//发送
	private SButtonControl clearControl = new SButtonControl(290, 425, 75, 23, "clear-*.png");	//清空

	private JList<String> userListControl = new JList<String>(userList);
	private JScrollPane userListControlScrollControl = new JScrollPane(userListControl);

	private SImageControl userPhotoControl = new SImageControl(460, 7, 60, 60, "photo.png");
	private JLabel userNameControl = new JLabel("测试用户");
	
	private SButtonControl searchButtonControl = new SButtonControl(465, 425, 75, 23, "search-*.png");	//查找
	private SButtonControl aboutButtonControl = new SButtonControl(555, 425, 75, 23, "about2-*.png");	//关于
	
	public ClientMainForm(int userID, String userName) {
		//初始化客户端类
		this.userModule = new Client(userID, userName);
		this.pushRecord();	//聊天消息推送线程
		this.pushFriend();	//好友请求推送线程
		this.pushSystemType();	//消息推送推送线程
		
		//窗体设置
		this.setPosition(this, 640, 480);
		
		this.initialize();
		this.initializeUI(BaseModule.projectName, "client-main-title.png", "client-main-background.png");
		this.titleControl.changeMinImages("title2-min-*.png");
		this.titleControl.changeCloseImages("title2-close-*.png");

		//获取数据库数据
		HashMap<Integer, String> tmp = (HashMap<Integer, String>)userModule.listUser();

		for(int index : tmp.keySet()){
			this.userIDList.add(index);	//记录好友id
			this.userList.addElement(tmp.get(index));
			this.userPhotoList.put(tmp.get(index), "photo.png");
		}
		
		this.userListControl.repaint();
		
		//获取所有聊天记录
		this.userRocordList = this.userModule.catchAllRecord();
		
		//选中默认对话
		if(this.userIDList.size() > 0){
			this.userListControl.setSelectedIndex(0);
			this.setTabShow(0);
		}else{
			this.submitControl.setEnabled(false);
		}

	}

	private void initialize() {
		//初始化控件
		this.userlistControl();
		this.usernameControl();
		this.userPhotoControl();
		
		this.contentControl();
		this.sendControl();
		this.submitControl();
		this.clearControl();
		this.searchButtonControl();
		this.aboutButtonControl();
	}

	// 消息文本框
	private void contentControl() {
		this.contentControl.setEditable(false);
		this.contentControl.setContentType("text/html");
		this.contentControl.setBackground(new Color(253, 239, 243));

		//new FixTextBug(contentControl);
		this.setSize(contentScrollControl, 5, 0, 450, 300);
		this.contentScrollControl.setBorder(null);
		this.add(this.contentScrollControl);
		
	}

	// 发送文本框
	private void sendControl() {
		this.sendControl.setBackground(new Color(253, 239, 243));

		//new FixTextBug(sendControl);
		this.setSize(this.sendScrollControl, 5, 320, 450, 100);
		this.sendScrollControl.setBorder(null);
		this.add(this.sendScrollControl);
	}

	// 发送按钮
	private void submitControl() {
		this.add(this.submitControl);

		this.submitControl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	if(submitControl.getEnabled()){
    				if(!sendControl.getText().equals("")){
    					int userID = indexToUserID();
    					userModule.sendMessage(userID, sendControl.getText());
    					sendControl.setText("");	//清空发送框
    				}
            	}
			}

		});

	}

	//清空按钮
	private void clearControl() {
		this.add(this.clearControl);

		this.clearControl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
				sendControl.setText("");
			}

		});
	}

	//好友列表
	private void userlistControl(){
		ListUI ui = new ListUI();
		this.userListControl.setUI(ui);
		this.userListControl.setCellRenderer(new JListRenderer());
		
		this.userListControlScrollControl.setBorder(null);
		this.userListControl.setBackground(new Color(253, 239, 243));
		this.setSize(this.userListControlScrollControl, 456, 75, 183, 345);
		this.add(this.userListControlScrollControl);
		
		//控件事件
		this.userListControl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1 && userIDList.size() > 0){
                	int index = userListControl.getSelectedIndex();
                	setTabShow(index);	//切换tab
                }
                
                if(e.getButton() != MouseEvent.BUTTON1 && userIDList.size() > 0){
            		Object[] options = { "确定", "取消" };
            		
            		int index = userListControl.getSelectedIndex();
            		String name = userListControl.getModel().getElementAt(index).toString();

            		int result = JOptionPane.showOptionDialog(null, "确定要将好友[" + name +"]移出列表吗？", "提示 - " + BaseModule.projectName, JOptionPane.YES_OPTION, 2, null, options, options[0]);

            		switch(result){
            		case 0:	//确定
            			if(userModule.removeUser(indexToUserID())){
                			userRocordList.remove(indexToUserID());
                			userPhotoList.remove(name);
                			userList.removeElement(name);
                			userIDList.remove(index);
                			userListControl.repaint();
                			
                			if(userIDList.size() > 0){
                				userListControl.setSelectedIndex(0);
                				setTabShow(0);
                			}else{
                				contentControl.setText("");
                				sendControl.setText("");
                				submitControl.setEnabled(false);
                				setTitle(BaseModule.projectName);
                			}
                			
                			
            			}else{
            				BaseModule.messageBox("遇到未知原因，请稍后再尝试！", -1);
            			}

            			break;
            		}
            		
                }
            }
        });
	}
	
	//好友列表控件重写
	class JListRenderer extends DefaultListCellRenderer {
	    private Font font = new Font(Font.DIALOG, 0, 12);
	    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	    	ListImageControl label = new ListImageControl(0, 0, 30, 30, "photo.png", value.toString());
	    	
	        if (isSelected) {
	        	((ListUI)list.getUI()).setCellHeight(index, 46, 26);
	        	label.changeSize(40, 40);
	        }else{
	        	((ListUI)list.getUI()).setCellHeight(index, 26);
	        	label.changeSize(20, 20);
	        }
	        return label;
	    }
	}
	
	class ListUI extends BasicListUI {   
        public ListUI() {      
            super();         
            cellHeights = new int[2];   
        }   
        public void setCellHeight(int index, int value, int defaultHeight) {      
            for (int i = 0; i < cellHeights.length; i++) {   
                cellHeights[i] = defaultHeight;    
            }         
            cellHeights[index] = value;   
        }   
        void setCellHeight(int index, int i) {  
            cellHeights[index] = i;   
        }  
    }

	//用户信息
	private void usernameControl(){		
		this.userNameControl.setText("<html><div style='color:white;'><b>" + this.userModule.getUserName() + "</b></div><html>");
		this.setSize(this.userNameControl, 530, 0, 170, 25);
		this.add(this.userNameControl);
	}

	//用户头像
	private void userPhotoControl(){
		this.add(this.userPhotoControl);
		
		SImageControl box = new SImageControl(455, 0, 184, 75, "userContent.png");
		this.add(box);
	}
	
	//tab处理
	private void setTabShow(int index){
		String name = this.userListControl.getModel().getElementAt(index).toString();
    	this.setTitle(BaseModule.projectName + " - 与" + name + "聊天中");

		if(this.userRocordList != null){
			String box = "";
			int userID = this.userIDList.get(index);
			
			Vector<RecordArray> recordArray = this.userRocordList.get(userID);
			if(recordArray != null){
				String source = null;
				for(RecordArray record : recordArray){
					if(record.source != this.userModule.getUserID()){
						source = name;
					}else{
						source = this.userModule.getUserName();
					}
					box += "<div style='font-size:10px;font-family:宋体;'><div style='color:blue;padding-bottom:5px;'><span>" + source + "(</span><span style='color:#0072c1;text-decoration:underline;'>" + (10000 + record.source) + "</span><span>) " + record.time + "</span></div><div<div style='color:black;padding-bottom:5px;'>" + record.content + "</div></div>";
				}
				this.contentControl.setText(box);
				this.contentControl.setCaretPosition(this.contentControl.getDocument().getLength());
			}else{
				BaseModule.messageBox("a", -1);
			}

		}
	}
	
	//由ID转变为索引
	public int indexToUserID(){
		int index = this.userListControl.getSelectedIndex();
		return this.userIDList.get(index);
	}
	
	//由索引转变为ID
	public int userIDToIndex(int userID){
		for(int i = 0; i < this.userIDList.size(); i++){
			if(this.userIDList.get(i) == userID){
				return i;
			}
		}
		return -1;
	}
	
	//搜索按钮
	private void searchButtonControl(){
		this.add(this.searchButtonControl);

		//控件事件
		this.searchButtonControl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
        		new SearchForm(userModule.getUserID(), userModule.getUserName());
            }
        });
	}
	
	protected void aboutButtonControl(){
		this.add(this.aboutButtonControl);

		//控件事件
		this.aboutButtonControl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
        		aboutForm();
            }
            
        });
	}
	
	protected void close(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	//添加聊天记录
	
	/*
	public void addRecord(int userID){
		RecordArray tmp = this.client.catchRecord(userID);

		this.userRocordList.get(userID).add(tmp);
		this.setTabShow(this.userIDToIndex(userID));
	}
	*/
	
	public void addRecord(int id){
		RecordArray tmp = this.userModule.pushRecord(id);
		
		if(tmp != null){
			int index = tmp.source;
			if(tmp.target != this.userModule.getUserID()){
				index = tmp.target;
			}
			
			try{	//以防单向好友发送消息
				this.userRocordList.get(index).add(tmp);
				
				index = this.userIDToIndex(index);
				this.userListControl.setSelectedIndex(index);
				this.setTabShow(index);
			}catch(Exception e){
				
			}

		}

		this.pushRecord();	//递归调用
		
	}
	
	public void addFriend(int id){
		String name = (String)this.userModule.pushFriend(id);
		this.userIDList.add(id);	//记录好友id
		this.userList.addElement(name);
		this.userPhotoList.put(name, "photo.png");
		
		this.userRocordList.put(new Integer(id), new Vector<RecordArray>());
		this.userListControl.repaint();

		if(this.userIDList.size() > 0){
			this.submitControl.setEnabled(true);
			this.userListControl.setSelectedIndex(0);
			this.setTabShow(0);
		}
		
		BaseModule.messageBox("新好友[" + name + "]已被添加到好友列表！", 1);
		this.pushFriend();	//递归调用
	}
	
	public void systemType(int id){
		String tmp = this.userModule.pushSystemType(id);
		
		//逐出服务器
		if(tmp.equals("offline")){
			BaseModule.messageBox("目前使用的帐号被服务器强制下线！", -1);
			System.exit(0);
		}
		
		BaseModule.messageBox(tmp, 1);

		this.pushSystemType();	//递归调用
	}
	
	private void pushRecord(){
		(new ServerPush(this.userModule, this, 1)).start();
	}
	
	private void pushFriend(){
		(new ServerPush(this.userModule, this, 2)).start();
	}
	
	private void pushSystemType(){
		(new ServerPush(this.userModule, this, 3)).start();
	}
}
