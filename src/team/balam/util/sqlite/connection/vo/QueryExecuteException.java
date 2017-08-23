package team.balam.util.sqlite.connection.vo;

/**
 * Created by smkwon on 2017-08-23.
 */
public class QueryExecuteException extends Exception {
	public QueryExecuteException(QueryVo _vo, Exception _e) {
		super(_vo.toString(), _e);
	}

	protected QueryExecuteException(QueryVo _vo) {
		super(_vo.toString());
	}
}
