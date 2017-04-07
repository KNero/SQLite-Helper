package team.balam.util.sqlite.connection;

public class DatabaseLoadException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public DatabaseLoadException(String _msg)
	{
		super(_msg);
	}

	public DatabaseLoadException(Exception _e)
	{
		super(_e);
	}
}
