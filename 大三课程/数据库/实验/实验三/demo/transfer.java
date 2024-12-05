import java.sql.*;
public class transfer {
    //opengauss端连接，按照你的信息修改相关值
    static final String opengauss_DRIVER = "org.postgresql.Driver";
    static final String opengauss_URL = "jdbc:postgresql://127.0.0.1:5432/opengauss_database";
    static final String opengauss_user = "gaussdb";
    static final String opengauss_pwd = "Enmo@123";

    //mysql端连接，按照你的信息修改相关值
    static final String databaseName = "test";
    //本机mysql
    static final String mysql_URL = "jdbc:mysql://localhost:3306/test";
    static final String mysql_user = "members";
    static final String mysql_pwd = "helloworld";
    static final String mysql_DRIVER = "com.mysql.jdbc.Driver";


    public static void main(String[]args)throws Exception {
        Connection opengauss_conn = null;
        Statement opengauss_stmt = null;
        Connection mysql_conn = null;
        Statement mysql_stmt = null;
        try {
            //opengauss侧
            Class.forName(opengauss_DRIVER);
            System.out.println("连接opengauss数据库中..");
            opengauss_conn = DriverManager.getConnection(opengauss_URL, opengauss_user, opengauss_pwd);
            opengauss_stmt=opengauss_conn.createStatement();
            //mysql侧
            Class.forName(mysql_DRIVER);
            System.out.println("连接mysql数据库中");
            mysql_conn = DriverManager.getConnection(mysql_URL, mysql_user, mysql_pwd);
            mysql_stmt = mysql_conn.createStatement();

            //迁移
            System.out.println("函数迁移进行中...");

            //从mysql.proc和 information_schema表中获取函数定义
            String Msql = "SELECT A.NAME, A.param_list, B.data_type, B.routine_definition FROM mysql.proc A JOIN information_schema.ROUTINES B ON A.NAME = B.specific_name WHERE A.type = 'FUNCTION' and A.db=\'"+databaseName+"\';"; //改进语法
            ResultSet Mrs = mysql_stmt.executeQuery(Msql);

            while (Mrs.next()) {
                String name = Mrs.getString("NAME");
                String param = Mrs.getString("param_list");

                String body = Mrs.getString("routine_definition");
                String returns=Mrs.getString("data_type");

                //封装，opengauss端复用
                String Osql="CREATE OR REPLACE FUNCTION %s(%s) returns %s as $$ %s; $$LANGUAGE plpgsql;";
                //或者用String Osql="CREATE OR REPLACE FUNCTION %s(%s) return %s AS %s\n /";  因为在opengauss中创建函数有两种语法
                Osql=String.format(Osql,name,param,returns,body);
                opengauss_stmt.execute(Osql);
                System.out.println(Osql); //检验
                System.out.println("函数迁移完成！");
            }
            mysql_stmt.close();
            mysql_conn.close();
            opengauss_stmt.close();
            opengauss_conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (mysql_stmt != null) mysql_stmt.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
        try {
            if (mysql_conn != null) mysql_conn.close();
        } catch (SQLException e3) {
            e3.printStackTrace();
        }
        System.out.println("goodbye");
    }
}
