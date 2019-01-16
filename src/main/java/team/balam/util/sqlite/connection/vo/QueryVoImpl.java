package team.balam.util.sqlite.connection.vo;

import team.balam.util.sqlite.connection.PoolManager;

import java.util.List;

public class QueryVoImpl implements QueryVo {
	private Type mode;
	private String query;
	private Object[] param;
	private int queryTimeout;

	private volatile Result result;

	QueryVoImpl(Type _mode) {
		mode = _mode;
	}

	public void setQueryTimeout(int queryTimeout) {
		this.queryTimeout = queryTimeout;
	}

	@Override
	public int getQueryTimeout() {
		return this.queryTimeout;
	}

	@Override
	public Type getMode() {
		return this.mode;
	}
	
	@Override
	public String getQuery() 
	{
		return query;
	}

	@Override
	public void setQuery( String _query ) 
	{
		this.query = _query;
	}

	@Override
	public Object[] getParam()
	{
		return this.param;
	}

	@Override
	public void setParam(List<Object[]> _param)
	{
		this.param = _param.toArray();
	}
	
	@Override
	public void setParam(Object... _param)
	{
		this.param = _param;
	}
	
	@Override
	public Result getResult() throws QueryExecuteException {
		if (this.result != null) {
			return this.result;
		} else {
			long start = System.currentTimeMillis();
			int realQueryTimeout = this.queryTimeout > 0 ? this.queryTimeout : Integer.MAX_VALUE;

			do {
				if (this.result != null) {
					if (this.result.getException() != null) {
						ResultAutoCloser.getInstance().add(this);
						throw new QueryExecuteException(this, this.result.getException());
					}

					return this.result;
				}
			} while(System.currentTimeMillis() - start < realQueryTimeout);

			ResultAutoCloser.getInstance().add(this);
			throw new QueryTimeoutException(this);
		}
	}

	public void setResult(Result _result) {
		this.result = _result;
	}

	@Override
	public String toString() {
		StringBuilder msg = new StringBuilder("\n# Query : ");
		msg.append(this.query);
		msg.append("\n# Parameter : ");

		if (this.param != null) {
			for (Object p : this.param) {
				msg.append("[").append(p.toString()).append("]");
			}
		}

		return msg.toString();
	}

	@Override
	public void execute() {
		PoolManager.executeQuery(this);
	}

	@Override
	public void execute(String dbName) {
		PoolManager.executeQuery(dbName, this);
	}
}
