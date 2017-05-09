package team.balam.util.sqlite.connection;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import team.balam.util.sqlite.connection.pool.AlreadyExistsConnectionException;
import team.balam.util.sqlite.connection.vo.ResultAutoCloser;

public class DatabaseLoader {
	synchronized public static void load(String _name, String _path, boolean _isWAL) throws AlreadyExistsConnectionException, DatabaseLoadException {
		ResultAutoCloser.getInstance().start();

		try {
			if (PoolManager.getInstance().containsConnection(_name)) {
				throw new AlreadyExistsConnectionException(_name);
			}

			File file = new File(_path);
			if (file.exists() && file.isDirectory()) {
				throw new DatabaseLoadException("This is not the type of file.");
			}

			Class.forName("org.sqlite.JDBC");

			Connection dbCon = null;

			try {
				dbCon = DriverManager.getConnection("jdbc:sqlite:" + _path);
			} catch (Exception e) {
				if (dbCon != null) {
					dbCon.close();
				}

				throw e;
			}

			if (_isWAL) {
				Statement statement = null;

				try {
					statement = dbCon.createStatement();
					statement.execute("PRAGMA journal_mode=WAL;");
				} catch (Exception e) {
					if (dbCon != null) {
						dbCon.close();
					}

					throw e;
				} finally {
					if (statement != null) {
						statement.close();
					}
				}
			}

			PoolManager.getInstance().addConnection(_name, dbCon);
		} catch (AlreadyExistsConnectionException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseLoadException(e);
		}
	}

	synchronized public static void load(String _name, String _path) throws Exception {
		load(_name, _path, false);
	}
}
