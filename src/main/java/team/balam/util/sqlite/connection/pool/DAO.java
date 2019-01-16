package team.balam.util.sqlite.connection.pool;

import team.balam.util.sqlite.connection.vo.QueryResult;
import team.balam.util.sqlite.connection.vo.QueryVo;
import team.balam.util.sqlite.connection.vo.QueryVoImpl;

import java.sql.*;

class DAO {
	static void select(Connection con, QueryVoImpl vo) {
		PreparedStatement ps;
		ResultSet result;
		QueryResult queryResult = new QueryResult();

		try {
			ps = prepareStatement(con, vo);
			result = ps.executeQuery();

			queryResult.setSelectResult(ps, result);
			queryResult.onSuccess();
		} catch (Exception e) {
			queryResult.setException(e);
		} finally {
			vo.setResult(queryResult);
		}
	}

	static void updateOrDelete(Connection con, QueryVoImpl vo) {
		PreparedStatement ps = null;
		QueryResult queryResult = new QueryResult();

		try {
			ps = prepareStatement(con, vo);

			queryResult.setResultCount(ps.executeUpdate());
			queryResult.onSuccess();
		} catch (SQLException e) {
			queryResult.setException(e);
		} finally {
			vo.setResult(queryResult);
			close(queryResult, ps);
		}
	}

	static void insertOrExecute(Connection con, QueryVoImpl vo) {
		PreparedStatement ps = null;
		QueryResult queryResult = new QueryResult();

		try {
			ps = prepareStatement(con, vo);
			ps.execute();

			queryResult.onSuccess();
		} catch (SQLException e) {
			queryResult.setException(e);
		} finally {
			vo.setResult(queryResult);
			close(queryResult, ps);
		}
	}

	private static PreparedStatement prepareStatement(Connection con, QueryVo vo) throws SQLException {
		PreparedStatement ps = con.prepareStatement(vo.getQuery());
		if (vo.getQueryTimeout() > 0) {
			ps.setQueryTimeout(vo.getQueryTimeout());
		}

		Object[] param = vo.getParam();
		if (param != null) {
			for (int p = 1; p <= param.length; ++p) {
				ps.setObject(p, param[p - 1]);
			}
		}

		return ps;
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
