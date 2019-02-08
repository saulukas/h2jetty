package h2jetty.utils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpCycle {

    public final HttpServletRequest request;
    public final HttpServletResponse response;

    public HttpCycle(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public static HttpCycle http(HttpServletRequest request, HttpServletResponse response) {
        return new HttpCycle(request, response);
    }

    public static HttpCycle http(ServletRequest request, ServletResponse response) {
        return new HttpCycle((HttpServletRequest)request, (HttpServletResponse)response);
    }

}
