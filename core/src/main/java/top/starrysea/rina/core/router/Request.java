package top.starrysea.rina.core.router;

import lombok.Data;

@Data
public class Request {
	private String method;
	private String path;
}
