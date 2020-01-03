package top.starrysea.rina.core.router;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.starrysea.rina.core.connection.entity.enums.HttpMethod;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestInfo {
	private HttpMethod httpMethod;
	private String path;
}
