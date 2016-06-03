# SQLite-Helper
SQLite Single Thread Connection Pool

###Create or load DB File
    DatabaseLoader.load("Test", "./test.db");
* param1 : database name
* param2 : database file name

###Get database connection
    import team.balam.util.sqlite.connection.pool.Connection
    
    Connection con = PoolManager.getInstance().getConnection("Test");

###QueryVO
QueryVO is interface for execute query.
    QueryVO insertVo = QueryVoFactory.createInsert();
    QueryVO selectVo = QueryVoFactory.createSelect();
    QueryVO updateVo = QueryVoFactory.createUpdate();
    QueryVO deleteVo = QueryVoFactory.createDelete();
    QueryVO executeVo = QueryVoFactory.createExecute();
    
