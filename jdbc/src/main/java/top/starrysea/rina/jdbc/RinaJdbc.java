package top.starrysea.rina.jdbc;

import com.google.common.base.CaseFormat;
import top.starrysea.rina.jdbc.cp.BasicConnectionPool;
import top.starrysea.rina.jdbc.cp.ConnectionPool;
import top.starrysea.rina.util.exception.RinaException;
import top.starrysea.rina.util.factory.RinaObjectFactory;
import top.starrysea.rina.util.json.JSONUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RinaJdbc {

	private ConnectionPool pool = RinaObjectFactory.getRinaObject(BasicConnectionPool.class);
	private String sql;

	public int delete(Object o, String idFieldName) throws SQLException {
		Map<String, Object> objectMap = JSONUtil.toMap(JSONUtil.toStr(o));
		Connection connection = pool.getConnection();
		String idColumnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, idFieldName);
		String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, o.getClass().getSimpleName());
		sql = "DELETE FROM " + tableName
				+ " WHERE " + idColumnName + "=? ";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setObject(1, objectMap.get(idFieldName));
		int result = statement.executeUpdate();
		pool.releaseConnection(connection);
		return result;
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
		Connection connection = pool.getConnection();
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
		int result = statement.executeUpdate();
		pool.releaseConnection(connection);
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
		Connection connection = pool.getConnection();
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
		int result = statement.executeUpdate();
		pool.releaseConnection(connection);
		return result;
	}

	public ResultSet find(RinaQuery query) throws SQLException {
		Connection connection = pool.getConnection();
		sql = query.getSql();
		List<Object> valueList = query.getValueList();
		PreparedStatement statement = connection.prepareStatement(sql);
		final int[] index = {1};
		valueList.stream().forEach(v -> {
			try {
				statement.setObject(index[0]++, v);
			} catch (SQLException e) {
				throw new RinaException(e.getMessage(), e);
			}
		});
		return statement.executeQuery();
	}
}
