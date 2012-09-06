package org.kalibro.core.util.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.kalibro.KalibroError;
import org.kalibro.KalibroException;

/**
 * Performs reflective static calls on the specified class in an easy way.
 * 
 * @author Carlos Morais
 */
public class ClassReflector {

	private Class<?> theClass;

	public ClassReflector(Class<?> theClass) {
		this.theClass = theClass;
	}

	public Class<?> getReturnType(String methodName, Object... arguments) {
		return findMethod(methodName, arguments).getReturnType();
	}

	public Object invoke(String methodName, Object... arguments) {
		try {
			Method method = findMethod(methodName, arguments);
			method.setAccessible(true);
			return method.invoke(null, arguments);
		} catch (InvocationTargetException exception) {
			Throwable cause = exception.getCause();
			if (cause instanceof RuntimeException)
				throw (RuntimeException) cause;
			throw new KalibroException(errorMessage("invoking", methodName), cause);
		} catch (Exception exception) {
			throw new KalibroError("Method " + theClass.getName() + "." + methodName + " is not static", exception);
		}
	}

	private Method findMethod(String methodName, Object[] arguments) {
		for (Method method : theClass.getDeclaredMethods())
			if (isCompatible(method, methodName, arguments))
				return method;
		throw new KalibroError(errorMessage("finding", methodName));
	}

	private boolean isCompatible(Method method, String methodName, Object[] arguments) {
		return method.getName().equals(methodName) && areTypesCompatible(method.getParameterTypes(), arguments);
	}

	private boolean areTypesCompatible(Class<?>[] types, Object[] arguments) {
		if (types.length != arguments.length)
			return false;
		for (int i = 0; i < types.length; i++)
			if (!types[i].isAssignableFrom(arguments[i].getClass()))
				return false;
		return true;
	}

	private String errorMessage(String verb, String methodName) {
		return "Error " + verb + " method: " + theClass.getName() + "." + methodName;
	}
}