# SQLite-Helper
SQLite Single Thread And Single Connection Pool

### Create or load DB File
You can load many database.
```java
DatabaseLoader.load("Test1", "./test1.db");
DatabaseLoader.load("Test1", "./test2.db");
```
* param1 : database name
* param2 : database file path and name

### QueryVO
QueryVO is interface for execute query.
```java
team.balam.util.sqlite.connection.vo.QueryVo insertVo = QueryVoFactory.create(QueryVo.Type.INSERT);
```
```java
QueryVO selectVo = QueryVoFactory.create(QueryVo.Type.SELECT);
```
```java
QueryVO updateVo = QueryVoFactory.create(QueryVo.Type.UPDATE);
```
```java
QueryVO deleteVo = QueryVoFactory.create(QueryVo.Type.DELETE);
```
```java
QueryVO executeVo = QueryVoFactory.create(QueryVo.Type.EXECUTE);
```

#### Make QueryVo
User QueryVo.setQuery(String) and QueryVo.setParam(Object[]) for execute query.
```java
QueryVo select = QueryVoFactory.create(QueryVo.Type.SELECT);
select.setQuery("SELECT * FROM TEST WHERE DATA1=?");
select.setParam(new Object[]{"1111"});
```

### Execute query
```java
PoolManager.getInstance().executeQuery(dbName, queryVo);
```
    
#### Get query result
Result object is query result interface. QueryVO has result object and result object can check success.
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
And Result can convert to list. List contents format is Map<String, String>
* key is column name(lowercase), value is column value.

```java
List<HashMap<String, String>> list = result.getSelectResult();
			
for(HashMap<String, String> m : list)
{
	System.out.println(m.get("data1"));
	System.out.println(m.get("data2"));
	System.out.println(m.get("data3"));
	System.out.println();
}
```

Update and delete result can get count.

```java
int count = result.getResultCount();
```
You can get exception when fail to execute query

```java
String errorMsg = result.getErrorMessage();
Exception excep = result.getException();
```

#### team.balam.util.sqlite.connection.vo.Result object must call close()
```java
result.close();
```
#### Remove connection from connection pool
```java
PoolManager.getInstance().removeConnection(connectionName);
```
#### Stop connection pool
Don't stop all thread when you not call destroyPool().
```java
PoolManager.getInstance().destroyPool();
```
