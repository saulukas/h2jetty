package h2jetty.utils;

import java.util.concurrent.Callable;

public class ExceptionUtils {

    public interface Action {
        void execute() throws Exception;
    }

    public interface ExceptionalFunction<T, R> {
        R apply(T t) throws Exception;
    }

    public interface ExceptionalConsumer<T> {
        void consume(T t) throws Exception;
    }

    public static RuntimeException runtimeExceptionOf(Throwable t) {
        if (t instanceof RuntimeException) {
            return (RuntimeException) t;
        }
        return new RuntimeException(t);
    }

    public static <T> T rethrowRuntimeExceptionOf(Callable<T> callable) {
        try {
            return callable.call();
        } catch (RuntimeException rt) {
            throw rt;
        } catch (Error e) {
            throw e;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static void rethrowRuntimeExceptionOf(Action action) {
        rethrowRuntimeExceptionOf(() -> {
            action.execute();
            return null;
        });
    }
}
