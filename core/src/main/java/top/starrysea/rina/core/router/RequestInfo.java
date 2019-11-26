package top.starrysea.rina.core.router;

import lombok.Data;
import top.starrysea.rina.core.connection.entity.enums.HttpMethod;

@Data
public class RequestInfo {
	private HttpMethod httpMethod;
	private String path;
}
