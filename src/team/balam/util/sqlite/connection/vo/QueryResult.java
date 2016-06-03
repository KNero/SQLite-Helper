package team.balam.util.sqlite.connection.vo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

public class QueryResult implements Result
{
	private SelectResultInfo selectResult;
	private int m_count;
	
	private boolean isCloseException;
	private boolean m_isSuccess;
	private String m_errorMessage;
	private Exception m_exception;
	
	public void setSelectResult(Statement _statement, PreparedStatement _preparedStatement, ResultSet _resultSet)
	{
		this.selectResult = new SelectResultInfo();
		this.selectResult.setInfo(_statement, _preparedStatement, _resultSet);
	}
	
	@Override
	public List<HashMap<String, String>> getSelectResult() throws SQLException 
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
		m_count = _count;
	}

	@Override
	public int getResultCount() 
	{
		return m_count;
	}
	
	public void onCloseException()
	{
		this.isCloseException = false;
	}
	
	@Override
	public boolean isCloseException()
	{
		return this.isCloseException;
	}

	public void onSuccess()
	{
		m_isSuccess = true;
	}
	
	@Override
	public boolean isSuccess() 
	{
		return m_isSuccess;
	}
	
	public void setErrorMessage( String _msg )
	{
		m_errorMessage = _msg;
	}

	@Override
	public String getErrorMessage() 
	{
		return m_errorMessage;
	}
	
	public void setException( Exception _exception ) 
	{
		m_exception = _exception;
	}

	@Override
	public Exception getException() 
	{
		return m_exception;
	}

	@Override
	public void close() throws SQLException 
	{
		if( this.selectResult != null )
		{
			this.selectResult.close();
		}
	}
}
