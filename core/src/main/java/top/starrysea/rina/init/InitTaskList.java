package top.starrysea.rina.init;

import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.util.collection.RinaArrayList;

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
        //TODO 读取默认的yaml文件并装配到ServerConfig中,然后将该对象放到RinaObjectFactory
    };

    private static InitTask initServerBackgroundTask = () -> {
        log.info("执行初始化服务器后台任务的初始化任务");
        //TODO 服务器的后台任务，例如不断读取默认的yaml文件与现有的ServerConfig比较并刷新
    };

    public void execute() {
        initTaskList.forEach(InitTask::execute);
    }
}
