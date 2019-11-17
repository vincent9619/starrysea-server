package top.statrysea.rina.core;

import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.util.exception.RinaException;
import top.starrysea.rina.util.factory.RinaObjectFactory;
import top.statrysea.rina.init.InitTaskList;

@Slf4j
public class Rina {

    private static boolean isStart = false;

    public static void main(String[] args) {
        iku();
    }

    public static void iku() {
        try {
            InitTaskList initTaskList = RinaObjectFactory.generateRinaObject(InitTaskList.class);
            initTaskList.execute();
        } catch (Exception e) {
            throw new RinaException(e.getMessage(), e);
        }
        isStart = true;
        while (isStart) {
            //TODO 这里接收请求,使用NIO的方式
        }
    }

    public static void yame() {
        isStart = false;
    }
}
