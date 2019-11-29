package top.starrysea.rina.core.connection.entity.enums;

public enum HttpStatus {

	OK(200, "OK"),
	BAD_REQUEST(400, "Bad Request"),
	UNAUTHORIZED(401, "Unauthorized"),
	FORBIDDEN(403, "Forbidden"),
	NOT_FOUND(404, "Not Found"),
	I_AM_A_TEAPOT(418, "I'm a teapot"),
	INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
	SERVICE_UNAVAILABLE(503, "Service Unavailable");

	private final int value;
	private final String reason;

	HttpStatus(int value, String reason) {
		this.value = value;
		this.reason = reason;
	}

	@Override
	public String toString() {
		return this.value + " " + this.reason;
	}
}
