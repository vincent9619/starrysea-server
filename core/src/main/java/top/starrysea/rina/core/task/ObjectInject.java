package top.starrysea.rina.core.task;

import org.reflections.Reflections;
import top.starrysea.rina.core.annotation.RinaObject;
import top.starrysea.rina.core.annotation.RinaWired;
import top.starrysea.rina.init.ServerConfig;
import top.starrysea.rina.util.exception.RinaException;
import top.starrysea.rina.util.factory.RinaObjectFactory;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class ObjectInject {

    private static Set<String> existRinaObjectNameSet = new HashSet<>();

    // 为 RinaObject 注入
    public void execute() {
        Reflections reflections = new Reflections(RinaObjectFactory.getRinaObject(ServerConfig.class).getBasePackage());
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(RinaObject.class);
        classes.stream().forEach(this::inject);
    }

    private <T> T inject(Class<T> clazz) {
        if (existRinaObjectNameSet.contains(clazz.getName())) {
            throw new RinaException(clazz.getName() + "类有循环依赖!");
        }
        existRinaObjectNameSet.add(clazz.getName());
        T result = RinaObjectFactory.getRinaObject(clazz);
        if (result != null) {
            existRinaObjectNameSet.remove(clazz.getName());
            return result;
        }
        try {
            result = clazz.getConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                RinaWired wired = field.getAnnotation(RinaWired.class);
                if (wired != null) {
                    // 递归注入
                    Object object = inject(field.getType());
                    // 针对 private
                    if (!field.canAccess(result))
                        field.setAccessible(true);
                    field.set(result, object);
                }
            }
        } catch (Exception e) {
            throw new RinaException(e.getMessage(), e);
        }
        RinaObjectFactory.putRinaObject(clazz, result);
        existRinaObjectNameSet.remove(clazz.getName());
        return result;
    }
}
