package top.starrysea.rina.util.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RinaObjectFactory {

    private static Map<Class<?>, Object> objectMap;

    static {
        objectMap = new ConcurrentHashMap<>();
    }

    public <T> T getRinaObject(Class<T> key) {
        return key.cast(objectMap.get(key));
    }

    public <T> void putRinaObject(Class<T> key, T value) {
        objectMap.put(key, value);
    }
}
