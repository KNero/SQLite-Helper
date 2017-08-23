package team.balam.util.sqlite.connection.vo;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueryVoImpl implements QueryVo
{
	private QueryVo.Type mode;
	private String query;
	private Object[] param;
	private int queryTimeout;
	
	private BlockingQueue<Result> resultQueue;
	private Result result;
	
	QueryVoImpl(QueryVo.Type _mode) {
		mode = _mode;
		resultQueue = new LinkedBlockingQueue<Result>();
	}
	
	public void setQueryTimeout(int queryTimeout) {
		this.queryTimeout = queryTimeout;
	}

	@Override
	public QueryVo.Type getMode() {
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
	public void setParam(Object[] _param)
	{
		this.param = _param;
	}
	
	@Override
	public Result getResult() throws QueryExecuteException {
		if (this.result == null) {
			try {
				this.result = resultQueue.poll(queryTimeout, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
			}
		}

		if (this.result == null) {
			ResultAutoCloser.getInstance().add(this);
			throw new QueryTimeoutException(this);
		} else if (this.result.getException() != null) {
			throw new QueryExecuteException(this, this.result.getException());
		}

		return this.result;
	}

	public void setResult( Result _result ) 
	{
		this.resultQueue.add( _result );
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
}
