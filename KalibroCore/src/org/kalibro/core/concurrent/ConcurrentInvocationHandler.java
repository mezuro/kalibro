package org.kalibro.core.concurrent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.LinkedBlockingQueue;

public final class ConcurrentInvocationHandler extends Task implements InvocationHandler {

	public static <T> T createProxy(Object object, Class<T> interfaceClass) {
		InvocationHandler handler = new ConcurrentInvocationHandler(object);
		return (T) Proxy.newProxyInstance(object.getClass().getClassLoader(), new Class[]{interfaceClass}, handler);
	}

	private Object object;
	private LinkedBlockingQueue<MethodInvocation> methodInvocations;

	private ConcurrentInvocationHandler(Object object) {
		super();
		this.object = object;
		methodInvocations = new LinkedBlockingQueue<MethodInvocation>();
		executeInBackground();
	}

	@Override
	protected void perform() throws Throwable {
		while (true) {
			MethodInvocation invocation = methodInvocations.take();
			synchronized (invocation) {
				invocation.invoke();
				invocation.notify();
			}
		}
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		MethodInvocation invocation = new MethodInvocation(object, method, args);
		synchronized (invocation) {
			methodInvocations.add(invocation);
			while (!invocation.done())
				invocation.wait();
		}
		return invocation.getResult();
	}
}