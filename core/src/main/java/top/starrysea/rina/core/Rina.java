package top.starrysea.rina.core;

import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.util.exception.RinaException;
import top.starrysea.rina.util.factory.RinaObjectFactory;
import top.starrysea.rina.init.InitTaskList;

import java.io.IOException;


@Slf4j
public class Rina {

    private static boolean isStart = false;

    public static void main(String[] args)  {
        iku();
    }

    public static void iku()  {
        try {
            InitTaskList initTaskList = RinaObjectFactory.generateRinaObject(InitTaskList.class);
            initTaskList.execute();
        } catch (Exception e) {
            throw new RinaException(e.getMessage(), e);
        }
        isStart = true;
        while (isStart) {
            //TODO 运行的代码
            try {
                HttpNIO httpNIO =  RinaObjectFactory.generateRinaObject( HttpNIO.class);
                httpNIO.executeNio();
            }
            catch (Exception e) {
                log.error(e.getMessage(),e);
            }
        }
    }

    public static void yame() {
        isStart = false;
    }
}
