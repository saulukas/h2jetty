package h2jetty.utils;

import com.google.common.io.CharStreams;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

public class ClasspathUtils {

    public static String resourceToString(Class<?> klass, String resourcePath) {
        try {
            Reader reader = resourceReader(klass, resourcePath);
            return CharStreams.toString(reader);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load classpath resource " + resourcePath + ": " + ex, ex);
        }
    }

    public static String resourceToString(String resourcePath) {
        return resourceToString(ClasspathUtils.class, resourcePath);
    }

    public static String resourcePathNextToClass(Class<?> klass, String fileName) {
        String path = "/" + klass.getPackage().getName().replace(".", "/") + "/" + fileName;
        int byteCodeManipulatorPostfix = path.indexOf("$$");
        if (byteCodeManipulatorPostfix > 0) {
            path = path.substring(0, byteCodeManipulatorPostfix);
        }
        return path;
    }

    public static String textFileNextToClass(Class<?> klass, String fileName) {
        String resourcePath = resourcePathNextToClass(klass, fileName);
        return resourceToString(klass, resourcePath);
    }

    private static Reader resourceReader(Class<?> klass, String resourcePath) throws UnsupportedEncodingException {
        InputStream inputStream = klass.getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new RuntimeException("Resource path not found: " + resourcePath);
        }
        return new InputStreamReader(inputStream, "UTF-8");
    }

}
