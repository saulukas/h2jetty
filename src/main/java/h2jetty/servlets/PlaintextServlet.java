package h2jetty.servlets;

import java.io.IOException;
import java.nio.charset.Charset;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.MimeTypes;

public class PlaintextServlet extends GenericServlet {

    final byte[] helloWorld = "Hello, World!".getBytes(Charset.forName("ISO-8859-1"));

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.setContentType(MimeTypes.Type.TEXT_PLAIN.asString());
        response.getOutputStream().write(helloWorld);
    }
}
