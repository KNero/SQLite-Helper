package team.balam.util.sqlite.connection.vo;

public class QueryVoFactory 
{
	public static QueryVO createInsert()
	{
		return new QueryVOImpl(QueryVO.Type.INSERT);
	}
	
	public static QueryVO createDelete()
	{
		return new QueryVOImpl(QueryVO.Type.DELETE);
	}
	
	public static QueryVO createUpdate()
	{
		return new QueryVOImpl(QueryVO.Type.UPDATE);
	}
	
	public static QueryVO createSelect()
	{
		return new QueryVOImpl(QueryVO.Type.SELECT);
	}
	
	public static QueryVO createExecute()
	{
		return new QueryVOImpl(QueryVO.Type.EXECUTE);
	}
}
