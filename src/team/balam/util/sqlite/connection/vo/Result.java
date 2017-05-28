package team.balam.util.sqlite.connection.vo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Result 
{
	public List<Map<String, Object>> getSelectResult() throws SQLException;
	public ResultSet getResultSet();
	public int getResultCount();
	
	public boolean isSuccess();
	public String getErrorMessage();
	public Exception getException();
	
	public void close() throws SQLException;
	public boolean isClosed() throws SQLException;
}
