package team.balam.util.sqlite.connection.pool;

import team.balam.util.sqlite.connection.vo.QueryVO;

public interface Connection
{
	int getSelectSize();
	int getOtherSize();
	int size();
	
	void query( QueryVO _vo );
}
