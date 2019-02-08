package h2jetty.servlets;

import java.io.IOException;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class AggregatingServlet extends GenericServlet {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher("/json").include(req, res);
        req.getRequestDispatcher("/json").include(req, res);
        req.getRequestDispatcher("/json").include(req, res);
        req.getRequestDispatcher("/json").include(req, res);
//        req.getRequestDispatcher("/plaintext").include(req, res);
    }
}
