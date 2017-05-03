package team.balam.util.sqlite.test;

import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import team.balam.util.sqlite.connection.DatabaseLoader;
import team.balam.util.sqlite.connection.PoolManager;
import team.balam.util.sqlite.connection.pool.AlreadyExistsConnectionException;
import team.balam.util.sqlite.connection.vo.QueryVO;
import team.balam.util.sqlite.connection.vo.QueryVoFactory;
import team.balam.util.sqlite.connection.vo.Result;

public class QueryTest
{
	private static final String DB_NAME = "./db/test.db";
	private static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS TEST";
	private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS TEST(DATA1 TEXT, DATA2 NUMERIC, DATA3 INTEGER, PRIMARY KEY (DATA1, DATA2))";
	
	public static void initTest() throws Exception
	{
		try
		{
			DatabaseLoader.load(DB_NAME, DB_NAME, true);
		}
		catch(AlreadyExistsConnectionException e)
		{
			
		}
		
		QueryVO dropVo = QueryVoFactory.createExecute();
		dropVo.setQuery(DROP_TABLE_QUERY);
		
		PoolManager.getInstance().executeQuery(DB_NAME, dropVo);
		
		if(! dropVo.getResult().isSuccess())
		{
			throw dropVo.getResult().getException();
		}
		
		QueryVO createVo = QueryVoFactory.createExecute();
		createVo.setQuery(CREATE_TABLE_QUERY);
		
		PoolManager.getInstance().executeQuery(DB_NAME, createVo);
		
		if(! createVo.getResult().isSuccess())
		{
			throw createVo.getResult().getException();
		}
	}
	
	@Test
	public void testSelect() throws Exception
	{
		Result selectResult = null;
		
		try
		{
			initTest();
			
			QueryVO insertVo = QueryVoFactory.createInsert();
			insertVo.setQuery("INSERT INTO TEST VALUES('test1', 123, 456)");
			
			PoolManager.getInstance().executeQuery(DB_NAME, insertVo);
			
			if(! insertVo.getResult().isSuccess())
			{
				throw insertVo.getResult().getException();
			}
			
			QueryVO selectVo = QueryVoFactory.createSelect();
			selectVo.setQuery("SELECT * FROM TEST WHERE DATA1='test1'");
			
			PoolManager.getInstance().executeQuery(DB_NAME, selectVo);
			
			selectResult = selectVo.getResult();
			if(selectResult.isSuccess())
			{
				HashMap<String, Object> row = selectResult.getSelectResult().get(0);
				
				Assert.assertEquals("test1", row.get("data1"));
				Assert.assertEquals(123, row.get("data2"));
				Assert.assertEquals(456, row.get("data3"));
			}
			else
			{
				throw selectResult.getException();
			}
		}
		finally
		{
			if(selectResult != null)
			{
				selectResult.close();				
			}
		}
	}
	
	@Test
	public void testUpdate() throws Exception
	{
		Result selectResult = null;
		
		try
		{
			initTest();
			
			QueryVO insertVo = QueryVoFactory.createInsert();
			insertVo.setQuery("INSERT INTO TEST VALUES('test1', 123, 456)");
			
			PoolManager.getInstance().executeQuery(DB_NAME, insertVo);
			
			if(! insertVo.getResult().isSuccess())
			{
				throw insertVo.getResult().getException();
			}
			
			QueryVO updateVo = QueryVoFactory.createUpdate();
			updateVo.setQuery("UPDATE TEST SET DATA1='testtest', DATA2=1234, DATA3=9870 WHERE DATA1='test1'");
			
			PoolManager.getInstance().executeQuery(DB_NAME, updateVo);
			
			Assert.assertEquals(1, updateVo.getResult().getResultCount());
			
			QueryVO selectVo = QueryVoFactory.createSelect();
			selectVo.setQuery("SELECT * FROM TEST WHERE DATA1='testtest'");
			
			PoolManager.getInstance().executeQuery(DB_NAME, selectVo);
			
			selectResult = selectVo.getResult();
			if(selectResult.isSuccess())
			{
				HashMap<String, Object> row = selectResult.getSelectResult().get(0);
				
				Assert.assertEquals("testtest", row.get("data1"));
				Assert.assertEquals(1234, row.get("data2"));
				Assert.assertEquals(9870, row.get("data3"));
			}
			else
			{
				throw selectResult.getException();
			}
		}
		finally
		{
			if(selectResult != null)
			{
				selectResult.close();				
			}
		}
	}
	
	@Test
	public void testDelete() throws Exception
	{
		Result selectResult = null;
		
		try
		{
			initTest();
			
			QueryVO insertVo = QueryVoFactory.createInsert();
			insertVo.setQuery("INSERT INTO TEST VALUES('test1', 123, 456)");
			
			PoolManager.getInstance().executeQuery(DB_NAME, insertVo);
			
			if(! insertVo.getResult().isSuccess())
			{
				throw insertVo.getResult().getException();
			}
			
			QueryVO deleteVo = QueryVoFactory.createDelete();
			deleteVo.setQuery("DELETE FROM TEST WHERE DATA1='test1'");
			
			PoolManager.getInstance().executeQuery(DB_NAME, deleteVo);
			
			if(! deleteVo.getResult().isSuccess())
			{
				throw deleteVo.getResult().getException();
			}
			
			Assert.assertEquals(1, deleteVo.getResult().getResultCount());
			
			QueryVO selectVo = QueryVoFactory.createSelect();
			selectVo.setQuery("SELECT * FROM TEST WHERE DATA1='test1'");
			
			PoolManager.getInstance().executeQuery(DB_NAME, selectVo);
			
			selectResult = selectVo.getResult();
			if(selectResult.isSuccess())
			{
				Assert.assertEquals(0, selectVo.getResult().getSelectResult().size());
			}
			else
			{
				throw selectResult.getException();
			}
		}
		finally
		{
			if(selectResult != null)
			{
				selectResult.close();				
			}
		}
	}
	
	@Test
	public void testSelectTableMeta() throws Exception
	{
		initTest();
		
		Result selectResult = null;
		
		try
		{
			QueryVO selectVo = QueryVoFactory.createSelect();
			selectVo.setQuery("PRAGMA table_info(test)");
			
			PoolManager.getInstance().executeQuery(DB_NAME, selectVo);
			
			selectResult = selectVo.getResult();
			if(selectResult.isSuccess())
			{
				List<HashMap<String, Object>> list = selectResult.getSelectResult();
				
				Assert.assertEquals(3, list.size());
				
				Assert.assertEquals("DATA1", list.get(0).get("name"));
				Assert.assertEquals("TEXT", list.get(0).get("type"));
				
				Assert.assertEquals("DATA2", list.get(1).get("name"));
				Assert.assertEquals("NUMERIC", list.get(1).get("type"));
				
				Assert.assertEquals("DATA3", list.get(2).get("name"));
				Assert.assertEquals("INTEGER", list.get(2).get("type"));
			}
			else
			{
				throw selectResult.getException();
			}
		}
		finally
		{
			if(selectResult != null)
			{
				selectResult.close();				
			}
		}
	}
	
	@Test
	public void testSelectTableIndex() throws Exception
	{
		initTest();
		
		Result selectResult = null;
		
		try
		{
			QueryVO selectVo = QueryVoFactory.createSelect();
			selectVo.setQuery("PRAGMA index_list(test)");
			
			PoolManager.getInstance().executeQuery(DB_NAME, selectVo);
			
			selectResult = selectVo.getResult();
			if(selectResult.isSuccess())
			{
				if(selectResult.getSelectResult().size() > 0)
				{
					HashMap<String, Object> output = selectResult.getSelectResult().get(0);
					System.out.println(output.get("seq")); //A sequence number assigned to each index for internal tracking purposes.
					System.out.println(output.get("name")); //The name of the index.
					System.out.println(output.get("unique")); //"1" if the index is UNIQUE and "0" if not.
					System.out.println(output.get("origin")); //"c" if the index was created by a CREATE INDEX statement, "u" if the index was created by a UNIQUE constraint, or "pk" if the index was created by a PRIMARY KEY constraint.
					System.out.println(output.get("partial")); //"1" if the index is a partial index and "0" if not.
					
					Result selectResult2 = null;
					QueryVO selectVo2 = QueryVoFactory.createSelect();
					selectVo2.setQuery("PRAGMA index_info(" + output.get("name") + ")");
					
					try
					{
						PoolManager.getInstance().executeQuery(DB_NAME, selectVo2);
						selectResult2 = selectVo2.getResult();
						if(selectResult2.isSuccess())
						{
							for(HashMap<String, Object> output2 : selectResult2.getSelectResult())
							{
								System.out.println(output2.get("seqno"));
								System.out.println(output2.get("name"));
								System.out.println(output2.get("cid"));
							}
						}
						else
						{
							throw selectResult2.getException();
						}
					}
					finally
					{
						if(selectResult != null)
						{
							selectResult2.close();				
						}
					}
				}
			}
			else
			{
				throw selectResult.getException();
			}
		}
		finally
		{
			if(selectResult != null)
			{
				selectResult.close();				
			}
		}
	}
}
