package h2jetty.extjs_in_action;

import h2jetty.utils.Dto;
import java.util.List;

public class ExtjsStoreResponse extends Dto {

    public static class Meta extends Dto {
        public boolean success = false;
        public String msg = "";
    }

    public List data; // JSON or XML
    public Meta meta;

}
