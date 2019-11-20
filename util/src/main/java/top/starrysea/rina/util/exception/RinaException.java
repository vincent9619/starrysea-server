package top.starrysea.rina.util.exception;

public class RinaException extends RuntimeException {

    public RinaException(Throwable throwable) {
        super(throwable);
    }

    public RinaException(String message) {
        super(message);
    }

    public RinaException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
