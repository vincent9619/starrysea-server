package top.starrysea.rina.core;

import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.util.exception.RinaException;
import top.starrysea.rina.util.factory.RinaObjectFactory;
import top.starrysea.rina.init.InitTaskList;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public class Rina {

    private static boolean isStart = false;

    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        iku();
    }

    public static void iku() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        try {
            InitTaskList initTaskList = RinaObjectFactory.generateRinaObject(InitTaskList.class);
            initTaskList.execute();
        } catch (Exception e) {
            throw new RinaException(e.getMessage(), e);
        }
        isStart = true;
        while (isStart) {
            //TODO 运行的代码
            RinaObjectFactory.generateRinaObject( HttpNIO.class);
            HttpNIO.executeNio();
        }
    }

    public static void yame() {
        isStart = false;
    }
}
