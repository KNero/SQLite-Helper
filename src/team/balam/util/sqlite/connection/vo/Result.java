package team.balam.util.sqlite.connection.vo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface Result 
{
	public List<HashMap<String, Object>> getSelectResult() throws SQLException;
	public ResultSet getResultSet();
	public int getResultCount();
	
	public boolean isSuccess();
	public boolean isCloseException();
	public String getErrorMessage();
	public Exception getException();
	
	public void close() throws SQLException;
}
