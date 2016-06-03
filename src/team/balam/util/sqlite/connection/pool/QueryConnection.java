package team.balam.util.sqlite.connection.pool;

import team.balam.util.sqlite.connection.executor.Executor;
import team.balam.util.sqlite.connection.vo.QueryVO;
import team.balam.util.sqlite.connection.vo.QueryVOImpl;

public class QueryConnection implements Connection
{
	private Executor m_executor;
	
	public QueryConnection( Executor _executor )
	{
		m_executor = _executor;
	}
	
	@Override
	public int getSelectSize()
	{
		return m_executor.getSelectSize();
	}
	
	@Override
	public int getOtherSize()
	{
		return m_executor.getOtherSize();
	}
	
	@Override
	public int size()
	{
		return m_executor.size();
	}
	
	@Override
	public void query( QueryVO _vo ) 
	{
		switch( _vo.getMode() )
		{
			case QueryVO.Type.SELECT :
				m_executor.executeSelect((QueryVOImpl)_vo);
				break;
			
			case QueryVO.Type.EXECUTE :
			case QueryVO.Type.DELETE :
			case QueryVO.Type.INSERT :
			case QueryVO.Type.UPDATE :
				m_executor.executeOther((QueryVOImpl)_vo);
				break;
		}
	}
	
	public void close() throws Exception
	{
		m_executor.stop();
	}
}
