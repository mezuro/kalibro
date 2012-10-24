package org.kalibro.dto;

final class LazyLoadExpectation {

	Class<?> daoClass;
	String methodName;
	Object[] parameters;

	Object returnValue;

	LazyLoadExpectation(Class<?> daoClass, String methodName, Object... parameters) {
		this.daoClass = daoClass;
		this.methodName = methodName;
		this.parameters = parameters;
	}

	void thenReturn(Object value) {
		returnValue = value;
	}
}