package h2jetty.servlets;

import h2jetty.utils.HttpCycle;
import h2jetty.utils.HttpServlet;

import static com.google.common.base.MoreObjects.firstNonNull;

public class HelloServlet extends HttpServlet {

    @Override
    public void service(HttpCycle http) throws Exception {
        String name = firstNonNull(http.request.getParameter("name"), "Tomas");

        http.response.setContentType("text/plain;charset=utf-8");
        http.response.getWriter().write("Nice to see you again, " + name + "!\n");
        String pathAndQuery = firstNonNull(http.request.getPathInfo(), "");
        if (http.request.getQueryString() != null) {
            pathAndQuery += "?" + http.request.getQueryString();
        }
        http.response.getWriter().write("Servlet URL path & query [" + pathAndQuery + "]");
    }

}
