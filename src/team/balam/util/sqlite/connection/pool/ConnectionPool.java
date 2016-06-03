package team.balam.util.sqlite.connection.pool;

public interface ConnectionPool 
{
	void createConnection(String _name, String _url, boolean _isWAL) throws Exception;
	
	Connection get( String _name ) throws Exception;
	
	void destory() throws Exception;
}
