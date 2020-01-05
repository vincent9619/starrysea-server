package top.starrysea.rina.core.service;

import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.basic.annotation.RinaWired;

import top.starrysea.rina.core.dao.MurasameDao;

import java.sql.SQLException;


@top.starrysea.rina.basic.annotation.RinaService
@Slf4j
public class MUService {
    @RinaWired
    private MurasameDao murasameDao;

    public void test2() throws SQLException {
        murasameDao.search();
    }
}


