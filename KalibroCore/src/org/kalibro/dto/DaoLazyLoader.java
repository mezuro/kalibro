package org.kalibro.dto;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.LazyLoader;

import org.kalibro.dao.DaoFactory;
import org.kalibro.util.reflection.MethodReflector;

/**
 * Creates lazy load proxies that call {@link DaoFactory} methods to load.
 * 
 * @author Carlos Morais
 */
final class DaoLazyLoader implements LazyLoader {

	static Object createProxy(Class<?> daoClass, String methodName, Object... arguments) {
		DaoLazyLoader loader = new DaoLazyLoader(daoClass, methodName, arguments);
		return Enhancer.create(loader.getTargetClass(), loader);
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