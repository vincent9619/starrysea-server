package top.starrysea.rina.core.task;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import top.starrysea.rina.core.annotation.RinaController;
import top.starrysea.rina.core.annotation.RinaGet;
import top.starrysea.rina.core.annotation.RinaPost;
import top.starrysea.rina.core.router.Request;
import top.starrysea.rina.core.router.RinaRequestMapping;
import top.starrysea.rina.core.router.RinaRequestRouteInfo;
import top.starrysea.rina.init.ServerConfig;
import top.starrysea.rina.util.exception.RinaException;
import top.starrysea.rina.util.factory.RinaObjectFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InitRouter {
	public void execute() {
		Reflections reflections = new Reflections(RinaObjectFactory.getRinaObject(ServerConfig.class).getBasePackage());
		Map<Request, RinaRequestRouteInfo> routeMap = new HashMap<>();
		Set<Class<?>> classes = reflections.getTypesAnnotatedWith(RinaController.class);

		classes.stream().forEach(aClass -> {
			Reflections methodReflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forClass(aClass)).setScanners(new MethodAnnotationsScanner()));
			methodReflections.getMethodsAnnotatedWith(RinaGet.class).stream().forEach(method -> {
				try {
					RinaGet getObject = method.getAnnotation(RinaGet.class);
					Request request = RinaObjectFactory.generateRinaObject(Request.class);
					request.setMethod("get");
					request.setPath(getObject.value());
					RinaRequestRouteInfo routeInfo = RinaObjectFactory.generateRinaObject(RinaRequestRouteInfo.class);
					routeInfo.setMethod(method);
					routeMap.put(request, routeInfo);
				} catch (Exception e) {
					throw new RinaException(e.getMessage(), e);
				}
			});

			methodReflections.getMethodsAnnotatedWith(RinaPost.class).stream().forEach(method -> {
				try {
					RinaPost getObject = method.getAnnotation(RinaPost.class);
					Request request = RinaObjectFactory.generateRinaObject(Request.class);
					request.setMethod("post");
					request.setPath(getObject.value());
					RinaRequestRouteInfo routeInfo = RinaObjectFactory.generateRinaObject(RinaRequestRouteInfo.class);
					routeInfo.setMethod(method);
					routeMap.put(request, routeInfo);
				} catch (Exception e) {
					throw new RinaException(e.getMessage(), e);
				}
			});
		});

		try {
			RinaRequestMapping requestMapping = RinaObjectFactory.generateRinaObject(RinaRequestMapping.class);
			requestMapping.setRouteInfoMap(routeMap);
			RinaObjectFactory.putRinaObject(RinaRequestMapping.class, requestMapping);
		} catch (Exception e) {
			throw new RinaException(e.getMessage(), e);
		}
	}
}
