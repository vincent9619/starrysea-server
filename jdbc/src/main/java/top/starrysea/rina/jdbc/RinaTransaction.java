package top.starrysea.rina.jdbc;

import top.starrysea.rina.util.exception.RinaException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.HashMap;
import java.util.Map;

public class RinaTransaction {
	private Connection connection;
	private Map<String, Savepoint> savepointMap = new HashMap<>();

	public RinaTransaction(Connection connection) throws SQLException {
		this.connection = connection;
	}

	public void begin() throws SQLException {
		connection.setAutoCommit(false);
	}

	public void setTransactionIsolation(int level) throws SQLException {
		connection.setTransactionIsolation(level);
	}

	public void commit() throws SQLException {
		connection.commit();
	}

	public void rollback() throws SQLException {
		connection.rollback();
	}

	public void rollbackTo(String name) throws SQLException {
		Savepoint savepoint = savepointMap.get(name);
		if (savepoint != null) {
			connection.rollback(savepoint);
		} else {
			throw new RinaException("名为 " + name + " 的检查点不存在");
		}
	}

	public void setSavepoint(String name) throws SQLException {
		Savepoint savepoint = connection.setSavepoint(name);
		savepointMap.put(name, savepoint);
	}

}
