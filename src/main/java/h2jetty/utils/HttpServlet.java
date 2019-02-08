package h2jetty.utils;

import java.io.IOException;
import javax.servlet.*;

import static h2jetty.utils.HttpCycle.http;

public abstract class HttpServlet extends GenericServlet {


    public abstract void service(HttpCycle http) throws Exception;



    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        try {
            service(http(req, res));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
