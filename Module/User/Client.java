package Module.User;

import Module.*;
import Module.Base.*;

import java.net.InetAddress;
import java.util.*;

public class Client extends UserModule{
	public static int timeDifference = 0;
	public static int lastRecordID = 0;
	
	public Client(){
		
	}
	
	public Client(int userID, String userName){
		super(userID, userName);
	}
	
	public boolean loginUser(String userName, String password){

		try{
			ResultList sql = (ResultList)Controller.dataSource.selectData("select to_char(sysdate, 'yyyy-MM-dd HH24:mi:ss') as serverTime, id, username, password from xiaotian_user where username = '" + userName + "' and password = '" + password + "' and usergroup = 'user' and type = 'default' and (select content from xiaotian_system where type = 'system_type') != 'off'");
			
			if(sql != null){
				sql.next();
				
				//时间差
				Client.timeDifference = BaseModule.stringToTime(sql.getString("serverTime")) - BaseModule.getTime();

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
		try{
			return Controller.dataSource.addData("insert into xiaotian_user values(xiaotian_user_add.nextval, '" + userName + "', '" + password + "', 'user', sysdate, '" + InetAddress.getLocalHost().getHostAddress() + "','default')");
		}catch(Exception e){
			return false;
		}
		
	}
	
	public void dataListener(){
		
	}
	
	//发送消息
	public boolean sendMessage(int userID, String content){
		String sql = "insert into xiaotian_record values(xiaotian_record_add.nextval, " + this.userID + ", " + userID + ", '" + content + "', sysdate, 'default')";
		return Controller.dataSource.addData(sql);
	}
	
	//删除好友
	public boolean removeUser(int userID){
		int i = 0;
		
		String sql = "delete from xiaotian_friend where friendid = " + this.userID + " and userid = " + userID;
		if(Controller.dataSource.deleteData(sql)){
			i++;
		}
		
		sql = "delete from xiaotian_friend where friendid = " + userID + " and userid = " + this.userID;
		if(Controller.dataSource.deleteData(sql)){
			i++;
		}
		
		if(i != 2){
			return false;
		}else{
			return true;
		}

	}

	//添加好友
	public boolean addUser(int userID){
		int i = 0;
		
		String sql = "insert into xiaotian_friend values(xiaotian_friend_add.nextval, " + this.userID + ", " + userID + ", sysdate, 'default')";
		if(Controller.dataSource.addData(sql)){
			i++;
		}
		
		sql = "insert into xiaotian_friend values(xiaotian_friend_add.nextval, " + userID + ", " + this.userID + ", sysdate, 'default')";
		if(Controller.dataSource.addData(sql)){
			i++;
		}
		
		if(i != 2){
			return false;
		}else{
			return true;
		}
	}
	
	//搜索好友
	public HashMap<Integer, String> searchFriend(String searchKey){
		HashMap<Integer, String> tmp = new HashMap<Integer, String>();
		String searchID = "";
		
		try{
			int id = Integer.parseInt(searchKey) - 10000;
			searchID =  " or id = " + id;
		}catch(Exception e){
			
		}
		
		try{
			String exist = "select friendid from xiaotian_friend where userid = " + this.userID;
			ResultList sql = (ResultList)Controller.dataSource.selectData("select id, username from xiaotian_user where (username like '%" + searchKey + "%'" + searchID + ") and usergroup = 'user' and type = 'default' and id != " + this.userID + " and id not in(" + exist + ")");

			if(sql != null){
				while(sql.next()){
					int userID = sql.getInt("id");
					String userName = sql.getString("username");
					tmp.put(userID, userName);
				}
			}

		}catch (Exception exception) {
			return null;
		}
		
		return tmp;
	}
	
	//好友列表
	public HashMap<Integer, String> listUser(){
		HashMap<Integer, String> tmp = new HashMap<Integer, String>();

		try{

			ResultList sql = (ResultList)Controller.dataSource.selectData("select id, username from xiaotian_user where id in(select friendid from xiaotian_friend where userid = " + this.userID + ") and type != 'delete'");
			if(sql != null){
				while(sql.next()){
					int userID = sql.getInt("id");
					String userName = sql.getString("username");
					tmp.put(userID, userName);
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

		try{
			//先获得好友的ID
			ResultList sql = (ResultList)Controller.dataSource.selectData("select friendid from xiaotian_friend where userid = " + this.userID + " order by id asc");
			if(sql != null){
				while(sql.next()){
					//通过好友的ID获取彼此的聊天记录
					int userID = sql.getInt("friendid");

					ResultList sql2 = (ResultList)Controller.dataSource.selectData("select userid, friendid, content, to_char(time, 'yyyy/mm/dd hh24:mi:ss') as time from xiaotian_record where (userid = " + this.userID + " and friendid = " + userID + ") or (friendid = " + this.userID + " and userid = " + userID + ") order by time asc");
					if(sql2 != null){
						Vector<RecordArray> recordList = new Vector<RecordArray>();
						//将每个好友之间的聊天记录临时存放起来
						while(sql2.next()){
							recordList.add(new RecordArray(sql2.getInt("userid"), sql2.getInt("friendid"), sql2.getString("content"), sql2.getString("time")));
						}
						//将每个好友的聊天记录推进一个TreeMap<结构
						tmp.put(userID, recordList);
					}
				}
			}

		}catch (Exception exception) {
			return null;
		}
		
		//返回
		return tmp;
	}
	
	//抓取好友的一条聊天记录
	public RecordArray catchRecord(int userID){
		RecordArray tmp = null;

		try{
			//通过好友的ID获取彼此的聊天记录
			ResultList sql = (ResultList)Controller.dataSource.selectData("select userid, friendid, content, time from xiaotian_record where (userid = " + this.userID + " and friendid = " + userID + ") or (friendid = " + this.userID + " and userid =  " + userID + ") order by time desc");
			if(sql != null){
				//将好友之间的聊天记录临时存放起来
				sql.next();
				tmp = new RecordArray(sql.getInt("userid"), sql.getInt("friendid"), sql.getString("content"), sql.getString("time"));
			}
		}catch (Exception exception) {
			return null;
		}
		
		//返回
		return tmp;
	}
	
	public boolean freezeUser(int userID){
		return false;
	}
	
	public boolean thawUser(int userID){
		return false;
	}

	//改变服务器状态
	public boolean changeSystemType(boolean type){
		return false;
	}
	
	//push用的抓取最新一条聊天记录
	public RecordArray pushRecord(int id){
		RecordArray tmp = null;

		try{
			//通过好友的ID获取彼此的聊天记录
			ResultList sql = (ResultList)Controller.dataSource.selectData("select userid, friendid, content, to_char(time, 'yyyy/mm/dd hh24:mi:ss') as time from xiaotian_record where id = " + id + " order by time desc");
			if(sql != null){
				//将好友之间的聊天记录临时存放起来
				sql.next();
				tmp = new RecordArray(sql.getInt("userid"), sql.getInt("friendid"), sql.getString("content"), sql.getString("time").replace(".0", ""));
			}
		}catch (Exception exception) {
			return null;
		}
		
		//返回
		return tmp;
	}
	
	//push用的抓取最新一条好友请求
	public String pushFriend(int id){
		String tmp = null;
		
		try{
			//通过好友的ID获取彼此的聊天记录
			ResultList sql = (ResultList)Controller.dataSource.selectData("select username from xiaotian_user where id = " + id);
			if(sql != null){
				//将好友之间的聊天记录临时存放起来
				sql.next();
				tmp = sql.getString("username");
			}
		}catch (Exception exception) {
			return null;
		}
		
		return tmp;
	}
	
	//push用的抓取最新一条推送的消息
	public String pushSystemType(int id){
		String tmp = null;
		
		try{
			//通过好友的ID获取彼此的聊天记录
			ResultList sql = (ResultList)Controller.dataSource.selectData("select content from xiaotian_system where id = " + id + " order by id desc");
			if(sql != null){
				//将好友之间的聊天记录临时存放起来
				sql.next();
				tmp = sql.getString("content");
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
			case 3:
				return this.getFresh3(timeLock);
		}
		return 0;
	}
	
	private int getFresh1(int timeLock){
		try{
			//通过好友的ID获取彼此的聊天记录
			ResultList sql = (ResultList)Controller.dataSource.selectData("select id, to_char(time, 'yyyy-MM-dd HH24:mi:ss') as time from xiaotian_record where userid = " + this.userID + " or friendid = " + this.userID + " order by id desc");
			if(sql != null){
				//将好友之间的聊天记录临时存放起来
				sql.next();
				
				int tmp = BaseModule.stringToTime(sql.getString("time"));
				int id = sql.getInt("id");
				if(tmp > timeLock && Client.lastRecordID != id){
					Client.lastRecordID = id;
					return id;
				}
			}
		}catch (Exception exception) {
		}
		return 0;
	}
	
	private int getFresh2(int timeLock){
		try{
			//通过好友的ID获取彼此的新好友
			ResultList sql = (ResultList)Controller.dataSource.selectData("select friendid, to_char(time, 'yyyy-MM-dd HH24:mi:ss') as time from xiaotian_friend where userid = " + this.userID + " order by id desc");
			if(sql != null){
				//将好友之间的聊天记录临时存放起来
				sql.next();

				int tmp = BaseModule.stringToTime(sql.getString("time"));
				if(tmp > timeLock){
					return sql.getInt("friendid");
				}
			}
		}catch (Exception exception) {
		}
		return 0;
	}
	
	private int getFresh3(int timeLock){
		try{
			ResultList sql = (ResultList)Controller.dataSource.selectData("select id, to_char(time, 'yyyy-MM-dd HH24:mi:ss') as time from xiaotian_system where userid = 1 or userid = " + this.userID + " order by id desc");
			if(sql != null){
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
