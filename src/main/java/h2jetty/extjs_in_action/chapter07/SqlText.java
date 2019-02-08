package h2jetty.extjs_in_action.chapter07;

import static h2jetty.utils.ClasspathUtils.textFileNextToClass;

public class SqlText {

    public static final String RECREATE_DB = text("recreate-db.sql");

    static String text(String fileName) {
        return textFileNextToClass(SqlText.class, fileName);
    }

}
