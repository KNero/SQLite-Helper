package team.balam.util.sqlite.connection.vo;

public class QueryTimeoutException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private String query;
	private Object[] param;

	public QueryTimeoutException(String _query, Object[] _param) {
		this.query = _query;
		this.param = _param;
	}
	
	public String getMessage() {
		StringBuilder msg = new StringBuilder("\n# Query : ");
		msg.append(this.query);
		msg.append("\n# Parameter : ");
		
		if (this.param != null) {
			for (Object p : this.param) {
				msg.append("[").append(p.toString()).append("]");
			}
		}
		
		return msg.toString();
	}
}
