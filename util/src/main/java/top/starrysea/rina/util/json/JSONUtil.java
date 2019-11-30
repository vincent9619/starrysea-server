package top.starrysea.rina.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import top.starrysea.rina.util.exception.RinaException;

public class JSONUtil {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static String toStr(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RinaException(e.getMessage(), e);
        }
    }

    public static <T> T toObject(String jsonStr, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            throw new RinaException(e.getMessage(), e);
        }
    }

    public static void prettyPrint(Object object) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RinaException(e.getMessage(), e);
        }
    }
}
