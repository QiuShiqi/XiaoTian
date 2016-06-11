package Module.Base;

import java.sql.*;
import java.util.Vector;
import java.util.HashMap;

public class ResultList {
	private Vector<HashMap<String, String>> list = null;
	private int cursor = -1;
	
	public ResultList(ResultSet resultSet) {
		this.list = new Vector<HashMap<String, String>>();
		try {
			if (resultSet != null) {
				while (resultSet.next()) {
					HashMap<String, String> tmp = new HashMap<String, String>();
					ResultSetMetaData fields = resultSet.getMetaData();

					for (int i = 1; i <= fields.getColumnCount(); i++) {
						String fieldName = fields.getColumnName(i);
						tmp.put(fieldName, resultSet.getString(fieldName));
					}
					
					this.list.add(tmp);
				}
			}
		} catch (SQLException exception) {
			System.out.println("DB ERROR!");
		}

	}
	
	public boolean next(){
		if(this.cursor < this.list.size() - 1){
			this.cursor++;
			return true;
		}else{
			return false;
		}

	}
	
	public int getInt(String field){
		HashMap<String, String> tmp = this.list.get(this.cursor);
		try{
			return Integer.parseInt(tmp.get(field.toUpperCase()));
		}catch(Exception e){

		}

		return 0;
	}
	
	public String getString(String field){
		HashMap<String, String> tmp = this.list.get(this.cursor);
		return tmp.get(field.toUpperCase());
	}
	

}
