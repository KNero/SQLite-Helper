package team.balam.util.sqlite.connection.pool;

import java.sql.SQLException;

public interface ConnectionPool 
{
	void addConnection(String _name, java.sql.Connection _con) throws Exception;
	
	Connection get( String _name ) throws ConnectionNotFoundException;
	
	void remove(String _name) throws SQLException;
	
	void destory() throws SQLException;
}
