package top.starrysea.rina.core.dao;

import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.core.annotation.RinaDao;
import top.starrysea.rina.core.annotation.RinaWired;

import java.lang.annotation.Annotation;

@Slf4j
@RinaDao
public class MurasameDao {


    public void test1() {

        log.info("123");
    }
}

