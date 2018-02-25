package team.balam.util.sqlite.connection.vo;

public class QueryVoFactory {
	private static int defaultQueryTimeout;

	public static void setDefaultQueryTimeout(int defaultQueryTimeout) {
		QueryVoFactory.defaultQueryTimeout = defaultQueryTimeout;
	}

	public static QueryVo create(QueryVo.Type _type) {
		return create(_type, defaultQueryTimeout);
	}

	public static QueryVo create(QueryVo.Type _type, int _queryTimeout) {
		QueryVo vo = new QueryVoImpl(_type);
		vo.setQueryTimeout(_queryTimeout > 0 ? _queryTimeout : defaultQueryTimeout);
		return vo;
	}
}
