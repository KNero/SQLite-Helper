package team.balam.util.sqlite.connection.vo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SelectResultInfo {
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private List<Map<String, Object>> resultList;

    public void setInfo(PreparedStatement _preparedStatement, ResultSet _resultSet) {
        this.preparedStatement = _preparedStatement;
        this.resultSet = _resultSet;
    }

    public List<Map<String, Object>> getResultList() throws SQLException {
        if (this.resultList == null) {
            this.resultList = new ArrayList<Map<String, Object>>();

            ResultSetMetaData rsmd = this.resultSet.getMetaData();
            int columnSize = rsmd.getColumnCount();

            while (this.resultSet.next()) {
                HashMap<String, Object> m = new HashMap<String, Object>();

                for (int i = 1; i <= columnSize; ++i) {
                    String columnName = rsmd.getColumnName(i).toLowerCase();
                    Object columnValue = this.resultSet.getObject(i);

                    m.put(columnName, columnValue);
                }

                this.resultList.add(m);
            }
        }

        return this.resultList;
    }

    public ResultSet getResultSet() {
        return this.resultSet;
    }

    public void close() throws SQLException {
        if (this.preparedStatement != null) this.preparedStatement.close();
        if (this.resultSet != null) this.resultSet.close();
    }

    public boolean isClosed() throws SQLException {
        return this.resultSet.isClosed();
    }
}
