package top.starrysea.rina.core.task;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import top.starrysea.rina.core.annotation.RinaController;
import top.starrysea.rina.core.annotation.RinaGet;
import top.starrysea.rina.core.annotation.RinaPost;
import top.starrysea.rina.core.router.RequestInfo;
import top.starrysea.rina.core.router.RinaRequestMapping;
import top.starrysea.rina.core.router.RinaRequestRouteInfo;
import top.starrysea.rina.init.ServerConfig;
import top.starrysea.rina.util.exception.RinaException;
import top.starrysea.rina.util.factory.RinaObjectFactory;

import java.util.Set;

public class InitRouter {
	public void execute() {
		Reflections reflections = new Reflections(RinaObjectFactory.getRinaObject(ServerConfig.class).getBasePackage());
		RinaRequestMapping requestMapping = new RinaRequestMapping();
		Set<Class<?>> classes = reflections.getTypesAnnotatedWith(RinaController.class);

		classes.stream().forEach(aClass -> {
			Reflections methodReflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forClass(aClass)).setScanners(new MethodAnnotationsScanner()));
			methodReflections.getMethodsAnnotatedWith(RinaGet.class).stream().forEach(method -> {
				try {
					RinaGet getObject = method.getAnnotation(RinaGet.class);
					RequestInfo requestInfo = new RequestInfo();
					requestInfo.setHttpMethod("get");
					requestInfo.setPath(getObject.value());
					RinaRequestRouteInfo routeInfo = new RinaRequestRouteInfo();
					routeInfo.setMethod(method);
					requestMapping.registerRouteInfo(requestInfo, routeInfo);
				} catch (Exception e) {
					throw new RinaException(e.getMessage(), e);
				}
			});

			methodReflections.getMethodsAnnotatedWith(RinaPost.class).stream().forEach(method -> {
				try {
					RinaPost postObject = method.getAnnotation(RinaPost.class);
					RequestInfo requestInfo = new RequestInfo();
					requestInfo.setHttpMethod("post");
					requestInfo.setPath(postObject.value());
					RinaRequestRouteInfo routeInfo = new RinaRequestRouteInfo();
					routeInfo.setMethod(method);
					requestMapping.registerRouteInfo(requestInfo, routeInfo);
				} catch (Exception e) {
					throw new RinaException(e.getMessage(), e);
				}
			});
		});

		try {
			RinaObjectFactory.putRinaObject(RinaRequestMapping.class, requestMapping);
		} catch (Exception e) {
			throw new RinaException(e.getMessage(), e);
		}
	}
}
