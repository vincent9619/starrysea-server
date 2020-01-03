package top.starrysea.rina.util.exception;

public class RinaFileException extends RinaException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7567031532459379736L;

	public RinaFileException(Throwable throwable) {
        super(throwable);
    }

    public RinaFileException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
