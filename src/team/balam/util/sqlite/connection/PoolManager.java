package team.balam.util.sqlite.connection;

import java.sql.SQLException;

import team.balam.util.sqlite.connection.pool.AlreadyExistsConnectionException;
import team.balam.util.sqlite.connection.pool.ConnectionNotFoundException;
import team.balam.util.sqlite.connection.pool.ConnectionPool;
import team.balam.util.sqlite.connection.pool.QueryConnectionPool;
import team.balam.util.sqlite.connection.vo.QueryVO;
import team.balam.util.sqlite.connection.vo.QueryVOImpl;
import team.balam.util.sqlite.connection.vo.ResultAutoCloser;

public class PoolManager 
{
	private ConnectionPool connectionPool;
	
	private static PoolManager self = new PoolManager();
	
	private PoolManager()
	{
		this.connectionPool = new QueryConnectionPool();
	}
	
	public static PoolManager getInstance()
	{
		return self;
	}
	
	public void setQueryTimeout( int _millisecond )
	{
		QueryVOImpl.setQueryTimeout(_millisecond);
	}
	
	public boolean containsConnection(String _dbName)
	{
		return this.connectionPool.contains(_dbName);
	}
	
	synchronized void addConnection(String _dbName, java.sql.Connection _con) throws AlreadyExistsConnectionException
	{
		this.connectionPool.add(_dbName, _con);
	}
	
	public void executeQuery(String _dbName, QueryVO _vo) throws ConnectionNotFoundException
	{
		this.connectionPool.executeQuery(_dbName, _vo);
	}
	
	public void removeConnection(String _dbName) throws SQLException
	{
		this.connectionPool.remove(_dbName);
	}
	
	public void destroyPool() throws Exception
	{
		this.connectionPool.destory();
		ResultAutoCloser.getInstance().stop();
	}
	
	public int getActiveQuerySize()
	{
		return this.connectionPool.getActiveQuerySize();
	}

	public int getWaitQuerySize()
	{
		return this.connectionPool.getWaitQuerySize();
	}
}
