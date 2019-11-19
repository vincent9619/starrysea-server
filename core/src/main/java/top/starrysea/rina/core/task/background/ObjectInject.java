package top.starrysea.rina.core.task.background;

import org.reflections.Reflections;
import top.starrysea.rina.core.annotation.RinaObject;
import top.starrysea.rina.core.annotation.RinaWired;
import top.starrysea.rina.util.exception.RinaException;
import top.starrysea.rina.util.factory.RinaObjectFactory;

import java.lang.reflect.Field;
import java.util.Set;

public class ObjectInject implements BackgroundTaskInterface {
	// 为 RinaObject 注入

	@Override
	public void execute() {
		Reflections reflections = new Reflections("top.starrysea.rina");
		Set<Class<?>> classes = reflections.getTypesAnnotatedWith(RinaObject.class);
		classes.stream().forEach(this::inject);
	}

	private <T> T inject(Class<T> clazz) {
		T result = RinaObjectFactory.getRinaObject(clazz);
		if (result != null)
			return result;
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
		return result;
	}
}
