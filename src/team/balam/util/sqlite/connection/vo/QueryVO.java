package team.balam.util.sqlite.connection.vo;

import java.util.List;

public interface QueryVO 
{
	interface Type
	{
		byte SELECT = 0;
		byte DELETE = 1;
		byte UPDATE = 2;
		byte INSERT = 3;
		byte EXECUTE = 4;
	}
	
	byte getMode();
	
	String getQuery(); 
	void setQuery(String _query);
	
	List<Object[]> getParamList();
	void setParam(List<Object[]> _param);
	
	Object[] getParam();
	void setParam(Object[] _param);
	
	Result getResult() throws InterruptedException;
}
