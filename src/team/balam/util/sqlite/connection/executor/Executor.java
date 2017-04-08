package team.balam.util.sqlite.connection.executor;

import java.sql.SQLException;

import team.balam.util.sqlite.connection.vo.QueryVOImpl;

public interface Executor 
{
	int getSelectSize();
	int getOtherSize();
	int size();
	
	void executeSelect( QueryVOImpl _vo );
	void executeOther( QueryVOImpl _vo );
	
	void stop() throws SQLException;
}
