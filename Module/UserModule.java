package Module;

import java.util.*;

import Module.Base.*;

public abstract class UserModule {
	//用户ID
	protected int userID;
	public int getUserID(){
		return this.userID;
	}
	
	//用户名
	protected String userName;
	public String getUserName(){
		return this.userName;
	}
	
	public UserModule(){
		
	}
	
	public UserModule(int userID, String userName){
		this.userID = userID;
		this.userName = userName;
	}
	
	public abstract boolean loginUser(String userName, String password);
	public abstract boolean registerUser(String userName, String password);
	
	public abstract void dataListener();
	public abstract boolean sendMessage(int userID, String content);
	
	public abstract boolean removeUser(int userID);
	public abstract boolean addUser(int userID);
	public abstract Object listUser();
	
	public abstract HashMap<Integer, String> searchFriend(String searchKey);
	public abstract HashMap<Integer, Vector<RecordArray>> catchAllRecord();
	
	public abstract int getFresh(int timeLock, int type);
	public abstract RecordArray pushRecord(int id);
	public abstract Object pushFriend(int id);
	public abstract String pushSystemType(int id);
	
	public abstract boolean freezeUser(int userID);
	public abstract boolean thawUser(int userID);
	
	//获取服务器状态
	public boolean getSystemType(){
		try{
			ResultList sql = (ResultList)Controller.dataSource.selectData("select content from xiaotian_system where type = 'system_type'");
			if(sql != null){
				sql.next();
				String tmp = sql.getString("content");
				if(tmp.equals("off")){
					return false;
				}else{
					return true;
				}

			}
		}catch (Exception exception) {

		}
		return false;
	}
	public abstract boolean changeSystemType(boolean type);
	
}
