package h2jetty.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.h2.tools.Server;

public class H2Database {

    public static void startServer(int port, String dbName) {
        try {
            Server.createTcpServer("-tcp", "-tcpPort", "" + port, "-tcpAllowOthers").start();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String jdbcUrlExternal(int port, String database) {
        return "jdbc:h2:tcp://localhost:" + port + "/mem:" + database;
    }

    public static String jdbcUrlInternal(String database) {
        return "jdbc:h2:mem:" + database;
    }

    public static DataSource createDataSource(String database, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrlInternal(database));
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return new HikariDataSource(config);
    }

}
