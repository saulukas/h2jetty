package h2jetty.extjs_in_action.chapter07;

import h2jetty.extjs_in_action.ExtjsStoreRequestParams;
import h2jetty.utils.HttpCycle;
import h2jetty.utils.HttpServlet;

import static h2jetty.extjs_in_action.ExtjsStoreUtils.*;


public class ExtjsChapter7Servlet extends HttpServlet {

    @Override
    public void service(HttpCycle http) throws Exception {

        ExtjsStoreRequestParams storeParams = storeRequestParamsFrom(http.request);
        System.out.println("-- extjs7ch --[" + storeParams + "]");

        addExtjsStoreHeader(http.response);
        writeExtjsStorePayload(storeParams, http.response, extjsStoreSuccess("Nekažką..."));

    }

}
