package team.balam.util.sqlite.connection.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import team.balam.util.sqlite.connection.vo.QueryResult;
import team.balam.util.sqlite.connection.vo.QueryVOImpl;

public class DAO 
{
	public static void select( Connection _con, QueryVOImpl _vo )
	{
		Statement statement = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		QueryResult queryResult = new QueryResult();
		
		try
		{
			Object[] param = _vo.getParam();
			
			if(param != null)
			{
				ps = _con.prepareStatement( _vo.getQuery() );
				
				for( int p = 1; p <= param.length; ++p )
				{
					ps.setObject( p, param[ p - 1 ] );
				}
				
				result =  ps.executeQuery();
			}
			else
			{
				statement = _con.createStatement();
				
				result = statement.executeQuery( _vo.getQuery() );
			}
			
			queryResult.setSelectResult(statement, ps, result);
			queryResult.onSuccess();
		}
		catch( Exception e )
		{
			queryResult.setErrorMessage( e.getMessage() );
			queryResult.setException( e );
		}
		finally
		{
			_vo.setResult( queryResult );
		}
	}
	
	public static void updateOrDelete( Connection _con, QueryVOImpl _vo )
	{
		Statement st = null;
		PreparedStatement ps = null;
		int count = 0;
		QueryResult queryResult = new QueryResult();
		
		try
		{
			if(_vo.getParam() != null)
			{
				Object[] param = _vo.getParam();
				
				ps = _con.prepareStatement( _vo.getQuery() );
				
				for( int p = 1; p <= param.length; ++p )
				{
					ps.setObject( p, param[ p - 1 ] );
				}
				
				count += ps.executeUpdate();
			}
			else if(_vo.getParamList() != null && _vo.getParamList().size() > 0)
			{
				ps = _con.prepareStatement( _vo.getQuery() );
				
				List<Object[]> paramList = _vo.getParamList();
				for( Object[] param : paramList )
				{
					for( int p = 1; p <= param.length; ++p )
					{
						ps.setObject( p, param[ p - 1 ] );
					}
					
					count += ps.executeUpdate();
				}
			}
			else
			{
				st = _con.createStatement();
				
				count += st.executeUpdate( _vo.getQuery() );
			}
			
			queryResult.setResultCount( count );
			queryResult.onSuccess();
		}
		catch( Exception e )
		{
			queryResult.setErrorMessage( e.getMessage() );
			queryResult.setException( e );
		}
		finally
		{
			_vo.setResult( queryResult );
			
			close(queryResult, ps);
			close(queryResult, st);
		}
	}
	
	public static void insertOrExecute( Connection _con, QueryVOImpl _vo )
	{
		Statement st = null;
		PreparedStatement ps = null;
		QueryResult queryResult = new QueryResult();
		
		try
		{
			if(_vo.getParam() != null)
			{
				Object[] param = _vo.getParam();
				
				ps = _con.prepareStatement( _vo.getQuery() );
				
				for( int p = 1; p <= param.length; ++p )
				{
					ps.setObject( p, param[ p - 1 ] );
				}
				
				ps.execute();
			}
			else if(_vo.getParamList() != null && _vo.getParamList().size() > 0)
			{
				ps = _con.prepareStatement( _vo.getQuery() );
				
				List<Object[]> paramList = _vo.getParamList();
				for( Object[] param : paramList )
				{
					for( int p = 1; p <= param.length; ++p )
					{
						ps.setObject( p, param[ p - 1 ] );
					}
					
					ps.execute();
				}
			}
			else
			{
				st = _con.createStatement();
				
				st.execute( _vo.getQuery() );
			}
			
			queryResult.onSuccess();
		}
		catch( Exception e )
		{
			queryResult.setErrorMessage( e.getMessage() );
			queryResult.setException( e );
		}
		finally
		{
			_vo.setResult( queryResult );
			
			close(queryResult, ps );
			close(queryResult, st );
		}
	}
	
	public static void close(QueryResult _result, Statement _stat )
	{
		try
		{
			if( _stat != null )
			{
				_stat.close();
			}
		}
		catch( Exception e )
		{
			_result.onCloseException();
			_result.setErrorMessage( e.getMessage() );
			_result.setException( e );
		}
	}
}
