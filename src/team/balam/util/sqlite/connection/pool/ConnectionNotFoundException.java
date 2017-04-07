package team.balam.util.sqlite.connection.pool;

public class ConnectionNotFoundException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public ConnectionNotFoundException(String _msg)
	{
		super(_msg);
	}

	public ConnectionNotFoundException(Exception _e)
	{
		super(_e);
	}
}
