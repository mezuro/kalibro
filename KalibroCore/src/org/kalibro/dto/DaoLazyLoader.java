package org.kalibro.dto;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.LazyLoader;

import org.kalibro.core.reflection.MethodReflector;
import org.kalibro.dao.DaoFactory;

/**
 * Creates lazy load proxies that call {@link DaoFactory} methods to load the target.
 * 
 * @author Carlos Morais
 */
public final class DaoLazyLoader implements LazyLoader {

	public static <T> T createProxy(Class<?> daoClass, String methodName, Object... arguments) {
		DaoLazyLoader loader = new DaoLazyLoader(daoClass, methodName, arguments);
		return (T) Enhancer.create(loader.getTargetClass(), loader);
	}

	private MethodReflector reflector;
	private String methodName;
	private Object[] arguments;

	private DaoLazyLoader(Class<?> daoClass, String methodName, Object[] arguments) {
		this.methodName = methodName;
		this.arguments = arguments;
		reflector = new MethodReflector(daoClass);
	}

	private Class<?> getTargetClass() {
		return reflector.getReturnType(methodName, arguments);
	}

	@Override
	public Object loadObject() {
		Object dao = new MethodReflector(DaoFactory.class).invoke("get" + reflector.getClassName());
		return reflector.invoke(dao, methodName, arguments);
	}
}