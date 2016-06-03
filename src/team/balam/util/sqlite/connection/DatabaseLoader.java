package team.balam.util.sqlite.connection;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class DatabaseLoader 
{
	private static AtomicBoolean isInitJDBC = new AtomicBoolean(false);
	
	synchronized public static void load(String _name, String _path, boolean _isWAL) throws Exception
	{
		if(! isInitJDBC.getAndSet(true)) 
		{
			Class.forName("org.sqlite.JDBC");
		}
		
		File file = new File( _path );
		if(file.exists() && file.isDirectory())
		{
			throw new Exception("This is not the type of file.");
		}
	
		PoolManager.getInstance().createConnection( _name, _path, _isWAL);
	}
	
	synchronized public static void load(String _name, String _path) throws Exception
	{
		load(_name, _path, false);
	}
}
