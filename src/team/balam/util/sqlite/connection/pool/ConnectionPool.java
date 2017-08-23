package team.balam.util.sqlite.connection.pool;

import java.sql.SQLException;

import team.balam.util.sqlite.connection.vo.QueryVo;

public interface ConnectionPool 
{
	boolean contains(String _name);
	
	void add(String _name, java.sql.Connection _con) throws AlreadyExistsConnectionException;
	
	void executeQuery(String _name, QueryVo _queryVo);

	void destroy() throws SQLException;
	
	int getActiveQuerySize();
	
	int getWaitQuerySize();
}
