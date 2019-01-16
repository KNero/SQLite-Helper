package team.balam.util.sqlite.connection.pool;

import team.balam.util.sqlite.connection.vo.QueryVo;

import java.sql.SQLException;

public interface ConnectionPool {
	void add(String name, java.sql.Connection con) throws AlreadyExistsConnectionException;

	void returnConnection(String name, java.sql.Connection con);

	void executeQuery(String name, QueryVo queryVo);

	void destroy() throws SQLException;

	boolean isEmpty();
	
	int getActiveQuerySize();
	
	int getWaitQuerySize();
}
