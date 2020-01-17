package top.starrysea.rina.jdbc.cp;

import top.starrysea.rina.util.exception.RinaException;
import top.starrysea.rina.util.factory.RinaObjectFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class RinaDataSource implements DataSource {
	private String url;
	private String driverClassName;
	private String username;
	private String password;

	private RinaDataSource(String url, String driverClassName, String username, String password) {
		this.url = url;
		this.driverClassName = driverClassName;
		this.username = username;
		this.password = password;
	}

	public static RinaDataSource getInstance(String url, String driverClassName, String username, String password) {
		RinaDataSource dataSource = RinaObjectFactory.getRinaObject(RinaDataSource.class);
		if (dataSource == null) {
			RinaDataSource newDataSource = new RinaDataSource(url, driverClassName, username, password);
			RinaObjectFactory.putRinaObject(RinaDataSource.class, newDataSource);
			return newDataSource;
		} else {
			return dataSource;
		}
	}

	@Override
	public Connection getConnection() throws SQLException {

		Connection connection;
		try {
			Class.forName(driverClassName);
			connection = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			throw new RinaException(e.getMessage(), e);
		}
		return connection;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return null;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {

	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {

	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}
}
