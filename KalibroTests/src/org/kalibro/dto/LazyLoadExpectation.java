package org.kalibro.dto;

final class LazyLoadExpectation {

	Object stub;
	Class<?> daoClass;
	String methodName;
	Object[] parameters;

	LazyLoadExpectation(Object stub, Class<?> daoClass, String methodName, Object... parameters) {
		this.stub = stub;
		this.daoClass = daoClass;
		this.methodName = methodName;
		this.parameters = parameters;
	}
}