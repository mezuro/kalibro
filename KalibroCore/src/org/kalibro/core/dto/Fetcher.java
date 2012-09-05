package org.kalibro.core.dto;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public abstract class Fetcher<T> implements InvocationHandler {

	private T target;

	public T createProxy(Class<T> interfaceClass) {
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