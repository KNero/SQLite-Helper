package team.balam.util.sqlite.connection.pool;

import team.balam.util.sqlite.connection.executor.Executor;
import team.balam.util.sqlite.connection.vo.QueryVO;
import team.balam.util.sqlite.connection.vo.QueryVOImpl;

public class QueryConnection implements Connection
{
	private Executor executor;
	
	public QueryConnection( Executor _executor )
	{
		executor = _executor;
	}
	
	@Override
	public int getSelectSize()
	{
		return executor.getSelectSize();
	}
	
	@Override
	public int getOtherSize()
	{
		return executor.getOtherSize();
	}
	
	@Override
	public int size()
	{
		return executor.size();
	}
	
	@Override
	public void query( QueryVO _vo ) 
	{
		switch( _vo.getMode() )
		{
			case QueryVO.Type.SELECT :
				executor.executeSelect((QueryVOImpl)_vo);
				break;
			
			case QueryVO.Type.EXECUTE :
			case QueryVO.Type.DELETE :
			case QueryVO.Type.INSERT :
			case QueryVO.Type.UPDATE :
				executor.executeOther((QueryVOImpl)_vo);
				break;
		}
	}
	
	public void close() throws Exception
	{
		executor.stop();
	}
}
