package Module.Base;

public class DatabaseInfo {
	public DatabaseInfo(String host, int port, String userName, String password){
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.password = password;
	}
	
	
	public String host;
	public int port;
	public String userName;
	public String password;
}
