package team.balam.util.sqlite.connection.vo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class QueryResult implements Result
{
	private SelectResultInfo selectResult;
	private int count;
	
	private boolean isSuccess;
	private String errorMessage;
	private Exception exception;
	
	public void setSelectResult(Statement _statement, PreparedStatement _preparedStatement, ResultSet _resultSet)
	{
		this.selectResult = new SelectResultInfo();
		this.selectResult.setInfo(_statement, _preparedStatement, _resultSet);
	}
	
	@Override
	public List<Map<String, Object>> getSelectResult() throws SQLException 
	{
		return this.selectResult.getResultList();
	}
	
	@Override
	public ResultSet getResultSet()
	{
		return this.selectResult.getResultSet();
	}
	
	public void setResultCount( int _count )
	{
		count = _count;
	}

	@Override
	public int getResultCount() 
	{
		return count;
	}
	
	public void onSuccess()
	{
		isSuccess = true;
	}
	
	@Override
	public boolean isSuccess() 
	{
		return isSuccess;
	}
	
	public void setErrorMessage( String _msg )
	{
		errorMessage = _msg;
	}

	@Override
	public String getErrorMessage() 
	{
		return errorMessage;
	}
	
	public void setException( Exception _exception ) 
	{
		exception = _exception;
	}

	@Override
	public Exception getException() 
	{
		return exception;
	}

	@Override
	public void close() throws SQLException 
	{
		if( this.selectResult != null )
		{
			this.selectResult.close();
		}
	}
	
	@Override
	public boolean isClosed() throws SQLException {
		if (this.selectResult != null) {
			return this.selectResult.isClosed();
		}
		
		return true;
	}
	
}
