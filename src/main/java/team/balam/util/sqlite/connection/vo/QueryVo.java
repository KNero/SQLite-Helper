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
	void setQuery(String _query);
	
	Object[] getParam();
	void setParam(Object[] _param);
	void setParam(List<Object[]> _param);
	
	Result getResult() throws QueryExecuteException;
}
