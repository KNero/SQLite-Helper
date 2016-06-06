package team.balam.util.sqlite.connection.executor;

import java.sql.Connection;
import java.util.concurrent.ConcurrentLinkedQueue;

import team.balam.util.sqlite.connection.vo.QueryVO;
import team.balam.util.sqlite.connection.vo.QueryVOImpl;

public class QueryExecutor implements Executor
{
	private Connection dbConnection;
	private ExecutorWorker worker;
	
	private ConcurrentLinkedQueue<QueryVOImpl> otherList;
	private ConcurrentLinkedQueue<QueryVOImpl> selectList;
	
	public QueryExecutor(Connection _con) throws Exception
	{
		this.dbConnection = _con;
		
		selectList = new ConcurrentLinkedQueue<QueryVOImpl>();
		otherList = new ConcurrentLinkedQueue<QueryVOImpl>();
		
		worker = new ExecutorWorker();
		worker.start();
	}
	
	@Override
	public int getSelectSize()
	{
		return selectList.size();
	}
	
	@Override
	public int getOtherSize()
	{
		return otherList.size();
	}
	
	@Override
	public int size()
	{
		return selectList.size() + otherList.size();
	}
	
	@Override
	public void executeSelect( QueryVOImpl _vo ) 
	{
		selectList.add(_vo);
	}

	@Override
	public void executeOther( QueryVOImpl _vo ) 
	{
		otherList.add( _vo );
	}
	
	@Override
	public void stop() throws Exception
	{
		worker.stopWorker();
		dbConnection.close();
	}
	
	private class ExecutorWorker extends Thread
	{
		private boolean isRunning;
		
		public ExecutorWorker()
		{
			this.setName("QueryExecutorWorker");
			isRunning = true;
		}
		
		public void stopWorker()
		{
			isRunning = false;
		}
		
		@Override
		public void run()
		{
			while( isRunning )
			{
				boolean isHasQuery = false;
				
				QueryVOImpl selectVO = selectList.poll();
				if(selectVO != null )
				{
					isHasQuery = true;
					
					DAO.select(dbConnection, selectVO);
				}
				
				QueryVOImpl otherVO = otherList.poll();
				if( otherVO != null )
				{
					isHasQuery = true;
					
					if( otherVO.getMode() == QueryVO.Type.INSERT || otherVO.getMode() == QueryVO.Type.EXECUTE )
					{
						DAO.insertOrExecute( dbConnection, otherVO );
					}
					else
					{
						DAO.updateOrDelete( dbConnection, otherVO );
					}
				}
				
				if(! isHasQuery)
				{
					try 
					{
						Thread.sleep( 1 );
					} 
					catch( InterruptedException e ) 
					{
						e.printStackTrace();
					}
				}
			}
		}
	}
}
