package top.starrysea.rina.core;

import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.core.connection.HttpNIO;
import top.starrysea.rina.util.exception.RinaException;
import top.starrysea.rina.util.factory.RinaObjectFactory;
import top.starrysea.rina.init.InitTaskList;


@Slf4j
public class Rina {

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
        try {
            HttpNIO httpNIO = RinaObjectFactory.getRinaObject(HttpNIO.class);
            httpNIO.executeNio();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
