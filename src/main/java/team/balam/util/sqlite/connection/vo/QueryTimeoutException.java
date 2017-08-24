package team.balam.util.sqlite.connection.vo;

public class QueryTimeoutException extends QueryExecuteException {
	public QueryTimeoutException(QueryVo _vo) {
		super(_vo);
	}
}
