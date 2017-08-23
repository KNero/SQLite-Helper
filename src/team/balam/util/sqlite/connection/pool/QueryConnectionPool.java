package team.balam.util.sqlite.connection.pool;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.*;

import team.balam.util.sqlite.connection.vo.QueryVo;

public class QueryConnectionPool implements ConnectionPool
{
	private ThreadPoolExecutor workerPool;
	private Map<String, java.sql.Connection> pool;
	
	public QueryConnectionPool() {
		int maximumPoolSize = Runtime.getRuntime().availableProcessors() + 1;
		BlockingQueue<Runnable> waitQueue = new LinkedBlockingQueue<Runnable>();
		this.workerPool = new ThreadPoolExecutor(maximumPoolSize, maximumPoolSize * 2, 30, TimeUnit.SECONDS, waitQueue);
		this.pool = new ConcurrentHashMap<String, java.sql.Connection>();
	}
	
	@Override
	public boolean contains(String _name)
	{
		return this.pool.containsKey(_name);
	}
	
	@Override
	public synchronized void add(String _name, java.sql.Connection _con) throws AlreadyExistsConnectionException
	{
		if(this.pool.containsKey(_name))
		{
			throw new AlreadyExistsConnectionException(_name);
		}

		this.pool.put(_name, _con);
	}
	
	@Override
	public void executeQuery(String _name, QueryVo _vo)
	{
		java.sql.Connection con = this.pool.get(_name);
		if(con == null)
		{
			throw new ConnectionNotFoundException( "[ " + _name + " ]" + " The connection does not exist." );
		}
		
		synchronized(con)
		{
			this.workerPool.execute(new QueryExecutor(con, _vo));
		}
	}
	
	@Override
	public void destroy()
	{
		this.workerPool.shutdown();
		
		try
		{
			this.workerPool.awaitTermination(1, TimeUnit.MINUTES);
		}
		catch(InterruptedException e)
		{
		}
		
		for(java.sql.Connection con : this.pool.values())
		{
			try
			{
				con.close();
			}
			catch(SQLException e)
			{
			}
		}
		
		this.pool = new ConcurrentHashMap<String, java.sql.Connection>();
	}

	@Override
	public int getActiveQuerySize()
	{
		return this.workerPool.getActiveCount();
	}

	@Override
	public int getWaitQuerySize()
	{
		return this.workerPool.getQueue().size();
	}
}
