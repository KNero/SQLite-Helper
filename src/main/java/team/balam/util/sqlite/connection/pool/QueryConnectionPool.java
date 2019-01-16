package team.balam.util.sqlite.connection.pool;

import team.balam.util.sqlite.connection.vo.QueryVo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class QueryConnectionPool implements ConnectionPool {
	private ThreadPoolExecutor workerPool;
	private Map<String, BlockingQueue<java.sql.Connection>> connectionPool;
	
	public QueryConnectionPool() {
		int maximumPoolSize = Runtime.getRuntime().availableProcessors() + 1;
		BlockingQueue<Runnable> waitQueue = new LinkedBlockingQueue<Runnable>();
		this.workerPool = new ThreadPoolExecutor(maximumPoolSize, maximumPoolSize * 2, 30, TimeUnit.SECONDS, waitQueue);
		this.connectionPool = new HashMap<String, BlockingQueue<java.sql.Connection>>();
	}

	@Override
	public boolean isEmpty() {
		return connectionPool.isEmpty();
	}

	private java.sql.Connection getConnection(String name) throws InterruptedException {
		BlockingQueue<java.sql.Connection> queue = connectionPool.get(name);
		if (queue == null) {
			return null;
		} else {
			return queue.poll(15, TimeUnit.SECONDS);
		}
	}

	@Override
	public void returnConnection(String name, java.sql.Connection con) {
		BlockingQueue<java.sql.Connection> queue = connectionPool.get(name);
		if (queue != null) {
			queue.add(con);
		}
	}

	@Override
	public synchronized void add(String name, java.sql.Connection con) throws AlreadyExistsConnectionException {
		if (this.connectionPool.containsKey(name)) {
			throw new AlreadyExistsConnectionException(name);
		} else {
			ArrayBlockingQueue<java.sql.Connection> conQue = new ArrayBlockingQueue<Connection>(1);
			conQue.add(con);
			connectionPool.put(name, conQue);
		}
	}
	
	@Override
	public void executeQuery(String name, QueryVo vo) {
		java.sql.Connection con = null;

		try {
			con = getConnection(name);
		} catch (InterruptedException e) {
			//ignore
		}

		if(con == null) {
			throw new ConnectionNotFoundException( "[ " + name + " ]" + " The connection does not exist." );
		}

		this.workerPool.execute(new QueryExecutor(this, name, con, vo));
	}
	
	@Override
	public void destroy() throws SQLException {
		this.workerPool.shutdown();
		
		try {
			this.workerPool.awaitTermination(1, TimeUnit.MINUTES);
		} catch(InterruptedException e) {
			//ignore
		}

		for (String name : connectionPool.keySet()) {
			int count = 0;
			java.sql.Connection con = null;

			while (++count <= 3) {
				try {
					con = getConnection(name);
					if (con != null) {
						con.close();
						break;
					}
				} catch (InterruptedException e) {
					//ignore
				}
			}

			if (con == null) {
				throw new SQLException("Fail to close db connection.");
			}
		}
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
