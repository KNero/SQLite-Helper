package team.balam.util.sqlite.connection.executor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.concurrent.ConcurrentLinkedQueue;

import team.balam.util.sqlite.connection.vo.QueryVO;
import team.balam.util.sqlite.connection.vo.QueryVOImpl;

public class QueryExecutor implements Executor
{
	private Connection m_dbConnection;
	private ExecutorWorker m_worker;
	
	private ConcurrentLinkedQueue<QueryVOImpl> m_otherList;
	private ConcurrentLinkedQueue<QueryVOImpl> m_selectList;
	
	public QueryExecutor( String _url, boolean _isWAL ) throws Exception
	{
		m_dbConnection = DriverManager.getConnection( "jdbc:sqlite:" + _url );
		
		if(_isWAL)
		{
			Statement statement = null;
			
			try
			{
				statement = m_dbConnection.createStatement();
				statement.execute( "PRAGMA journal_mode=WAL;" );
			}
			catch( Exception e )
			{
				throw e;
			}
		    finally
		    {
		    	if( statement != null )
		    	{
		    		statement.close();
		    	}
		    }
		}
		
		m_selectList = new ConcurrentLinkedQueue<QueryVOImpl>();
		m_otherList = new ConcurrentLinkedQueue<QueryVOImpl>();
		
		m_worker = new ExecutorWorker();
		m_worker.start();
	}
	
	@Override
	public int getSelectSize()
	{
		return m_selectList.size();
	}
	
	@Override
	public int getOtherSize()
	{
		return m_otherList.size();
	}
	
	@Override
	public int size()
	{
		return m_selectList.size() + m_otherList.size();
	}
	
	@Override
	public void executeSelect( QueryVOImpl _vo ) 
	{
		m_selectList.add(_vo);
	}

	@Override
	public void executeOther( QueryVOImpl _vo ) 
	{
		m_otherList.add( _vo );
	}
	
	@Override
	public void stop() throws Exception
	{
		m_worker.stopWorker();
		m_dbConnection.close();
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
				
				QueryVOImpl selectVO = m_selectList.poll();
				if(selectVO != null )
				{
					isHasQuery = true;
					
					DAO.select(m_dbConnection, selectVO);
				}
				
				QueryVOImpl otherVO = m_otherList.poll();
				if( otherVO != null )
				{
					isHasQuery = true;
					
					if( otherVO.getMode() == QueryVO.Type.INSERT || otherVO.getMode() == QueryVO.Type.EXECUTE )
					{
						DAO.insertOrExecute( m_dbConnection, otherVO );
					}
					else
					{
						DAO.updateOrDelete( m_dbConnection, otherVO );
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
