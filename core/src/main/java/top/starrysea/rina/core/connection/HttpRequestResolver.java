package top.starrysea.rina.core.connection;

import top.starrysea.rina.core.connection.entity.enums.HttpMethod;
import top.starrysea.rina.core.router.RequestInfo;
import top.starrysea.rina.core.router.RinaRequestMapping;
import top.starrysea.rina.core.router.RinaRequestRouteInfo;
import top.starrysea.rina.util.exception.RinaException;
import top.starrysea.rina.util.factory.RinaObjectFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpRequestResolver {

	public Object resolve(HttpMethod httpMethod, String path, Map<String, Object> parameterMap) {
		RequestInfo requestInfo = new RequestInfo(httpMethod, path);
		RinaRequestMapping requestMapping = RinaObjectFactory.getRinaObject(RinaRequestMapping.class);
		RinaRequestRouteInfo routeInfo = requestMapping.getRouteInfo(requestInfo);
		Method controllerMethod = routeInfo.getMethod();
		List<Object> controllerMethodInArgValueList = new ArrayList<>();
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		Class<?>[] controllerMethodInArgClasses = controllerMethod.getParameterTypes();
		try {
			for (Class<?> aClass : controllerMethodInArgClasses) {
				Object controllerMethodInArg = aClass.getConstructor().newInstance();
				parameterMap.forEach((key, value) -> {
					try {
						MethodHandle methodHandle = lookup.findSetter(aClass, key, value.getClass());
						methodHandle.invoke(controllerMethodInArg, value);
					} catch (Throwable e) {
						throw new RinaException(e.getMessage(), e);
					}
				});
				controllerMethodInArgValueList.add(controllerMethodInArg);
			}
			return controllerMethod.invoke(RinaObjectFactory
					.getRinaObject(controllerMethod.getDeclaringClass()), controllerMethodInArgValueList.toArray());
		} catch (Throwable e) {
			throw new RinaException(e.getMessage(), e);
		}
	}
}
