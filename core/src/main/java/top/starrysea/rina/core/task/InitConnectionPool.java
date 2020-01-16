package top.starrysea.rina.core.task;

import top.starrysea.rina.init.ServerConfig;
import top.starrysea.rina.jdbc.cp.BasicConnectionPool;
import top.starrysea.rina.util.exception.RinaException;
import top.starrysea.rina.util.factory.RinaObjectFactory;

import java.sql.SQLException;

public class InitConnectionPool {
	public void execute() {
		try {
			ServerConfig config = RinaObjectFactory.getRinaObject(ServerConfig.class);
			BasicConnectionPool pool = BasicConnectionPool
					.create(config.getJdbcDriver(), config.getJdbcUrl(), config.getJdbcUsername(), config.getJdbcPassword());
			RinaObjectFactory.putRinaObject(BasicConnectionPool.class, pool);
		} catch (SQLException e) {
			throw new RinaException(e.getMessage(), e);
		}
	}
}
