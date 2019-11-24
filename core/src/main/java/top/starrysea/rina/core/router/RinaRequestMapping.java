package top.starrysea.rina.core.router;

import lombok.Data;

import java.util.Map;

@Data
public class RinaRequestMapping {
	private Map<Request, RinaRequestRouteInfo> routeInfoMap;
}
