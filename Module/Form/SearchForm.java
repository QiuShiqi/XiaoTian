package Module.Form;

import javax.swing.*;
import javax.swing.plaf.basic.BasicListUI;

import java.awt.event.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Vector;

import Module.*;
import Module.User.*;
import Module.DirectUI.*;

public class SearchForm extends BaseUI {
	//数据
	private HashMap<String, String> userPhotoList = new HashMap<String, String>();	//好友头像控件的容器
	private DefaultListModel<String> userList = new DefaultListModel<String>();	//好友控件的容器
	private Vector<Integer> userIDList = new Vector<Integer>();	//好友ID列表
	
	//控件
	private JTextField searchTextControl = new JTextField("请输入XiaoTian号/昵称来进行搜索好友！");
	private SButtonControl searchButtonControl = new SButtonControl(395, 5, 95, 30, "search2-*.png");	//搜索
	private SButtonControl addButtonControl = new SButtonControl(395, 40, 95, 30, "add-*.png");	//添加
	private SButtonControl aboutButtonControl = new SButtonControl(395, 235, 95, 30, "about3-*.png");	//关于

	private JList<String> userListControl = new JList<String>(userList);
	private JScrollPane userListControlScrollControl = new JScrollPane(userListControl);
	
	public SearchForm(int userID, String userName) {
		//初始化客户端类
		this.userModule = new Client(userID, userName);
		
		//窗体设置
		this.setPosition(this, 500, 300);

		this.initialize();
		this.initializeUI("查找", "search-title.png", "search-background.png");
		//this.backgroundControl.changeColor(new Color(235, 242, 249));
		this.titleControl.changeColor(new Color(255, 255, 255));
		
		this.searchButtonControl.requestFocus();
	}
	
	private void initialize() {
		this.searchControl();
		this.addButtonControl();
		this.aboutButtonControl();
		this.userlistControl();
	}
	
	//帐号框
	private void searchControl() {

		this.setSize(this.searchTextControl, 5, 5, 385, 30);
		this.add(this.searchTextControl);

		this.add(this.searchButtonControl);
		
		this.searchTextControl.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e){
                if(searchTextControl.getText().equals("请输入XiaoTian号/昵称来进行搜索好友！")){
                	searchTextControl.setText("");
                }
			}
			
            public void focusLost(FocusEvent e){
                if(searchTextControl.getText().equals("")){
                	searchTextControl.setText("请输入XiaoTian号/昵称来进行搜索好友！");
                }
            }
        });
		
		//控件事件
		this.searchButtonControl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
				if(!searchTextControl.getText().equals("请输入XiaoTian号/昵称来进行搜索好友！")){
					search();
					
				}else{
					BaseModule.messageBox("关键字不能为空！", 0);
				}

			}
		});

	}

	//添加按钮
	private void addButtonControl() {
		this.add(this.addButtonControl);
		
		//控件事件
		this.addButtonControl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	int index = userListControl.getSelectedIndex();

            	if(index >= 0){
                	if(!userModule.addUser(indexToUserID())){
    					BaseModule.messageBox("无法添加该用户为好友！", 0);
                	}

       				String name = userListControl.getModel().getElementAt(index).toString();
    				userPhotoList.remove(name);
    				
    				userList.removeElementAt(index);
    				userIDList.remove(index);//记录好友id

    				//更新控件
    				userListControl.repaint();
    				
            	}else{
					BaseModule.messageBox("请选择需要添加为好友的用户！", 0);
            	}

            }

        });
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
	
	protected void aboutButtonControl(){
		this.add(this.aboutButtonControl);

		//控件事件
		this.aboutButtonControl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
        		aboutForm();
            }
        });
	}
	
	//好友列表
	private void userlistControl(){
		ListUI ui = new ListUI();
		this.userListControl.setUI(ui);
		this.userListControl.setCellRenderer(new JListRenderer());
		this.setSize(this.userListControlScrollControl, 5, 40, 385, 225);
		this.add(this.userListControlScrollControl);
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
	
	protected void close(){
		
	}
	
	//搜索操作
	private void search(){
		//清空容器
		this.userIDList.clear();
		this.userList.clear();
		this.userListControl.repaint();
		
		//获取数据库数据
		HashMap<Integer, String> tmp = userModule.searchFriend(this.searchTextControl.getText());
		if(!tmp.isEmpty()){
			for(int index : tmp.keySet()){
				this.userIDList.add(index);	//记录好友id
				this.userList.addElement(tmp.get(index));
				this.userPhotoList.put(tmp.get(index), "photo.png");
			}
			
			this.userListControl.repaint();
			
		}else{
			BaseModule.messageBox("找不到相关的用户！", 0);
		}
	}
}
