package h2jetty;

import h2jetty.extjs_in_action.chapter07.ExtjsChapter7Servlet;
import h2jetty.extjs_in_action.chapter07.SqlText;
import h2jetty.servlets.*;
import h2jetty.utils.H2Database;
import h2jetty.utils.HttpServer;
import java.lang.management.ManagementFactory;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;

import static h2jetty.utils.JdbcUtils.newTransaction;
import static java.lang.System.currentTimeMillis;

public class H2JettyServer {

    public static void main(String[] args) throws Exception {

        System.out.println("....... jvm up-time ...............: "
            + ManagementFactory.getRuntimeMXBean().getUptime() + " ms");

        int dbPort = 7701;
        String dbName = "somedb";

     //   H2Database.startServer(dbPort, dbName);

        long createDbStartMillis = currentTimeMillis();
        System.out.println("....... creating h2 database ....... ");
        DataSource dataSource = H2Database.createDataSource(dbName, "extjs7", "extjs7");
        System.out.println("JDBC URL: " + H2Database.jdbcUrlExternal(dbPort, dbName));

        newTransaction(dataSource, (connection) -> {
            try (Statement statement = connection.createStatement()) {
                statement.execute("select 1 from dual;");
                try (ResultSet resultSet = statement.getResultSet()) {
                    resultSet.next();
                    System.out.println("result is = " + resultSet.getObject(1));
                }
            }
        });

        newTransaction(dataSource, (connection) -> {
            try (Statement statement = connection.createStatement()) {
                statement.execute(SqlText.RECREATE_DB);
            }
        });
        System.out.println("....... creating h2 database ....... OK"
            + " (" + (currentTimeMillis() - createDbStartMillis) + " ms)");

        long jettyStartMillis = currentTimeMillis();
        System.out.println("....... starting jetty ............. ");
        int httpPort = 7887;
        String contextPath = "/h2jetty";

        HttpServer httpServer = new HttpServer(httpPort, contextPath);

        String h2consolePath = "/h2console";
        httpServer.addServlet("/json", /* ................ */ new JsonServlet());
        httpServer.addServlet("/plaintext", /* ........... */ new PlaintextServlet());
        httpServer.addServlet("/a", /* ................... */ new AggregatingServlet());
        httpServer.addServlet("/hello/*", /* ............. */ new HelloServlet());
        httpServer.addServlet("/extjs-ch7/*", /* ......... */ new ExtjsChapter7Servlet());
        httpServer.addServlet(h2consolePath + "/*", /* ... */ new org.h2.server.web.WebServlet());
        System.out.println("H2 Console: " + httpServer.contextUrl + h2consolePath);

        httpServer.start();
        System.out.println("....... starting jetty ............. OK"
            + " (" + (currentTimeMillis() - jettyStartMillis) + " ms)");

        System.out.println(httpServer.contextUrl);
        System.out.println("....... jvm up-time ...............: "
            + ManagementFactory.getRuntimeMXBean().getUptime() + " ms");

        httpServer.join();
    }
}
