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
		Method method = routeInfo.getMethod();
		List<Object> objects = new ArrayList<>();
		Class<?>[] classes = method.getParameterTypes();
		try {
			for (Class<?> aClass : classes) {
				Object object = aClass.getConstructor().newInstance();
				MethodHandles.Lookup lookup = MethodHandles.lookup();
				parameterMap.forEach((key, value) -> {
					try {
						MethodHandle methodHandle = lookup.findSetter(aClass, key, value.getClass());
						methodHandle.invoke(object, value);
					} catch (Throwable e) {
						throw new RinaException(e.getMessage(), e);
					}
				});
				objects.add(object);
			}
			return method.invoke(RinaObjectFactory.getRinaObject(method.getDeclaringClass()), objects.toArray());
		} catch (Throwable e) {
			throw new RinaException(e.getMessage(), e);
		}
	}
}
