package team.balam.util.sqlite.connection.pool;

import team.balam.util.sqlite.connection.vo.QueryVO;
import team.balam.util.sqlite.connection.vo.QueryVOImpl;

public class QueryExecutor implements Runnable
{
	private java.sql.Connection dbCon;
	private QueryVOImpl queryVo;
	
	QueryExecutor(java.sql.Connection _con, QueryVO _vo)
	{
		this.dbCon = _con;
		this.queryVo = (QueryVOImpl)_vo;
	}
	
	@Override
	public void run()
	{
		switch(this.queryVo.getMode())
		{
			case QueryVO.Type.SELECT :
				DAO.select(this.dbCon, this.queryVo);
				break;
				
			case QueryVO.Type.INSERT :
			case QueryVO.Type.EXECUTE :
				DAO.insertOrExecute(this.dbCon, this.queryVo);
				break;
				
			case QueryVO.Type.UPDATE :
			case QueryVO.Type.DELETE :
				DAO.updateOrDelete(this.dbCon, this.queryVo);
				break;
		}
	}
}
