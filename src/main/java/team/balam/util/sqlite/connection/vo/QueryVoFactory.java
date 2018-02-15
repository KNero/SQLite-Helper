package team.balam.util.sqlite.connection.vo;

public class QueryVoFactory
{
	private static final int DEFAULT_QUERY_TIMEOUT = 10000;
	
	public static QueryVo create(QueryVo.Type _type) {
		return create(_type, DEFAULT_QUERY_TIMEOUT);
	}

	public static QueryVo create(QueryVo.Type _type, int _queryTimeout) {
		QueryVo vo = new QueryVoImpl(_type);
		vo.setQueryTimeout(_queryTimeout > 0 ? _queryTimeout : DEFAULT_QUERY_TIMEOUT);
		return vo;
	}
}