package top.starrysea.rina.core.dao;


import lombok.extern.slf4j.Slf4j;
import top.starrysea.rina.basic.annotation.RinaWired;


import top.starrysea.rina.core.kumaOshi.Kumaoshi;
import top.starrysea.rina.jdbc.RinaJdbc;
import top.starrysea.rina.jdbc.RinaQuery;
import top.starrysea.rina.jdbc.enums.WhereType;

import java.sql.SQLException;

@top.starrysea.rina.core.annotation.RinaDao
@Slf4j
public class MurasameDao implements RinaDao {



    @RinaWired
    private RinaJdbc rinaJdbc;


    @Override
    public void add() throws SQLException {
        Kumaoshi kuma = new Kumaoshi();
          rinaJdbc.insert(kuma);
    }

    @Override
    public void delete() throws SQLException {
        Kumaoshi kuma = new Kumaoshi();
        //rinaJdbc.delete(kuma,"cname");
    }

    @Override
    public void rewrite() throws SQLException {
        Kumaoshi kuma = new Kumaoshi();
        //rinaJdbc.update(kuma,"cname");
    }

    @Override
    public void search() throws SQLException {
        Kumaoshi kuma = new Kumaoshi();
        RinaQuery rinaQuery = new RinaQuery(Kumaoshi.class);
        rinaQuery.addWhere("id", WhereType.EQUALS,1);
        rinaJdbc.find(rinaQuery,Kumaoshi.class);
    }
}
