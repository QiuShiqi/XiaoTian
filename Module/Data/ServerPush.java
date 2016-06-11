package Module.Data;

import java.text.SimpleDateFormat;

import Module.BaseModule;
import Module.Form.*;
import Module.User.Client;
import Module.*;

public class ServerPush extends Thread{
	private int timeLock;
	private UserModule userModule;
	private BaseUI form;
	private int type;	//1为聊天消息推送，2为好友请求推送。
	
	public ServerPush(UserModule client, BaseUI form, int type){
		this.timeLock = BaseModule.getTime() + Client.timeDifference;
		this.userModule = client;
		this.form = form;
		this.type = type;
	}
	
	public void run(){		
		while(true){
			int tmp = this.userModule.getFresh(this.timeLock, type);
			
			if(tmp > 0){
				switch(this.type){
				case 1:
					this.addRecord(tmp);
					break;
				case 2:
					this.addFriend(tmp);
					break;
				case 3:
					this.systemType(tmp);
					break;
				}
				
				this.interrupt();
			}
			
			//缓解CPU压力
			try{
				Thread.sleep(200);
				
			}catch(Exception e){
				return;
			}
		}
	}
	
	private void addRecord(int id){
		this.form.addRecord(id);
	}
	
	private void addFriend(int id){
		this.form.addFriend(id);
	}
	
	private void systemType(int id){
		this.form.systemType(id);
	}
}
