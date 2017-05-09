package team.balam.util.sqlite.connection.vo;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueryVOImpl implements QueryVO
{
	private QueryVO.Type mode;
	private String query;
	private Object[] param;
	private int queryTimeout = 10000;
	
	private BlockingQueue<Result> resultQueue;
	private Result result;
	
	QueryVOImpl(QueryVO.Type _mode) {
		mode = _mode;
		resultQueue = new LinkedBlockingQueue<Result>();
	}
	
	public void setQueryTimeout(int queryTimeout) {
		this.queryTimeout = queryTimeout;
	}

	@Override
	public QueryVO.Type getMode() {
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
	public Result getResult() throws QueryTimeoutException {
		if (this.result == null) {
			try {
				this.result = resultQueue.poll(queryTimeout, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
			}
		}

		if (this.result == null) {
			ResultAutoCloser.getInstance().add(this);
			throw new QueryTimeoutException(this.query, this.param);
		}

		return this.result;
	}

	public void setResult( Result _result ) 
	{
		this.resultQueue.add( _result );
	}
}
