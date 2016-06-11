package Module.Data;

import java.sql.*;

import Module.Base.*;

public class Oracle extends Database {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private Connection connect = null;

	public Oracle(String host, int port, String userName, String password){
		super(host, port, userName, password);
	}
	public boolean InitiallzData(String database) {
		try {
			Class.forName(this.driver);
			this.connect = DriverManager.getConnection("jdbc:oracle:thin:@" + this.host + ":" + this.port + ":" + database, this.userName, this.password);

			return true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}

	public Object selectData(String sql) {

		try {
			Statement statement = this.connect.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			ResultList ret = new ResultList(resultSet);
			statement.close();
			return ret;
			//return resultSet;
		} catch (SQLException e) {
			
		}

		return null;
	}

	public boolean addData(String sql) {
		return this.sqlStatement(sql);
	}
	
	public boolean modifyData(String sql) {
		return this.sqlStatement(sql);
	}

	public boolean deleteData(String sql) {
		return this.sqlStatement(sql);
	}
	
	public boolean sqlStatement(String sql){
		try {
			Statement statement = this.connect.createStatement();
			
			if(statement.executeUpdate(sql) > 0){
				this.connect.commit();	//提交
				return true;
			}else{
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

}
