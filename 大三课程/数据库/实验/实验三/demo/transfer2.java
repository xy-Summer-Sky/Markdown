import java.sql.*;

public class transfer2 {
    // OpenGauss connection details
    static final String opengauss_DRIVER = "org.postgresql.Driver";
    static final String opengauss_URL = "jdbc:postgresql://127.0.0.1:5432/opengauss_database";
    static final String opengauss_user = "gaussdb";
    static final String opengauss_pwd = "Enmo@123";

    // MySQL connection details
    static final String databaseName = "test";
    static final String mysql_URL = "jdbc:mysql://localhost:3306/test";
    static final String mysql_user = "members";
    static final String mysql_pwd = "helloworld";
    static final String mysql_DRIVER = "com.mysql.cj.jdbc.Driver";

    public static void main(String[] args) throws Exception {
        Connection opengauss_conn = null;
        Statement opengauss_stmt = null;
        Connection mysql_conn = null;
        Statement mysql_stmt = null;
        try {
            // OpenGauss connection
            Class.forName(opengauss_DRIVER);
            System.out.println("Connecting to OpenGauss database...");
            opengauss_conn = DriverManager.getConnection(opengauss_URL, opengauss_user, opengauss_pwd);
            opengauss_stmt = opengauss_conn.createStatement();

            // MySQL connection
            Class.forName(mysql_DRIVER);
            System.out.println("Connecting to MySQL database...");
            mysql_conn = DriverManager.getConnection(mysql_URL, mysql_user, mysql_pwd);
            mysql_stmt = mysql_conn.createStatement();

            // 视图迁移
           String viewSql = "SELECT TABLE_NAME AS NAME, REPLACE(VIEW_DEFINITION, 'test.', '') AS definition FROM information_schema.VIEWS WHERE TABLE_SCHEMA=\'" + databaseName + "\';";
            ResultSet viewRs = mysql_stmt.executeQuery(viewSql);

            while (viewRs.next()) {
                String viewName = viewRs.getString("NAME");
                String viewDefinition = viewRs.getString("definition");

                // 在 openGauss 创建视图
                String createViewSql = String.format("CREATE OR REPLACE VIEW %s AS %s;", viewName, viewDefinition);
                opengauss_stmt.execute(createViewSql);
                System.out.println("视图 " + viewName + " 迁移完成！");
            }


            // 存储过程迁移
            // 存储过程迁移
            String procedureSql = "SELECT r.SPECIFIC_NAME AS ROUTINE_NAME, r.ROUTINE_DEFINITION FROM INFORMATION_SCHEMA.ROUTINES r WHERE r.ROUTINE_SCHEMA = '" + databaseName + "' AND r.ROUTINE_TYPE = 'PROCEDURE' ORDER BY r.SPECIFIC_NAME;";
            ResultSet procedureRs = mysql_stmt.executeQuery(procedureSql);

            while (procedureRs.next()) {
                String procName = procedureRs.getString("ROUTINE_NAME");
                String procBody = procedureRs.getString("ROUTINE_DEFINITION");

                // 在 openGauss 中创建存储过程，假设所有存储过程均不返回值（如有返回值，需另行处理）
                // 确保 procBody 中的 SQL 语句是正确的，并且使用 $$ 包裹 SQL 命令块
                String createProcedureSql = String.format("CREATE OR REPLACE PROCEDURE %s() LANGUAGE plpgsql AS $$ BEGIN %s END; $$", procName, procBody);
                System.out.println("Creating procedure with SQL: " + createProcedureSql);
                opengauss_stmt.execute(createProcedureSql); // 执行创建存储过程的 SQL
                System.out.println("存储过程 " + procName + " 迁移完成！");
            }




            // 触发器迁移
            String triggerSql = "SELECT `TRIGGER_NAME` AS `NAME`, `EVENT_MANIPULATION` AS `event`, `EVENT_OBJECT_TABLE` AS `table_name`, `ACTION_STATEMENT` AS `definition` FROM `information_schema`.`TRIGGERS` WHERE `TRIGGER_SCHEMA`='" + databaseName + "';";
            ResultSet triggerRs = mysql_stmt.executeQuery(triggerSql);

            while (triggerRs.next()) {
                String triggerName = triggerRs.getString("NAME");
                String tableName = triggerRs.getString("table_name");
                String triggerEvent = triggerRs.getString("event");
                String actionStatement = triggerRs.getString("definition");

                // 构建触发器 SQL 语句
                String createTriggerSql = "CREATE TRIGGER " + triggerName + " AFTER " + triggerEvent + " ON " + tableName + " FOR EACH ROW " + actionStatement + ";";
                opengauss_stmt.execute(createTriggerSql);
                System.out.println("触发器 " + triggerName + " 迁移完成！");
            }


            // Close connections
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
        System.out.println("Goodbye");
    }
}