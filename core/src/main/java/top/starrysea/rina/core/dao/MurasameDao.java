package top.starrysea.rina.core.dao;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.basic.annotation.RinaWired;


import top.starrysea.rina.core.kumaOshi.kuma;
import top.starrysea.rina.jdbc.RinaJdbc;

import java.sql.SQLException;

@top.starrysea.rina.core.annotation.RinaDao
@Slf4j
public class MurasameDao implements RinaDao {

    public kuma kumaoshi = new kuma();

    @RinaWired
    public RinaJdbc rinaJdbc;


    @Override
    public void add() throws SQLException {
        rinaJdbc.insert(kumaoshi);
    }

    @Override
    public void delete() throws SQLException {
        //rinaJdbc.delete(kumaoshi,"cname");
    }

    @Override
    public void rewrite() throws SQLException {
        //rinaJdbc.update(kumaoshi,"cname");
    }
}
