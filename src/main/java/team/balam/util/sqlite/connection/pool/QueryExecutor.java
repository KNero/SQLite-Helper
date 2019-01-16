package team.balam.util.sqlite.connection.pool;

import team.balam.util.sqlite.connection.vo.QueryVo;
import team.balam.util.sqlite.connection.vo.QueryVoImpl;

import java.sql.Connection;

class QueryExecutor implements Runnable {
	private final ConnectionPool pool;
	private final String dbName;
	private final Connection dbCon;
	private final QueryVoImpl queryVo;
	
	QueryExecutor(ConnectionPool pool, String dbName, Connection con, QueryVo vo) {
		this.pool = pool;
		this.dbName = dbName;
		this.dbCon = con;
		this.queryVo = (QueryVoImpl)vo;
	}
	
	@Override
	public void run() {
		try {
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
		} finally {
			this.pool.returnConnection(dbName, dbCon);
		}
	}
}
