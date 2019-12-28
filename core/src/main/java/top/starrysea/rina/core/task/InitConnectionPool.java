package top.starrysea.rina.core.task;

import top.starrysea.rina.jdbc.cp.BasicConnectionPool;
import top.starrysea.rina.util.exception.RinaException;
import top.starrysea.rina.util.factory.RinaObjectFactory;

import java.sql.SQLException;

public class InitConnectionPool {
	public void execute() {
		try {
			BasicConnectionPool pool = BasicConnectionPool
					.create("jdbc:mysql://47.93.223.78:3306/testdb", "testdb", "niconiconi");
			RinaObjectFactory.putRinaObject(BasicConnectionPool.class, pool);
		} catch (SQLException e) {
			throw new RinaException(e.getMessage(), e);
		}
	}
}
