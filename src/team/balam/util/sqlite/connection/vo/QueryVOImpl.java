package team.balam.util.sqlite.connection.vo;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueryVOImpl implements QueryVO
{
	private byte m_mode;
	private String m_query;
	private List<Object[]> paramList;
	private Object[] param;
	
	private BlockingQueue<Result> m_resultQueue;
	private Result m_result;
	
	public static int queryTimeout = 10000;
	
	public QueryVOImpl( byte _mode )
	{
		m_mode = _mode;
		m_resultQueue = new LinkedBlockingQueue<Result>();
	}
	
	@Override
	public byte getMode()
	{
		return m_mode;
	}
	
	@Override
	public String getQuery() 
	{
		return m_query;
	}

	@Override
	public void setQuery( String _query ) 
	{
		this.m_query = _query;
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
	public Result getResult() throws InterruptedException 
	{
		if( m_result == null )
		{
			m_result = m_resultQueue.poll( queryTimeout, TimeUnit.MILLISECONDS );
		}
		
		return m_result;
	}

	public void setResult( Result _result ) 
	{
		this.m_resultQueue.add( _result );
	}
}
