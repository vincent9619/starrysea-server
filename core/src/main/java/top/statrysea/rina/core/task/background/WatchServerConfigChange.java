package top.statrysea.rina.core.task.background;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.util.factory.RinaObjectFactory;
import top.starrysea.rina.util.file.FileUtil;
import top.statrysea.rina.core.annotation.BackgroundTask;
import top.statrysea.rina.init.ServerConfig;

import java.util.concurrent.TimeUnit;

@Getter
@Slf4j
@BackgroundTask
public class WatchServerConfigChange extends BasicBackgroundTask {
	// 定时检测服务器配置文件变化
	private int time = 1;
	private TimeUnit timeUnit = TimeUnit.SECONDS;

	@Override
	public void execute() {
		ServerConfig currentConfig = RinaObjectFactory.getRinaObject(ServerConfig.class);
		ServerConfig newConfig = FileUtil.readYamlFile("serverConfig.yaml", ServerConfig.class);
		if (!newConfig.equals(currentConfig)) {
			RinaObjectFactory.putRinaObject(ServerConfig.class, newConfig);
			log.info("服务器配置文件变更。新的配置如下：");
			log.info(RinaObjectFactory.getRinaObject(ServerConfig.class).toString());
		}
	}
}
