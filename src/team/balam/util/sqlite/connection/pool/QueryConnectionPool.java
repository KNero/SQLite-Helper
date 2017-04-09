package team.balam.util.sqlite.connection.pool;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import team.balam.util.sqlite.connection.vo.QueryVO;

public class QueryConnectionPool implements ConnectionPool
{
	private ThreadPoolExecutor workerPool;
	private Map<String, java.sql.Connection> pool;
	
	public QueryConnectionPool()
	{
		int maximumPoolSize = 50;
		BlockingQueue<Runnable> waitQueue = new ArrayBlockingQueue<Runnable>(maximumPoolSize);
		this.workerPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() + 1, maximumPoolSize, 30, TimeUnit.SECONDS, waitQueue,
				new RejectedExecutionHandler(){
					@Override
					public void rejectedExecution(Runnable _r, ThreadPoolExecutor _executor)
					{
						if(! _executor.isShutdown())
						{
							_executor.execute(_r);
						}
					}
				});
		
		this.pool = new ConcurrentHashMap<String, java.sql.Connection>();
	}
	
	@Override
	public boolean contains(String _name)
	{
		return this.pool.containsKey(_name);
	}
	
	@Override
	public void add(String _name, java.sql.Connection _con) throws AlreadyExistsConnectionException
	{
		if(this.pool.containsKey(_name))
		{
			throw new AlreadyExistsConnectionException(_name);
		}
		
		this.pool.putIfAbsent(_name, _con);
	}
	
	@Override
	public void executeQuery(String _name, QueryVO _vo) throws ConnectionNotFoundException
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
	public void remove(String _name) throws SQLException
	{
		java.sql.Connection con = this.pool.remove(_name);
		if(con != null)
		{
			con.close();
		}
	}
	
	@Override
	public void destory()
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
