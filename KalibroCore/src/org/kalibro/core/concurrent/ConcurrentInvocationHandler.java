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
	private boolean running;
	private LinkedBlockingQueue<MethodInvocation> methodInvocations;

	private ConcurrentInvocationHandler(Object object) {
		super();
		this.object = object;
		running = true;
		methodInvocations = new LinkedBlockingQueue<MethodInvocation>();
		executeInBackground();
	}

	@Override
	public void perform() throws Throwable {
		while (running)
			methodInvocations.take().invokeAndNotify();
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		MethodInvocation invocation = new MethodInvocation(object, method, args);
		invocation.addToQueueAndWait(methodInvocations);
		return invocation.getResult();
	}

	@Override
	public void finalize() throws Throwable {
		running = false;
		MethodInvocation finalize = new MethodInvocation(object, Object.class.getDeclaredMethod("finalize"));
		methodInvocations.add(finalize);
		super.finalize();
	}

	@Override
	public String toString() {
		return "handling invocations for " + object.getClass().getName();
	}
}