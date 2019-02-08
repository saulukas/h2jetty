package h2jetty.utils;

import h2jetty.utils.ExceptionUtils.ExceptionalConsumer;
import h2jetty.utils.ExceptionUtils.ExceptionalFunction;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.slf4j.Logger;

import static h2jetty.utils.ExceptionUtils.rethrowRuntimeExceptionOf;
import static h2jetty.utils.ExceptionUtils.runtimeExceptionOf;
import static org.slf4j.LoggerFactory.getLogger;

public class JdbcUtils {

    private static final Logger LOG = getLogger(JdbcUtils.class);

    public static void newTransaction(DataSource dataSource, ExceptionalConsumer<Connection> function) {
        newTransaction(dataSource, (connection) -> {
            function.consume(connection);
            return null;
        });
    }

    public static <T> T newTransaction(DataSource dataSource, ExceptionalFunction<Connection, T> function) {
        Connection connection = rethrowRuntimeExceptionOf(() -> dataSource.getConnection());
        try {
            connection.setAutoCommit(false);
            T result = function.apply(connection);
            try {
                connection.commit();
            } catch (Throwable t) {
                throw new CommitFailedException(t, result);
            }
            return result;
        } catch (CommitFailedException t) {
            throw t;
        } catch (Throwable t) {
            try {
                connection.rollback();
            } catch (Throwable tt) {
                LOG.error("Database rollback failed: " + tt, tt);
            }
            throw runtimeExceptionOf(t);
        } finally {
            close(connection);
        }
    }

    private static void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException ex) {
            LOG.error("Database connection closing failed: " + ex, ex);
        }
    }

    static class CommitFailedException extends RuntimeException {

        public final Object result;

        public CommitFailedException(Throwable cause, Object result) {
            super(cause);
            this.result = result;
        }

    }

}
