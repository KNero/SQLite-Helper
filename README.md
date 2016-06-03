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
Result is query result interface. QueryVO has result object and result object can checking success.
<pre><code>team.balam.util.sqlite.connection.vo.Result result = select.getResult();

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
</code></pre>
