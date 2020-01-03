package top.starrysea.rina.core.router;

import lombok.Data;

import java.lang.reflect.Method;

@Data
public class RinaRequestRouteInfo {
	private Method method;
}
