package team.balam.util.sqlite.connection.pool;

public interface ConnectionPool 
{
	void addConnection(String _name, java.sql.Connection _con) throws Exception;
	
	Connection get( String _name ) throws Exception;
	
	void destory() throws Exception;
}
