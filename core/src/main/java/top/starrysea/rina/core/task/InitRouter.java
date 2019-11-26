package top.starrysea.rina.core.task;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import top.starrysea.rina.core.annotation.RinaController;
import top.starrysea.rina.core.annotation.RinaGet;
import top.starrysea.rina.core.annotation.RinaPost;
import top.starrysea.rina.core.connection.entity.enums.HttpMethod;
import top.starrysea.rina.core.router.RequestInfo;
import top.starrysea.rina.core.router.RinaRequestMapping;
import top.starrysea.rina.core.router.RinaRequestRouteInfo;
import top.starrysea.rina.init.ServerConfig;
import top.starrysea.rina.util.exception.RinaException;
import top.starrysea.rina.util.factory.RinaObjectFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

public class InitRouter {
	private RinaRequestMapping requestMapping = new RinaRequestMapping();

	public void execute() {
		Reflections reflections = new Reflections(RinaObjectFactory.getRinaObject(ServerConfig.class).getBasePackage());
		Set<Class<?>> classes = reflections.getTypesAnnotatedWith(RinaController.class);

		classes.stream().forEach(aClass -> {
			Reflections methodReflections = new Reflections(new ConfigurationBuilder()
					.setUrls(ClasspathHelper.forClass(aClass)).setScanners(new MethodAnnotationsScanner()));
			registerMethod(methodReflections, RinaGet.class);
			registerMethod(methodReflections, RinaPost.class);
		});

		try {
			RinaObjectFactory.putRinaObject(RinaRequestMapping.class, requestMapping);
		} catch (Exception e) {
			throw new RinaException(e.getMessage(), e);
		}
	}

	private void registerMethod(Reflections reflections, Class<? extends Annotation> annotation) {
		reflections.getMethodsAnnotatedWith(annotation).stream().forEach(method -> {
			RequestInfo requestInfo = new RequestInfo();
			RinaRequestRouteInfo routeInfo = new RinaRequestRouteInfo();
			if (annotation == RinaGet.class) {
				RinaGet getObject = method.getAnnotation(RinaGet.class);
				requestInfo.setHttpMethod(HttpMethod.GET);
				requestInfo.setPath(getObject.value());
				routeInfo.setMethod(method);
				requestMapping.registerRouteInfo(requestInfo, routeInfo);
			} else if (annotation == RinaPost.class) {
				RinaPost postObject = method.getAnnotation(RinaPost.class);
				requestInfo.setHttpMethod(HttpMethod.POST);
				requestInfo.setPath(postObject.value());
				routeInfo.setMethod(method);
				requestMapping.registerRouteInfo(requestInfo, routeInfo);
			}
		});
	}
}
