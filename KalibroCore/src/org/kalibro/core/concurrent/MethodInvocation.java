package org.kalibro.core.concurrent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Queue;

class MethodInvocation {

	private Object object;
	private Method method;
	private Object[] arguments;

	private boolean done;
	private Object result;
	private Throwable error;

	protected MethodInvocation(Object object, Method method, Object... arguments) {
		this.object = object;
		this.method = method;
		this.arguments = arguments;
	}

	protected void invoke() {
		try {
			result = method.invoke(object, arguments);
		} catch (Throwable exception) {
			error = exception;
		}
		done = true;
	}

	protected Object getResult() throws Throwable {
		if (error != null)
			throw (error instanceof InvocationTargetException) ? error.getCause() : error;
		return result;
	}

	protected synchronized void addToQueueAndWait(Queue<MethodInvocation> queue) throws Exception {
		queue.add(this);
		while (!done)
			wait();
	}

	protected synchronized void invokeAndNotify() {
		invoke();
		notify();
	}
}