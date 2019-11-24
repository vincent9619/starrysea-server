package top.starrysea.rina.core.router;

import lombok.Data;

@Data
public class RequestInfo {
	private String httpMethod;
	private String path;
}
