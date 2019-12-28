package top.starrysea.rina.jdbc;

import com.google.common.base.CaseFormat;
import top.starrysea.rina.jdbc.enums.WhereType;

import java.util.ArrayList;
import java.util.List;

public class RinaQuery {
	private String sql = "";
	private String lastOperation;
	private Class<?> thisTable;
	private String thisTableName;
	private List<Object> valueList = new ArrayList<>();
	private boolean hasWhere = false;
	private boolean hasOrder = false;
	private boolean hasLimit = false;

	public RinaQuery(Class<?> table, String... fieldNames) {
		lastOperation = "select";
		thisTable = table;
		thisTableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, thisTable.getSimpleName());
		String fields = "*";
		if (fieldNames.length == 1) {
			fields = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldNames[0]);
		} else if (fieldNames.length > 1) {
			List<String> underscoreList = new ArrayList<>();
			for (String fieldName : fieldNames) {
				underscoreList.add(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName));
			}
			fields = String.join(",", underscoreList);
		}
		sql += "SELECT " + fields + " FROM " + thisTableName + " ";
	}

	public void addWhere(String thisFieldName, WhereType type, Object staticValue) {
		if (!lastOperation.equals("select") && !lastOperation.equals("leftJoin") && hasWhere) {
			return;
		}
		String columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, thisFieldName);
		lastOperation = "where";
		String where = "WHERE " + thisTableName + "." + columnName + type.getChara() + "? ";
		hasWhere = true;
		sql += where;
		valueList.add(staticValue);
	}

	public void addAnd(String thisFieldName, WhereType type, Object staticValue) {
		if (!lastOperation.equals("where")) {
			return;
		}
		String columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, thisFieldName);
		String and = "AND " + thisTableName + "." + columnName + type.getChara() + "? ";
		sql += and;
		valueList.add(staticValue);
	}

	public void addOr(String thisFieldName, WhereType type, Object staticValue) {
		if (!lastOperation.equals("where")) {
			return;
		}
		String columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, thisFieldName);
		String or = "OR " + thisTableName + "." + columnName + type.getChara() + "? ";
		sql += or;
		valueList.add(staticValue);
	}

	public void addOrder(boolean isAscend, String... fieldNames) {
		if (hasOrder) {
			return;
		}
		String columnNames;
		if (fieldNames.length == 0) {
			return;
		} else if (fieldNames.length == 1) {
			columnNames = thisTableName + "." + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldNames[0]);
		} else {
			List<String> underscoreList = new ArrayList<>();
			for (String fieldName : fieldNames) {
				String column = thisTableName + "." + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName);
				underscoreList.add(column);
			}
			columnNames = String.join(",", underscoreList);
		}
		hasOrder = true;
		sql += "ORDER BY " + columnNames + " " + (isAscend ? "ASC" : "DESC") + " ";
	}

	public void leftJoin(Class<?> thatTable, String thisFieldName, String thatFieldName) {
		if (!lastOperation.equals("select")) {
			return;
		}
		lastOperation = "leftJoin";
		String thatTableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, thatTable.getSimpleName());
		String thisColumnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, thisFieldName);
		String thatColumnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, thatFieldName);
		String join = "LEFT JOIN " + thatTableName + " ON " + thisTableName + "." + thisColumnName + "=" + thatTableName + "." + thatColumnName + " ";
		sql += join;
	}

	public void addLimit(int min, int max) {
		if (hasLimit) {
			return;
		}
		lastOperation = "limit";
		String limit = "LIMIT " + min + "," + max + " ";
		hasLimit = true;
		sql += limit;
	}

	public void addLimit(int max) {
		if (hasLimit) {
			return;
		}
		lastOperation = "limit";
		String limit = "LIMIT " + max + " ";
		hasLimit = true;
		sql += limit;
	}

	public String getSql() {
		return sql;
	}

	public List<Object> getValueList() {
		return valueList;
	}
}
