# SQLite-Helper
SQLite Single Thread And Single Connection Pool

###Create or load DB File
    DatabaseLoader.load("Test", "./test.db");
* param1 : database name
* param2 : database file name

###Get database connection
    import team.balam.util.sqlite.connection.pool.Connection
    
    Connection connnection = PoolManager.getInstance().getConnection("Test");

###QueryVO
QueryVO is interface for execute query.
<pre><code>QueryVO insertVo = QueryVoFactory.createInsert();</code></pre>
<pre><code>QueryVO selectVo = QueryVoFactory.createSelect();</code></pre>
<pre><code>QueryVO updateVo = QueryVoFactory.createUpdate();</code></pre>
<pre><code>QueryVO deleteVo = QueryVoFactory.createDelete();</code></pre>
<pre><code>QueryVO executeVo = QueryVoFactory.createExecute();</code></pre>

####Make QueryVO
User QueryVO.setQuery(String) and QueryVO.setParam(Object[]) for execute query.
<pre><code>QueryVO select = QueryVoFactory.createSelect();
select.setQuery("SELECT * FROM TEST WHERE DATA1=?");
select.setParam(new Object[]{"1111"});</code></pre>

###Execute query
    connnection.query(select);
    
####Get query result
Result is query result interface. QueryVO has result object and result object can check success.
```java
team.balam.util.sqlite.connection.vo.Result result = select.getResult();

if(result.isSuccess())
{
    java.sql.ResultSet rs = result.getResultSet();
    while(rs.next())
    {
        System.out.println(rs.getString(1));
        System.out.println(rs.getString(2));
	System.out.println(rs.getString(3));
	System.out.println();
	}
}
```
And Result can convert to list. List contents format is Map<String, String>(key is column name(lowercase), value is column value).
```java
List<HashMap<String, String>> list = result.getSelectResult();</pre>
			
for(HashMap<String, String> m : list)
{
	System.out.println(m.get("data1"));
	System.out.println(m.get("data2"));
	System.out.println(m.get("data3"));
	System.out.println();
}
```

Update and delete result can get count.
<pre><code>int count = result.getResultCount();</code></pre>
You can get exception when fail to execute query 
<pre><code>String errorMsg = result.getErrorMessage();
Exception excep = result.getException();</code></pre>

####Result object must call close()
<pre><code>result.close();</code></pre>
