package team.balam.util.sqlite.connection.pool;

import java.util.HashMap;
import java.util.Map;

import team.balam.util.sqlite.connection.executor.QueryExecutor;

public class QueryConnectionPool implements ConnectionPool
{
	private Map<String, Connection> m_pool;
	
	public QueryConnectionPool()
	{
		m_pool = new HashMap<String, Connection>();
	}
	
	@Override
	public void createConnection(String _name, String _url, boolean _isWAL) throws Exception
	{
		if( m_pool.containsKey( _name ) )
		{
			throw new Exception( "[ " + _name + " ]" + " The connection already exists." );
		}
		
		try
		{
			QueryExecutor executor = new QueryExecutor( _url, _isWAL );
			
			Connection con = new QueryConnection( executor );
			m_pool.put( _name, con );
		}
		catch( Exception e )
		{
			throw new Exception( "Can not create a connection.", e );
		}
	}
	
	@Override
	public Connection get( String _name ) throws Exception
	{
		Connection con = m_pool.get( _name );
		if( con == null )
		{
			throw new Exception( "[ " + _name + " ]" + " The connection does not exist." );
		}
		
		return con;
	}
	
	@Override
	public void destory() throws Exception
	{
		for( String name : m_pool.keySet() )
		{
			((QueryConnection)m_pool.get(name)).close();
		}
	}
}
