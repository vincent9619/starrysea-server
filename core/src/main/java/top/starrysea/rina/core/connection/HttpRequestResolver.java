package top.starrysea.rina.core.connection;

import top.starrysea.rina.core.connection.entity.HttpContent;
import top.starrysea.rina.core.router.RequestInfo;
import top.starrysea.rina.core.router.RinaRequestMapping;
import top.starrysea.rina.core.router.RinaRequestRouteInfo;
import top.starrysea.rina.util.exception.RinaException;
import top.starrysea.rina.util.factory.RinaObjectFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpRequestResolver {

	public Object resolve(HttpContent httpContent) {
		RequestInfo requestInfo = new RequestInfo(httpContent.getHttpMethod(), httpContent.getPath());
		Map<String, String> parameterMap = httpContent.getFormData();
		RinaRequestMapping requestMapping = RinaObjectFactory.getRinaObject(RinaRequestMapping.class);
		RinaRequestRouteInfo routeInfo = requestMapping.getRouteInfo(requestInfo);
		Method controllerMethod = routeInfo.getMethod();
		List<Object> controllerMethodInArgValueList = new ArrayList<>();
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		Class<?>[] controllerMethodInArgClasses = controllerMethod.getParameterTypes();
		try {
			for (Class<?> aClass : controllerMethodInArgClasses) {
				Object controllerMethodInArg = aClass.getConstructor().newInstance();
				Field[] argField = aClass.getDeclaredFields();
				for (Field field : argField) {
					String key = field.getName();
					String value = parameterMap.get(key);
					if (value != null) {
						MethodHandle methodHandle = lookup.findSetter(aClass, key, field.getType());
						methodHandle.invoke(controllerMethodInArg, field.getType().cast(value));
					}
				}
				controllerMethodInArgValueList.add(controllerMethodInArg);
			}
			return controllerMethod.invoke(RinaObjectFactory
					.getRinaObject(controllerMethod.getDeclaringClass()), controllerMethodInArgValueList.toArray());
		} catch (Throwable e) {
			throw new RinaException(e.getMessage(), e);
		}
	}
}
