package top.starrysea.rina.core.connection.entity.enums;

public enum HttpContentType {

	APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
	TEXT_HTML("text/html"),
	TEXT_PLAIN("text/plain"),
	APPLICATION_JSON("application/json");

	private String type;

	private HttpContentType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}


