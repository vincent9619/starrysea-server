package top.starrysea.rina.core.dao;

import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.core.annotation.RinaDao;
import top.starrysea.rina.core.annotation.RinaWired;
import top.starrysea.rina.core.connection.RinaJDBC.RinaJdbc;

import java.lang.annotation.Annotation;

@Slf4j
@RinaDao
public class MurasameDao {
    @RinaWired
 public RinaJdbc rinaJdbc;


}

