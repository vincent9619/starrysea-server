package top.starrysea.rina.core.dao;

import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.basic.annotation.RinaWired;

import top.starrysea.rina.core.kumaOshi.kuma;
import top.starrysea.rina.jdbc.RinaJdbc;

import java.sql.SQLException;
@top.starrysea.rina.core.annotation.RinaDao
@Slf4j
public class MurasameDao implements RinaDao {
    public kuma kumaOshi;

    @RinaWired
    public RinaJdbc rinaJdbc;


  @Override
    public void add() throws SQLException {
   rinaJdbc.insert(kumaOshi);
    }

    @Override
    public void delete() throws SQLException {
        //rinaJdbc.delete(kumaOshi,"cname");
    }

    @Override
    public void rewrite() {

    }
}
