package team.balam.util.sqlite.connection.pool;

import java.sql.SQLException;

import team.balam.util.sqlite.connection.vo.QueryVo;

public interface ConnectionPool 
{
	boolean contains(String _name);
	
	void add(String _name, java.sql.Connection _con) throws AlreadyExistsConnectionException;
	
	void executeQuery(String _name, QueryVo _queryVo) throws ConnectionNotFoundException;
	
	void remove(String _name) throws SQLException;
	
	void destory() throws SQLException;
	
	int getActiveQuerySize();
	
	int getWaitQuerySize();
}
