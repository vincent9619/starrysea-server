package top.starrysea.rina.jdbc;

import com.google.common.base.CaseFormat;
import top.starrysea.rina.jdbc.cp.BasicConnectionPool;
import top.starrysea.rina.jdbc.cp.ConnectionPool;
import top.starrysea.rina.util.exception.RinaException;
import top.starrysea.rina.util.factory.RinaObjectFactory;
import top.starrysea.rina.util.json.JSONUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RinaTransaction {
	Connection connection;
	String sql;
	Map<String, Savepoint> savepointMap = new HashMap<>();
	private ConnectionPool pool = RinaObjectFactory.getRinaObject(BasicConnectionPool.class);

	public RinaTransaction() throws SQLException {
		connection = pool.getConnection();
		connection.setAutoCommit(false);
	}

	public void setTransactionIsolation(int level) throws SQLException {
		connection.setTransactionIsolation(level);
	}

	public void commit() throws SQLException {
		connection.commit();
		pool.releaseConnection(connection);
	}

	public void rollback() throws SQLException {
		connection.rollback();
	}

	public void rollbackTo(String name) throws SQLException {
		Savepoint savepoint = savepointMap.get(name);
		if (savepoint != null) {
			connection.rollback(savepoint);
		}
	}

	public void setSavepoint(String name) throws SQLException {
		Savepoint savepoint = connection.setSavepoint(name);
		savepointMap.put(name, savepoint);
	}

	public int insert(Object o) throws SQLException {
		Map<String, Object> objectMap = JSONUtil.toMap(JSONUtil.toStr(o));
		List<String> keyList = new ArrayList<>();
		List<Object> valueList = new ArrayList<>();
		List<String> questionList = new ArrayList<>();
		String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, o.getClass().getSimpleName());
		objectMap.forEach((key, value) -> {
			keyList.add(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, key));
			valueList.add(value);
			questionList.add("?");
		});
		String keys = String.join(",", keyList);
		String questions = String.join(",", questionList);
		sql = "INSERT INTO " + tableName
				+ "(" + keys + ")" + " VALUES (" + questions + ") ";
		PreparedStatement statement = connection.prepareStatement(sql);
		final int[] index = {1};
		valueList.stream().forEach(v -> {
			try {
				statement.setObject(index[0]++, v);
			} catch (SQLException e) {
				throw new RinaException(e.getMessage(), e);
			}
		});
		return statement.executeUpdate();
	}

	public int delete(Object o, String idFieldName) throws SQLException {
		Map<String, Object> objectMap = JSONUtil.toMap(JSONUtil.toStr(o));
		String idColumnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, idFieldName);
		String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, o.getClass().getSimpleName());
		sql = "DELETE FROM " + tableName
				+ " WHERE " + idColumnName + "=? ";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setObject(1, objectMap.get(idFieldName));
		int result = statement.executeUpdate();
		return result;
	}

	public int update(Object o, String idFieldName) throws SQLException {
		Map<String, Object> objectMap = JSONUtil.toMap(JSONUtil.toStr(o));
		List<String> updateList = new ArrayList<>();
		List<Object> valueList = new ArrayList<>();
		String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, o.getClass().getSimpleName());
		String idColumnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, idFieldName);
		objectMap.forEach((key, value) -> {
			valueList.add(value);
			updateList.add(String.format("%s=?", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, key)));
		});
		String updateDetail = String.join(",", updateList);
		sql = "UPDATE " + tableName
				+ " SET " + updateDetail
				+ " WHERE " + idColumnName + "=? ";
		PreparedStatement statement = connection.prepareStatement(sql);
		final int[] index = {1};
		valueList.stream().forEach(v -> {
			try {
				statement.setObject(index[0]++, v);
			} catch (SQLException e) {
				throw new RinaException(e.getMessage(), e);
			}
		});
		statement.setObject(index[0], objectMap.get(idFieldName));
		return statement.executeUpdate();
	}

}
