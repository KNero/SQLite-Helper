package team.balam.util.sqlite.connection.pool;

import team.balam.util.sqlite.connection.vo.QueryVo;
import team.balam.util.sqlite.connection.vo.QueryVoImpl;

import java.sql.Connection;

public class QueryExecutor implements Runnable
{
	private final Connection dbCon;
	private QueryVoImpl queryVo;
	
	QueryExecutor(Connection _con, QueryVo _vo)
	{
		this.dbCon = _con;
		this.queryVo = (QueryVoImpl)_vo;
	}
	
	@Override
	public void run() {
		synchronized (this.dbCon) {
			switch(this.queryVo.getMode()) {
				case SELECT :
					DAO.select(this.dbCon, this.queryVo);
					break;

				case INSERT :
				case EXECUTE :
					DAO.insertOrExecute(this.dbCon, this.queryVo);
					break;

				case UPDATE :
				case DELETE :
					DAO.updateOrDelete(this.dbCon, this.queryVo);
					break;
			}
		}
	}
}
