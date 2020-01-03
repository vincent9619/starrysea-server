package top.starrysea.rina.core.router;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RinaRequestMapping {
	private Map<RequestInfo, RinaRequestRouteInfo> routeInfoMap = new HashMap<>();

	public void registerRouteInfo(RequestInfo requestInfo, RinaRequestRouteInfo routeInfo){
		routeInfoMap.put(requestInfo, routeInfo);
	}

	public RinaRequestRouteInfo getRouteInfo(RequestInfo requestInfo){
		return routeInfoMap.get(requestInfo);
	}
}
