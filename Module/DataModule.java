package Module;

public abstract class DataModule {
	public abstract boolean InitiallzData(String database);
	public abstract Object selectData(String sql);
	public abstract boolean addData(String sql);
	public abstract boolean modifyData(String sql);
	public abstract boolean deleteData(String sql);
}
