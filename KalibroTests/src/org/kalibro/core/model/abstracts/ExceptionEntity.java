package org.kalibro.core.model.abstracts;

import org.kalibro.KalibroException;

class ExceptionEntity extends AbstractEntity<ExceptionEntity> {

	private KalibroException exception = new KalibroException("ExceptionEntity", new NullPointerException());

	public void throwException() {
		throw exception;
	}

	public void throwCause() throws Throwable {
		throw exception.getCause();
	}
}