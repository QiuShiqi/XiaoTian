package Module.Base;

public class DatabaseInfo {
	public DatabaseInfo(String type, String host, int port, String userName, String password) {
		this.type = type;
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.password = password;
	}

	public String type;
	public String host;
	public int port;
	public String userName;
	public String password;
}
