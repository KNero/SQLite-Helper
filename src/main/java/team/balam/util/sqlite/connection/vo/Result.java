package team.balam.util.sqlite.connection.vo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Result
{
	List<Map<String, Object>> getSelectResult() throws SQLException;
	ResultSet getResultSet();
	int getResultCount();
	
	boolean isSuccess();
	Exception getException();
	
	void close() throws SQLException;
	boolean isClosed() throws SQLException;
}
