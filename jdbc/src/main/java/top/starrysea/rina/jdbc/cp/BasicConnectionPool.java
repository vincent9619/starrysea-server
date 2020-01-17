package top.starrysea.rina.jdbc.cp;

import lombok.Getter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BasicConnectionPool implements ConnectionPool {
	private String driver;
	private String url;
	private String user;
	private String password;
	private List<Connection> connectionPool;
	private List<Connection> usedConnections = new ArrayList<>();
	private static int INITIAL_POOL_SIZE = 10;
	private static int MAX_POOL_SIZE = 16;

	private BasicConnectionPool(String driver, String url, String user, String password, List<Connection> pool) {
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;
		this.connectionPool = pool;
	}

	public static BasicConnectionPool create(
			String driver, String url, String user,
			String password) throws SQLException {

		List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
		for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
			pool.add(createConnection(driver, url, user, password));
		}
		return new BasicConnectionPool(driver, url, user, password, pool);
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (connectionPool.isEmpty()) {
			if (getSize() < MAX_POOL_SIZE) {
				connectionPool.add(createConnection(driver, url, user, password));
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
			connectionPool.add(createConnection(driver, url, user, password));
		} else {
			connectionPool.add(connection);
		}
		return usedConnections.remove(connection);
	}

	private static Connection createConnection(String driver, String url, String user, String password) throws SQLException {
		RinaDataSource dataSource = RinaDataSource.getInstance(url, driver, user, password);
		return dataSource.getConnection();
	}

	public int getSize() {
		return connectionPool.size() + usedConnections.size();
	}

}
