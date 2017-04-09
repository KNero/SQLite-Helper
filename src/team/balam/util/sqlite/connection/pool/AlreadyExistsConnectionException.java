package team.balam.util.sqlite.connection.pool;

public class AlreadyExistsConnectionException extends Exception
{
	private static final long serialVersionUID = 1L;

	public AlreadyExistsConnectionException(String _conName)
	{
		super("[ " + _conName + " ] The connection already exists.");
	}
}
