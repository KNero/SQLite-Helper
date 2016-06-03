package team.balam.util.sqlite.connection;

import team.balam.util.sqlite.connection.pool.Connection;
import team.balam.util.sqlite.connection.pool.ConnectionPool;
import team.balam.util.sqlite.connection.pool.QueryConnectionPool;
import team.balam.util.sqlite.connection.vo.QueryVOImpl;

public class PoolManager 
{
	private ConnectionPool m_connectionPool;
	
	private static PoolManager self = new PoolManager();
	
	private PoolManager()
	{
		m_connectionPool = new QueryConnectionPool();
	}
	
	public static PoolManager getInstance()
	{
		return self;
	}
	
	public void setQueryTimeout( int _millisecond )
	{
		QueryVOImpl.queryTimeout = _millisecond;
	}
	
	public synchronized void createConnection(String _dbName, String _url, boolean _isWAL) throws Exception
	{
		m_connectionPool.createConnection(_dbName, _url, _isWAL);
	}
	
	public Connection getConnection( String _dbName ) throws Exception
	{
		return m_connectionPool.get( _dbName );
	}
	
	public void destoryPool() throws Exception
	{
		m_connectionPool.destory();
	}
	
	public int getSelectSize( String _dbName )
	{
		try 
		{
			return m_connectionPool.get( _dbName ).getSelectSize();
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
			return m_connectionPool.get( _dbName ).getOtherSize();
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
			return m_connectionPool.get( _dbName ).size();
		} 
		catch( Exception e ) 
		{
			return 0;
		}
	}
}
