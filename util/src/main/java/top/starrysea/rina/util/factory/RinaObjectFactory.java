package top.starrysea.rina.util.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RinaObjectFactory {

    private static Map<Class<?>, Object> objectMap;

    static {
        objectMap = new ConcurrentHashMap<>();
    }

    public static <T> T getRinaObject(Class<T> key) {
        return key.cast(objectMap.get(key));
    }

    public static <T> void putRinaObject(Class<T> key, T value) {
        objectMap.put(key, value);
    }

    public static <T> T generateRinaObject(Class<T> key) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Object object = objectMap.get(key);
        if (object == null) {
            T t = key.getConstructor().newInstance();
            putRinaObject(key, t);
        }
        return getRinaObject(key);
    }
}
