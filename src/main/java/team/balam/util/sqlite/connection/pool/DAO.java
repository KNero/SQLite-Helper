package team.balam.util.sqlite.connection.pool;

import team.balam.util.sqlite.connection.vo.QueryResult;
import team.balam.util.sqlite.connection.vo.QueryVoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

class DAO {
	static void select(Connection _con, QueryVoImpl _vo) {
		PreparedStatement ps;
		ResultSet result;
		QueryResult queryResult = new QueryResult();

		try {
			ps = _con.prepareStatement(_vo.getQuery());
			if (_vo.getQueryTimeout() > 0) {
				ps.setQueryTimeout(_vo.getQueryTimeout());
			}

			Object[] param = _vo.getParam();
			if (param != null) {
				for (int p = 1; p <= param.length; ++p) {
					ps.setObject(p, param[p - 1]);
				}
			}

			result = ps.executeQuery();

			queryResult.setSelectResult(ps, result);
			queryResult.onSuccess();
		} catch (Exception e) {
			queryResult.setException(e);
		} finally {
			_vo.setResult(queryResult);
		}
	}

	static void updateOrDelete(Connection _con, QueryVoImpl _vo) {
		PreparedStatement ps = null;
		QueryResult queryResult = new QueryResult();

		try {
			ps = _con.prepareStatement(_vo.getQuery());
			if (_vo.getQueryTimeout() > 0) {
				ps.setQueryTimeout(_vo.getQueryTimeout());
			}

			Object[] param = _vo.getParam();
			if (param != null) {
				for (int p = 1; p <= param.length; ++p) {
					ps.setObject(p, param[p - 1]);
				}
			}

			queryResult.setResultCount(ps.executeUpdate());
			queryResult.onSuccess();
		} catch (Exception e) {
			queryResult.setException(e);
		} finally {
			_vo.setResult(queryResult);
			close(queryResult, ps);
		}
	}

	static void insertOrExecute(Connection _con, QueryVoImpl _vo) {
		PreparedStatement ps = null;
		QueryResult queryResult = new QueryResult();

		try {
			ps = _con.prepareStatement(_vo.getQuery());
			if (_vo.getQueryTimeout() > 0) {
				ps.setQueryTimeout(_vo.getQueryTimeout());
			}

			Object[] param = _vo.getParam();
			if (param != null) {
				for (int p = 1; p <= param.length; ++p) {
					ps.setObject(p, param[p - 1]);
				}
			}

			ps.execute();

			queryResult.onSuccess();
		} catch (Exception e) {
			queryResult.setException(e);
		} finally {
			_vo.setResult(queryResult);
			close(queryResult, ps);
		}
	}

	private static void close(QueryResult _result, Statement _stat) {
		try {
			if (_stat != null) {
				_stat.close();
			}
		} catch (Exception e) {
			_result.setException(e);
		}
	}
}
