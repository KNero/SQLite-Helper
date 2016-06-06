package team.balam.util.sqlite.connection.pool;

import java.util.HashMap;
import java.util.Map;

import team.balam.util.sqlite.connection.executor.QueryExecutor;

public class QueryConnectionPool implements ConnectionPool
{
	private Map<String, Connection> pool;
	
	public QueryConnectionPool()
	{
		pool = new HashMap<String, Connection>();
	}
	
	@Override
	public void addConnection(String _name, java.sql.Connection _con) throws Exception
	{
		if( pool.containsKey( _name ) )
		{
			throw new Exception( "[ " + _name + " ]" + " The connection already exists." );
		}
		
		try
		{
			QueryExecutor executor = new QueryExecutor(_con);
			
			Connection con = new QueryConnection( executor );
			pool.put( _name, con );
		}
		catch( Exception e )
		{
			throw new Exception( "Can not create a connection.", e );
		}
	}
	
	@Override
	public Connection get( String _name ) throws Exception
	{
		Connection con = pool.get( _name );
		if( con == null )
		{
			throw new Exception( "[ " + _name + " ]" + " The connection does not exist." );
		}
		
		return con;
	}
	
	@Override
	public void destory() throws Exception
	{
		for( String name : pool.keySet() )
		{
			((QueryConnection)pool.get(name)).close();
		}
	}
}
