package team.balam.util.sqlite.connection.vo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SelectResultInfo
{
	private Statement statement;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	
	private List<Map<String, Object>> resultList;
	
	public void setInfo(Statement _statement, PreparedStatement _preparedStatement, ResultSet _resultSet)
	{
		this.statement = _statement;
		this.preparedStatement = _preparedStatement;
		this.resultSet = _resultSet;
	}
	
	public List<Map<String, Object>> getResultList() throws SQLException 
	{
		if(this.resultList == null)
		{
			this.resultList = new ArrayList<Map<String, Object>>();
			
			ResultSetMetaData rsmd = this.resultSet.getMetaData();
			int columnSize = rsmd.getColumnCount();
			
			while( this.resultSet.next() )
			{
				HashMap<String, Object> m = new HashMap<String, Object>();
				
				for( int i = 1; i <= columnSize; ++i )
				{
					String columnName = rsmd.getColumnName( i ).toLowerCase();
					Object columnValue = this.resultSet.getObject( i );
					
					m.put( columnName, columnValue );
				}
				
				this.resultList.add( m );
			}
		}
		
		return this.resultList;
	}
	
	public ResultSet getResultSet()
	{
		return this.resultSet;
	}

	public void close() throws SQLException 
	{
		if(this.statement != null ) this.statement.close();
		if(this.preparedStatement != null ) this.preparedStatement.close();
		if(this.resultSet != null ) this.resultSet.close();
	}
	
	public boolean isClosed() throws SQLException {
		return this.resultSet.isClosed();
	}
}
