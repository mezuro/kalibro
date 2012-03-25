package org.kalibro.core.model.abstracts;

import org.kalibro.KalibroException;

class NoIdentityEntity extends AbstractEntity<NoIdentityEntity> {

	protected String field1, field2;

	public void throwKalibroException() {
		throw new KalibroException("throwing KalibroException");
	}

	public void throwNullPointer() {
		throw new NullPointerException();
	}
}