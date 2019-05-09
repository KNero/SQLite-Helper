package team.balam.util.sqlite.connection;

import team.balam.util.sqlite.connection.pool.AlreadyExistsConnectionException;
import team.balam.util.sqlite.connection.pool.ConnectionPool;
import team.balam.util.sqlite.connection.pool.QueryConnectionPool;
import team.balam.util.sqlite.connection.vo.QueryVo;

import java.sql.SQLException;

public class PoolManager
{
	private static ConnectionPool connectionPool= new QueryConnectionPool();
	private static String defaultDb;

	private PoolManager() {

	}

	public static void setDefaultDb(String _defaultDb) {
		defaultDb = _defaultDb;
	}

	static void addConnection(String _dbName, java.sql.Connection _con) throws AlreadyExistsConnectionException {
		connectionPool.add(_dbName, _con);

		if (defaultDb == null || defaultDb.isEmpty()) {
			defaultDb = _dbName;
		}
	}

	public static void executeQuery(QueryVo _vo) {
		executeQuery(defaultDb, _vo);
	}

	public static void executeQuery(String _dbName, QueryVo _vo) {
		connectionPool.executeQuery(_dbName, _vo);
	}
	
	public static void destroyPool() throws SQLException {
		connectionPool.destroy();
	}
	
	public static int getActiveQuerySize()
	{
		return connectionPool.getActiveQuerySize();
	}

	public static int getWaitQuerySize()
	{
		return connectionPool.getWaitQuerySize();
	}
}
