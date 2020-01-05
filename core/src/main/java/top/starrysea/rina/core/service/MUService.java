package top.starrysea.rina.core.service;

import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.basic.annotation.RinaWired;

import top.starrysea.rina.core.dao.MurasameDao;


@top.starrysea.rina.basic.annotation.RinaService
@Slf4j
public class MUService {
    @RinaWired
    public MurasameDao murasameDao;
}


