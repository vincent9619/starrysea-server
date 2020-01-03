package top.starrysea.rina.init;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import top.starrysea.rina.basic.annotation.BackgroundTask;
import top.starrysea.rina.core.task.InitConnectionPool;
import top.starrysea.rina.core.task.InitRouter;
import top.starrysea.rina.core.task.ObjectInject;
import top.starrysea.rina.core.task.background.BackgroundTaskInterface;
import top.starrysea.rina.util.collection.RinaArrayList;
import top.starrysea.rina.util.exception.RinaException;
import top.starrysea.rina.util.factory.RinaObjectFactory;
import top.starrysea.rina.util.file.FileUtil;
import top.starrysea.rina.util.thread.ThreadUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

@Slf4j
public class InitTaskList {

    private RinaArrayList<InitTask> initTaskList;

    public InitTaskList() {
        initTaskList = new RinaArrayList<>();
        initTaskList.add(initServerConfigTask);
        initTaskList.add(initConnectionPoolTask);
        initTaskList.add(initObjectInjectionTask);
        initTaskList.add(initRouterTask);
        initTaskList.add(initServerBackgroundTask);
    }

    private static InitTask initServerConfigTask = () -> {
        log.info("执行初始化服务器配置的初始化任务");
        ServerConfig initConfig = FileUtil.readYamlFile("serverConfig.yaml", ServerConfig.class);
        RinaObjectFactory.putRinaObject(ServerConfig.class, initConfig);
        log.info("服务器配置初始化完成。将使用以下配置：");
        log.info(RinaObjectFactory.getRinaObject(ServerConfig.class).toString());
    };

    private static InitTask initObjectInjectionTask = () -> {
        log.info("执行对象注入任务");
        try {
            ObjectInject task = RinaObjectFactory.generateRinaObject(ObjectInject.class);
            task.execute();
            log.info("对象注入完成");
        } catch (Exception e) {
            throw new RinaException(e.getMessage(), e);
        }
    };

	private static InitTask initRouterTask = () -> {
		log.info("执行初始化路由任务");
		try {
			InitRouter task = RinaObjectFactory.generateRinaObject(InitRouter.class);
			task.execute();
			log.info("路由初始化完成");
		} catch (Exception e) {
			throw new RinaException(e.getMessage(), e);
		}
	};

    private static InitTask initConnectionPoolTask = () -> {
        log.info("执行初始化数据库连接池任务");
        try {
            InitConnectionPool task = RinaObjectFactory.generateRinaObject(InitConnectionPool.class);
            task.execute();
            log.info("数据库连接池初始化完成");
        } catch (Exception e) {
            throw new RinaException(e.getMessage(), e);
        }
    };

    private static InitTask initServerBackgroundTask = () -> {
        log.info("执行初始化服务器后台任务的初始化任务");

        // 筛选包含特定注解的类，实例化后定期执行
        Reflections reflections = new Reflections(RinaObjectFactory.getRinaObject(ServerConfig.class).getBasePackage());
        Set<Class<?>> backgroundTasks = reflections.getTypesAnnotatedWith(BackgroundTask.class);
        backgroundTasks.stream().forEach(aClass -> {
            try {
                BackgroundTaskInterface backgroundTask = (BackgroundTaskInterface) aClass.getConstructor().newInstance();
                BackgroundTask b = aClass.getAnnotation(BackgroundTask.class);
                ThreadUtil.registerScheduleTask(backgroundTask::execute, b.time(), b.timeUnit());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new RinaException(e.getMessage(), e);
            }
        });
    };

    public void execute() {
        initTaskList.forEach(InitTask::execute);
    }
}
