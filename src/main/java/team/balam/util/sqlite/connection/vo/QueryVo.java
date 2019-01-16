package team.balam.util.sqlite.connection.vo;

import java.util.List;

public interface QueryVo 
{
	enum Type {
		SELECT, DELETE, UPDATE, INSERT, EXECUTE
	}
	
	Type getMode();
	void setQueryTimeout(int queryTimeout);
	int getQueryTimeout();
	
	String getQuery(); 
	void setQuery(String query);
	
	Object[] getParam();
	void setParam(Object... param);
	void setParam(List<Object[]> param);
	
	Result getResult() throws QueryExecuteException;

	void execute();
	void execute(String dbName);
}
