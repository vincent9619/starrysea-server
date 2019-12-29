package top.starrysea.rina.core.service;

import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.core.annotation.RinaWired;
import top.starrysea.rina.core.dao.MurasameDao;

@top.starrysea.rina.core.annotation.RinaService
@Slf4j
public class MUService {
    @RinaWired
    private MurasameDao murasameDao;

    public void test() {
        murasameDao.test1();
    }
}




