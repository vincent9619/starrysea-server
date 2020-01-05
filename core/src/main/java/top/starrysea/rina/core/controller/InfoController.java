package top.starrysea.rina.core.controller;

import top.starrysea.rina.basic.annotation.RinaController;
import top.starrysea.rina.basic.annotation.RinaGet;
import top.starrysea.rina.basic.annotation.RinaWired;
import top.starrysea.rina.core.dao.MurasameDao;
import top.starrysea.rina.core.service.MUService;
import top.starrysea.rina.init.ServerConfig;
import top.starrysea.rina.util.factory.RinaObjectFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RinaController
public class InfoController {
    @RinaWired
    public MUService muService;

    @RinaGet("/info")
    public Map<String, Object> getInfo() throws SQLException {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("processors", Runtime.getRuntime().availableProcessors());
        infoMap.put("freeMemory", Runtime.getRuntime().freeMemory());
        infoMap.put("totalMemory", Runtime.getRuntime().totalMemory());
        infoMap.put("serverConfig", RinaObjectFactory.getRinaObject(ServerConfig.class));
        infoMap.put("osName", System.getProperty("os.name"));
        infoMap.put("osVersion", System.getProperty("os.version"));
        muService.murasameDao.add();
        return infoMap;
    }
}
