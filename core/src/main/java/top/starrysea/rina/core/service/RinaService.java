package top.starrysea.rina.core.service;

import top.starrysea.rina.core.annotation.RinaWired;
import top.starrysea.rina.core.dao.MurasameDao;

@top.starrysea.rina.core.annotation.RinaService
public class RinaService {
  @RinaWired
  private MurasameDao murasameDao;

}




