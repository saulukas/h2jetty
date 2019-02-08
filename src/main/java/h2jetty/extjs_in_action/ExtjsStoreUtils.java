package h2jetty.extjs_in_action;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static h2jetty.utils.JsonUtils.jsonOf;
import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

public class ExtjsStoreUtils {

    public static ExtjsStoreRequestParams storeRequestParamsFrom(HttpServletRequest req) {

        ExtjsStoreRequestParams params = new ExtjsStoreRequestParams();

        params.model = req.getParameter("model");
        params.page = parseInt(firstNonNull(req.getParameter("page"), "1"));
        params.limit = parseInt(firstNonNull(req.getParameter("limit"), "25"));
        params.callback = req.getParameter("callback");

        return params;
    }

    public static ExtjsStoreResponse extjsStoreFailure(List data) {
        ExtjsStoreResponse resp = new ExtjsStoreResponse();
        resp.data = data;
        resp.meta = new ExtjsStoreResponse.Meta();
        resp.meta.success = false;
        resp.meta.msg = "";
        return resp;
    }

    public static ExtjsStoreResponse extjsStoreSuccess(String msg) {
        ExtjsStoreResponse resp = new ExtjsStoreResponse();
        resp.data = Collections.EMPTY_LIST;
        resp.meta = new ExtjsStoreResponse.Meta();
        resp.meta.success = false;
        resp.meta.msg = msg;
        return resp;
    }

    public static void addExtjsStoreHeader(HttpServletResponse resp) {
        resp.addHeader("Content-type", "application/json;charset=UTF-8");
    }

    public static void writeExtjsStorePayload(
        ExtjsStoreRequestParams params,
        HttpServletResponse resp,
        ExtjsStoreResponse payload
    ) throws IOException {

        if (params.callback != null) {
            resp.getWriter().write(params.callback + "(");
            resp.getWriter().write(jsonOf(payload));
            resp.getWriter().write(")");
        } else {
            resp.getWriter().write(jsonOf(payload));
        }
    }

}
