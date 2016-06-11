package Module.Base;

import javax.swing.table.DefaultTableModel;

public class UserList extends DefaultTableModel {
	public UserList(Object[] columnNames) {
		super(null, columnNames);
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
