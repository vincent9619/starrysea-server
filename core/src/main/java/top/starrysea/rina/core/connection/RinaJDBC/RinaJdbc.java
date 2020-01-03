package top.starrysea.rina.core.connection.RinaJDBC;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@top.starrysea.rina.core.annotation.RinaJdbc
@Slf4j
public class RinaJdbc {


    public static Connection getConn(String username, String password) {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/mysql?serverTimezone=UTC";
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void testJdbc() throws SQLException {
        Connection conn = getConn("root", "jhtq8lha233");  //没有密码就什么都不写，空字符串
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("show tables");//执行查询
        while (rs.next()) {//遍历查询结果
            System.out.println(rs.getString(1));
        }
        stmt.close();//显示关闭Statement对象，释放资源
        conn.close();
    }


}
