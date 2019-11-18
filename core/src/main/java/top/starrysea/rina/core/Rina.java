package top.starrysea.rina.core;

import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.util.exception.RinaException;
import top.starrysea.rina.util.factory.RinaObjectFactory;
import top.starrysea.rina.init.InitTaskList;

import java.io.IOException;

@Slf4j
public class Rina {

    private static boolean isStart = false;

    public static void main(String[] args)throws IOException {
        iku();
    }

    public static void iku() throws IOException {
        try {
            InitTaskList initTaskList = RinaObjectFactory.generateRinaObject(InitTaskList.class);
            initTaskList.execute();
        } catch (Exception e) {
            throw new RinaException(e.getMessage(), e);
        }
        isStart = true;
        while (isStart) {
            //TODO 运行的代码
            HttpNIO.ExecuteNio();

        }
    }

    public static void yame() {
        isStart = false;
    }
}
