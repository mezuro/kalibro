package org.kalibro.dto;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.kalibro.core.reflection.MethodReflector;
import org.kalibro.dao.DaoFactory;

/**
 * Creates lazy load proxies that call {@link DaoFactory} methods to load the target.
 * 
 * @author Carlos Morais
 */
public final class DaoLazyLoader implements MethodInterceptor {

	public static <T> T createProxy(Class<?> daoClass, String methodName, Object... arguments) {
		DaoLazyLoader loader = new DaoLazyLoader(daoClass, methodName, arguments);
		T proxy = (T) Enhancer.create(loader.getTargetClass(), loader);
		loader.start();
		return proxy;
	}

	private boolean started;
	private Object target;

	private MethodReflector reflector;
	private String methodName;
	private Object[] loadArguments;

	private DaoLazyLoader(Class<?> daoClass, String methodName, Object[] loadArguments) {
		this.methodName = methodName;
		this.loadArguments = loadArguments;
		reflector = new MethodReflector(daoClass);
	}

	private Class<?> getTargetClass() {
		return reflector.getReturnType(methodName, loadArguments);
	}

	private void start() {
		started = true;
	}

	@Override
	public Object intercept(Object object, Method method, Object[] arguments, MethodProxy proxy) throws Throwable {
		if (! started || method.getName().equals("finalize"))
			return null;
		if (target == null)
			load();
		method.setAccessible(true);
		return method.invoke(target, arguments);
	}

	private void load() {
		Object dao = new MethodReflector(DaoFactory.class).invoke("get" + reflector.getClassName());
		target = reflector.invoke(dao, methodName, loadArguments);
	}
}