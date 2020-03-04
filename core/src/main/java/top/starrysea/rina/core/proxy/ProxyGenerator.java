package top.starrysea.rina.core.proxy;

import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProxyGenerator {
	private Method beforeMethod;
	private Method afterMethod;
	private Object beforeObject;
	private Object afterObject;
	private Object[] beforeArgs;
	private Object[] afterArgs;

	public void setBefore(Method method, Object o, Object... args) {
		this.beforeMethod = method;
		this.beforeObject = o;
		this.beforeArgs = args;
	}

	public void setAfter(Method method, Object o, Object... args) {
		this.afterMethod = method;
		this.afterObject = o;
		this.afterArgs = args;
	}

	public void clearBefore() {
		this.beforeArgs = null;
		this.beforeMethod = null;
		this.beforeObject = null;
	}

	public void clearAfter() {
		this.afterArgs = null;
		this.afterMethod = null;
		this.afterObject = null;
	}

	public <T> T getProxy(Class<T> objectClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		ProxyFactory factory = new ProxyFactory();
		factory.setSuperclass(objectClass);
		factory.setFilter(method -> !method.getName().equals("finalize"));
		Object object = factory.createClass().getConstructor().newInstance();
		((Proxy) object).setHandler((o, method, method1, objects) -> {
			if (beforeMethod != null) {
				beforeMethod.invoke(beforeObject, beforeArgs);
			}
			Object result = method1.invoke(o, objects);
			if (afterMethod != null) {
				afterMethod.invoke(afterObject, afterArgs);
			}
			return result;
		});
		return objectClass.cast(object);
	}
}
