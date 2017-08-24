package team.balam.util.sqlite.connection;

import team.balam.util.sqlite.connection.pool.AlreadyExistsConnectionException;
import team.balam.util.sqlite.connection.vo.ResultAutoCloser;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseLoader {
	synchronized public static void load(String _name, String _path, boolean _isWAL) throws DatabaseLoadException {
		ResultAutoCloser.getInstance().start();
		Connection dbCon = null;
		Statement statement = null;

		try {
			if (PoolManager.containsConnection(_name)) {
				throw new AlreadyExistsConnectionException(_name);
			}

			File file = new File(_path);
			if (file.exists() && file.isDirectory()) {
				throw new DatabaseLoadException("This is not the type of file.");
			}

			Class.forName("org.sqlite.JDBC");

			dbCon = DriverManager.getConnection("jdbc:sqlite:" + _path);

			if (_isWAL) {
				statement = dbCon.createStatement();
				statement.execute("PRAGMA journal_mode=WAL;");
			}

			PoolManager.addConnection(_name, dbCon);
		} catch (Exception e) {
			if (dbCon != null) {
				try {
					dbCon.close();
				} catch (SQLException se) {
					throw new DatabaseLoadException(se);
				}
			}

			throw new DatabaseLoadException(e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	synchronized public static void load(String _name, String _path) throws Exception {
		load(_name, _path, false);
	}
}
