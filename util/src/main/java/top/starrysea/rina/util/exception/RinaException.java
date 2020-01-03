package top.starrysea.rina.util.exception;

public class RinaException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -219898481774689911L;

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
