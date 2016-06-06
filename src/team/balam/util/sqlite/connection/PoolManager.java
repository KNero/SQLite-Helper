package team.balam.util.sqlite.connection;

import team.balam.util.sqlite.connection.pool.Connection;
import team.balam.util.sqlite.connection.pool.ConnectionPool;
import team.balam.util.sqlite.connection.pool.QueryConnectionPool;
import team.balam.util.sqlite.connection.vo.QueryVOImpl;

public class PoolManager 
{
	private ConnectionPool connectionPool;
	
	private static PoolManager self = new PoolManager();
	
	private PoolManager()
	{
		connectionPool = new QueryConnectionPool();
	}
	
	public static PoolManager getInstance()
	{
		return self;
	}
	
	public void setQueryTimeout( int _millisecond )
	{
		QueryVOImpl.queryTimeout = _millisecond;
	}
	
	public synchronized void addConnection(String _dbName, java.sql.Connection _con) throws Exception
	{
		connectionPool.addConnection(_dbName, _con);
	}
	
	public Connection getConnection( String _dbName ) throws Exception
	{
		return connectionPool.get( _dbName );
	}
	
	public void destroyPool() throws Exception
	{
		connectionPool.destory();
	}
	
	public int getSelectSize( String _dbName )
	{
		try 
		{
			return connectionPool.get( _dbName ).getSelectSize();
		} 
		catch( Exception e ) 
		{
			return 0;
		}
	}
	
	public int getOtherSize( String _dbName )
	{
		try 
		{
			return connectionPool.get( _dbName ).getOtherSize();
		} 
		catch( Exception e ) 
		{
			return 0;
		}
	}
	
	public int size( String _dbName )
	{
		try 
		{
			return connectionPool.get( _dbName ).size();
		} 
		catch( Exception e ) 
		{
			return 0;
		}
	}
}
