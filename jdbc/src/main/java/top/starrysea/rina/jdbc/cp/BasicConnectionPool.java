package top.starrysea.rina.jdbc.cp;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BasicConnectionPool implements ConnectionPool {
	private String url;
	private String user;
	private String password;
	private List<Connection> connectionPool;
	private List<Connection> usedConnections = new ArrayList<>();
	private static int INITIAL_POOL_SIZE = 10;
	private static int MAX_POOL_SIZE = 16;

	private BasicConnectionPool(String url, String user, String password, List<Connection> pool) {
		this.url = url;
		this.user = user;
		this.password = password;
		this.connectionPool = pool;
	}

	public static BasicConnectionPool create(
			String url, String user,
			String password) throws SQLException {

		List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
		for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
			pool.add(createConnection(url, user, password));
		}
		return new BasicConnectionPool(url, user, password, pool);
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (connectionPool.isEmpty()) {
			if (getSize() < MAX_POOL_SIZE) {
				connectionPool.add(createConnection(url, user, password));
			} else {
				throw new RuntimeException(
						"Maximum pool size reached, no available connections!");
			}
		}

		Connection connection = connectionPool
				.remove(connectionPool.size() - 1);
		usedConnections.add(connection);
		return connection;
	}

	@Override
	public boolean releaseConnection(Connection connection) throws SQLException {
		if (connection.isClosed() || !connection.getAutoCommit()) {
			connectionPool.add(createConnection(url, user, password));
		} else {
			connectionPool.add(connection);
		}
		return usedConnections.remove(connection);
	}

	private static Connection createConnection(
			String url, String user, String password)
			throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	public int getSize() {
		return connectionPool.size() + usedConnections.size();
	}


}
