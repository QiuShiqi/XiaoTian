package Module.User;

import Module.*;
import Module.Base.*;

import java.util.*;

public class Server extends UserModule{
	
	public Server(){
		
	}
	
	public Server(int userID, String userName){
		super(userID, userName);
	}
	
	public boolean loginUser(String userName, String password){

		try{
			ResultList sql = (ResultList)Controller.dataSource.selectData("select * from xiaotian_user where username = '" + userName + "' and password = '" + password + "' and usergroup = 'admin' and type = 'default'");
			
			if(sql != null){
				sql.next();
				this.userID = sql.getInt("id");
				this.userName = sql.getString("username");
				boolean u = userName.equals(this.userName);
				boolean p = password.equals(sql.getString("password"));
				if(u && p){
					return true;
				}else{
					return false;
				}
			}
		}catch (Exception exception) {
			
		}
		
		return false;
	}

	//注册用户
	public boolean registerUser(String userName, String password){
		return false;
	}
	
	public void dataListener(){
		
	}
	
	//发送消息
	public boolean sendMessage(int userID, String content){
		String sql = "insert into xiaotian_system values(xiaotian_system_add.nextval, " + userID + ", '" + content + "', sysdate, 'message')";
		return Controller.dataSource.addData(sql);
	}
	
	//添加好友
	public boolean addUser(int userID){
		return false;
	}
	
	//删除用户
	public boolean removeUser(int userID){
		this.sendMessage(userID, "offline");
		
		//因为有延迟所以使用假删除 
		/*
		//删除相关的公告
		Controller.dataSource.deleteData("delete from xiaotian_system where userid = " + userID);
		
		//删除好友关系
		Controller.dataSource.deleteData("delete from xiaotian_friend where friendid = " + userID + " or userid = " + userID);
		
		//删除聊天记录
		Controller.dataSource.deleteData("delete from xiaotian_record where friendid = " + userID + " or userid = " + userID);
		
		//删除账号
		String sql = "delete from xiaotian_user where id = " + userID;
		*/
		
		String sql = "update xiaotian_user set type = 'delete' where id = '" + userID + "'";
		return Controller.dataSource.modifyData(sql);

	}
	
	//搜索好友
	public HashMap<Integer, String> searchFriend(String searchKey){
		HashMap<Integer, String> tmp = new HashMap<Integer, String>();		
		return tmp;
	}
	
	//用户列表
	public UserList listUser(){
		UserList tmp = new UserList(new String[]{ "ID", "用户名", "用户组", "注册时间", "注册IP", "状态" });

		try{

			ResultList sql = (ResultList)Controller.dataSource.selectData("select id, username, usergroup, to_char(time, 'yyyy-mm-dd hh24:mi:ss') as time, ip, type from xiaotian_user where usergroup != 'admin' and type != 'delete'");
			if(sql != null){
				while(sql.next()){
					int userID = 10000 + sql.getInt("id");
					String userName = sql.getString("username");
					String userGroup = (sql.getString("usergroup") != "user") ? "普通用户" : "管理员";
					String time = sql.getString("time");
					String IP = sql.getString("ip");
					String type = (sql.getString("type") != "default") ? "正常" : "冻结";
					
					tmp.addRow(new String[]{ userID + "", userName, userGroup, time, IP, type });
				}
			}

		}catch (Exception exception) {
			return null;
		}
		
		return tmp;
	}
	
	//抓取所有好友的聊天记录
	public HashMap<Integer, Vector<RecordArray>> catchAllRecord(){
		HashMap<Integer, Vector<RecordArray>> tmp = new HashMap<Integer, Vector<RecordArray>>();
		return tmp;
	}
	
	//抓取好友的一条聊天记录
	public RecordArray catchRecord(int userID){
		RecordArray tmp = null;
		return tmp;
	}
	
	//冻结
	public boolean freezeUser(int userID){
		String sql = "update xiaotian_user set type = 'freeze' where id = '" + userID + "'";

		return (this.sendMessage(userID, "offline") && Controller.dataSource.modifyData(sql));
	}
	
	//解冻
	public boolean thawUser(int userID){
		String sql = "update xiaotian_user set type = 'default' where id = '" + userID + "'";
		return Controller.dataSource.modifyData(sql);
	}

	//改变服务器状态
	public boolean changeSystemType(boolean type){
		String tmp = type ? "on" : "off";
		
		String sql = "update xiaotian_system set content = '" + tmp + "' where type = 'system_type'";

		return Controller.dataSource.modifyData(sql);
	}
	
	//push用的抓取最新一条聊天记录
	public RecordArray pushRecord(int id){
		RecordArray tmp = null;
		return tmp;
	}
	
	//push用的抓取最新一条推送的消息
	public String pushSystemType(int id){
		String tmp = null;
		return tmp;
	}
	
	//push用的抓取最新一个用户
	public Vector<String> pushFriend(int id){
		Vector<String> tmp = new Vector<String>();

		try{

			ResultList sql = (ResultList)Controller.dataSource.selectData("select id, username, usergroup, to_char(time, 'yyyy-mm-dd hh24:mi:ss') as time, ip, type from xiaotian_user where id = " + id);
			if(sql != null){
				sql.next();
				int userID = 10000 + sql.getInt("id");
				String userName = sql.getString("username");
				String userGroup = (sql.getString("usergroup") != "user") ? "普通用户" : "管理员";
				String time = sql.getString("time");
				String IP = sql.getString("ip");
				String type = (sql.getString("type") != "default") ? "正常" : "冻结";
				
				tmp.add(userID + "");
				tmp.add(userName);
				tmp.add(userGroup);
				tmp.add(time);
				tmp.add(IP);
				tmp.add(type);
				tmp.add(userName);

			}

		}catch (Exception exception) {
			return null;
		}
		
		return tmp;
	}
	
	//push用的返回最新消息
	public int getFresh(int timeLock, int type){
		switch(type){
			case 1:
				return this.getFresh1(timeLock);
			case 2:
				return this.getFresh2(timeLock);
		}
		return 0;
	}
	

	private int getFresh1(int timeLock){
		return 0;
	}
	
	//获取最新的用户
	private int getFresh2(int timeLock){
		try{
			//通过好友的ID获取彼此的新好友
			ResultList sql = (ResultList)Controller.dataSource.selectData("select id, to_char(time, 'yyyy-MM-dd HH24:mi:ss') as time from xiaotian_user order by id desc");
			if(sql != null){
				//将好友之间的聊天记录临时存放起来
				sql.next();
				
				int tmp = BaseModule.stringToTime(sql.getString("time"));
				if(tmp > timeLock){
					return sql.getInt("id");
				}
			}
		}catch (Exception exception) {
		}
		return 0;
	}
}
