package team.balam.util.sqlite.connection;

import java.io.File;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicBoolean;

import team.balam.util.sqlite.connection.pool.Connection;
import team.balam.util.sqlite.connection.vo.QueryVO;
import team.balam.util.sqlite.connection.vo.QueryVoFactory;
import team.balam.util.sqlite.connection.vo.Result;

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
	
	public static void main(String[] _args) throws Exception
	{
		DatabaseLoader.load("Test", "./test.bsl");
		
		Connection con = PoolManager.getInstance().getConnection("Test");
		
		QueryVO vo = QueryVoFactory.createInsert();
		vo.setQuery("INSERT INTO TEST VALUES( '1111','44444','66666' )");
		con.query(vo);
		
		QueryVO vo1 = QueryVoFactory.createSelect();
		vo1.setQuery("SELECT * FROM TEST WHERE DATA1=?");
		vo1.setParam(new Object[]{"1111"});
		con.query(vo1);
		
		Result result = vo1.getResult();
		
		if(result.isSuccess())
		{
			ResultSet rs = result.getResultSet();
			while(rs.next())
			{
				System.out.println(rs.getString(1));
				System.out.println(rs.getString(2));
				System.out.println(rs.getString(3));
				System.out.println();
			}
		}
		
		result.close();
		
		PoolManager.getInstance().destoryPool();
	}
}
