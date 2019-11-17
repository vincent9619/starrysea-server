package top.statrysea.rina.init;

import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.util.collection.RinaArrayList;
import top.starrysea.rina.util.factory.RinaObjectFactory;
import top.starrysea.rina.util.file.FileUtil;
import top.starrysea.rina.util.thread.ThreadUtil;

import java.util.concurrent.TimeUnit;

@Slf4j
public class InitTaskList {

    private RinaArrayList<InitTask> initTaskList;

    public InitTaskList() {
        initTaskList = new RinaArrayList<>();
        initTaskList.add(initServerConfigTask);
        initTaskList.add(initServerBackgroundTask);
    }

    private static InitTask initServerConfigTask = () -> {
        log.info("执行初始化服务器配置的初始化任务");
	    ServerConfig initConfig = FileUtil.readYamlFile("serverConfig.yaml", ServerConfig.class);
	    RinaObjectFactory.putRinaObject(ServerConfig.class, initConfig);
	    log.info("服务器配置初始化完成。将使用以下配置：");
	    log.info(RinaObjectFactory.getRinaObject(ServerConfig.class).toString());
    };

    private static InitTask initServerBackgroundTask = () -> {
        log.info("执行初始化服务器后台任务的初始化任务");

	    // 定时刷新服务器设置
	    ThreadUtil.registerScheduleTask(() -> {
		    ServerConfig currentConfig = RinaObjectFactory.getRinaObject(ServerConfig.class);
		    ServerConfig newConfig = FileUtil.readYamlFile("serverConfig.yaml", ServerConfig.class);
		    if (!newConfig.equals(currentConfig)) {
			    RinaObjectFactory.putRinaObject(ServerConfig.class, newConfig);
			    log.info("服务器配置文件变更。新的配置如下：");
			    log.info(RinaObjectFactory.getRinaObject(ServerConfig.class).toString());
		    }
		    return 0;
	    }, 1, TimeUnit.SECONDS);

    };

    public void execute() {
        initTaskList.forEach(InitTask::execute);
    }
}
