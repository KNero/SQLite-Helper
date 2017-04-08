package team.balam.util.sqlite.connection.vo;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueryVOImpl implements QueryVO
{
	private byte mode;
	private String query;
	private List<Object[]> paramList;
	private Object[] param;
	
	private BlockingQueue<Result> resultQueue;
	private Result result;
	
	public static int queryTimeout = 10000;
	
	public QueryVOImpl( byte _mode )
	{
		mode = _mode;
		resultQueue = new LinkedBlockingQueue<Result>();
	}
	
	@Override
	public byte getMode()
	{
		return mode;
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
	public List<Object[]> getParamList() 
	{
		return paramList;
	}
	
	@Override
	public Object[] getParam()
	{
		return this.param;
	}

	@Override
	public void setParam(List<Object[]> _param)
	{
		this.paramList = _param;
	}
	
	@Override
	public void setParam(Object[] _param)
	{
		this.param = _param;
	}
	
	@Override
	public Result getResult() 
	{
		if(result == null)
		{
			try
			{
				result = resultQueue.poll(queryTimeout, TimeUnit.MILLISECONDS);
			}
			catch(InterruptedException e)
			{
			}
		}
		
		return result;
	}

	public void setResult( Result _result ) 
	{
		this.resultQueue.add( _result );
	}
}
