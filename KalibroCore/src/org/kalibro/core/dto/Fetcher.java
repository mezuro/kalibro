package org.kalibro.core.dto;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Abstract class which creates a proxy for an target object that is to be fetched only on first use. Implementors
 * should define how to fetch the target.
 * 
 * @author Carlos Morais
 */
abstract class Fetcher<T> implements InvocationHandler {

	private T target;

	protected T createProxy(Class<T> interfaceClass) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
		if (target == null)
			target = fetch();
		return method.invoke(target, arguments);
	}

	protected abstract T fetch();
}