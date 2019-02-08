package h2jetty.utils;

import javax.servlet.Servlet;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import static org.slf4j.LoggerFactory.getLogger;

public class HttpServer {

        private static final org.slf4j.Logger LOG = getLogger(HttpServer.class);


    private final Server server;
    private final ServletContextHandler servletContext;
    public final String contextUrl;

    private boolean isStarted = false;

    public HttpServer(int port, String contextPath) {
        server = new Server(port);
        ServerConnector connector = server.getBean(ServerConnector.class);
        HttpConfiguration config =
            connector.getBean(HttpConnectionFactory.class).getHttpConfiguration();
        config.setSendDateHeader(true);
        config.setSendServerVersion(true);

        servletContext = new ServletContextHandler(ServletContextHandler.GZIP);
        servletContext.setContextPath(contextPath);

        this.contextUrl = "http://localhost:" + port + contextPath;
    }

    public synchronized void addServlet(String path, Servlet servlet) {
        ServletHolder holder = new ServletHolder(servlet);
        servletContext.addServlet(holder, path);
    }

    public synchronized void start() {
        try {
            server.setHandler(servletContext);
            server.start();
            isStarted = true;
        } catch (Exception ex) {
            try {
                server.stop();
                server.join();
            } catch (Exception ex1) {
                LOG.error("Failed to stop server after start-failure: " + ex, ex);
            }
            throw new RuntimeException(ex);
        }
    }

    public synchronized void join() {
        try {
            if (isStarted) {
                server.join();
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        } finally {
            isStarted = false;
        }

    }
}
