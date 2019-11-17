package top.starrysea.rina.util.exception;

public class RinaFileException extends RinaException {

    public RinaFileException(Throwable throwable) {
        super(throwable);
    }

    public RinaFileException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
