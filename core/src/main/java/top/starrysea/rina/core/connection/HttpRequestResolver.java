package top.starrysea.rina.core.connection;

import top.starrysea.rina.basic.annotation.RinaBody;
import top.starrysea.rina.core.connection.entity.HttpContent;
import top.starrysea.rina.core.connection.entity.enums.HttpContentType;
import top.starrysea.rina.core.connection.entity.enums.HttpMethod;
import top.starrysea.rina.core.connection.entity.enums.HttpStatus;
import top.starrysea.rina.core.router.RequestInfo;
import top.starrysea.rina.core.router.RinaRequestMapping;
import top.starrysea.rina.core.router.RinaRequestRouteInfo;
import top.starrysea.rina.util.exception.RinaException;
import top.starrysea.rina.util.factory.RinaObjectFactory;
import top.starrysea.rina.util.json.JSONUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpRequestResolver {

    public static HttpResponse resolve(HttpContent httpContent) {
        RequestInfo requestInfo = new RequestInfo(httpContent.getHttpMethod(), httpContent.getPath());
        Map<String, String> parameterMap = httpContent.getFormData();
        RinaRequestMapping requestMapping = RinaObjectFactory.getRinaObject(RinaRequestMapping.class);
        RinaRequestRouteInfo routeInfo = requestMapping.getRouteInfo(requestInfo);
        if (routeInfo == null) {
            routeInfo = requestMapping.getRouteInfo(new RequestInfo(HttpMethod.GET, "/error/404"));
        }
        Method controllerMethod = routeInfo.getMethod();
        List<Object> controllerMethodInArgValueList = new ArrayList<>();
        Class<?>[] controllerMethodInArgClasses = controllerMethod.getParameterTypes();
        Annotation[][] annotations = controllerMethod.getParameterAnnotations();
        try {
            if (annotations != null && annotations.length != 0) {
                int classIndex = 0;
                boolean isAnnotationUsed = false;
                for (Annotation[] annotationsPerMethod : annotations) {
                    for (Annotation annotationOfOneMethod : annotationsPerMethod) {
                        if (annotationOfOneMethod instanceof RinaBody) {
                            if (isAnnotationUsed) {
                                throw new RinaException("每个方法参数中不能出现多于1个的 RinaBody 注解");
                            }
                            Object controllerMethodInArg = JSONUtil.toObject(parameterMap.get("jsonObject"), controllerMethodInArgClasses[classIndex]);
                            controllerMethodInArgValueList.add(controllerMethodInArg);
                            isAnnotationUsed = true;
                            controllerMethodInArgClasses[classIndex] = null;
                        }
                    }
                    classIndex++;
                }
            }
            for (Class<?> aClass : controllerMethodInArgClasses) {
                if (aClass == null) {
                    continue;
                }
                Object controllerMethodInArg = aClass.getConstructor().newInstance();
                Field[] argField = aClass.getDeclaredFields();
                for (Field field : argField) {
                    String key = field.getName();
                    String value = parameterMap.get(key);
                    if (value != null) {
                        field.setAccessible(true);
                        if (field.getType() == int.class) {
                            field.set(controllerMethodInArg, Integer.parseInt(value));
                        } else if (field.getType() == short.class) {
                            field.set(controllerMethodInArg, Short.parseShort(value));
                        } else if (field.getType() == long.class) {
                            field.set(controllerMethodInArg, Long.parseLong(value));
                        } else if (field.getType() == String.class) {
                            field.set(controllerMethodInArg, value);
                        }
                    }
                }
                controllerMethodInArgValueList.add(controllerMethodInArg);
            }
            Object body = controllerMethod.invoke(RinaObjectFactory
                    .getRinaObject(controllerMethod.getDeclaringClass()), controllerMethodInArgValueList.toArray());
            return generateResponse(body);
        } catch (Throwable e) {
            throw new RinaException(e.getMessage(), e);
        }
    }

    private static HttpResponse generateResponse(Object body) {
        HttpResponse response = new HttpResponse();
        response.setHttpStatus(HttpStatus.OK);
        if (body.getClass() == String.class) {
            if (body.toString().trim().substring(0, 15).toLowerCase().contains("<!doctype html>")) {
                response.setHttpContentType(HttpContentType.TEXT_HTML);
            } else {
                response.setHttpContentType(HttpContentType.TEXT_PLAIN);
            }
            response.setResponseContent(body.toString());
        } else {
            response.setHttpContentType(HttpContentType.APPLICATION_JSON);
            response.setResponseContent(JSONUtil.toStr(body));
        }
        return response;
    }
}
