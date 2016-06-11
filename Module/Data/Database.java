package Module.Data;

import Module.DataModule;

public class Database extends DataModule {
	protected String host;
	protected int port;
	protected String userName;
	protected String password;
	
	public Database(String host, int port, String userName, String password){
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.password = password;
	}
	
	public boolean InitiallzData(String database){
		return false;
	};
	
	public Object selectData(String sql){
		return new Object();
	}
	
	public boolean addData(String sql){
		return false;
	};
	
	public boolean modifyData(String sql){
		return false;
	};
	
	public boolean deleteData(String sql){
		return false;
	};
}
