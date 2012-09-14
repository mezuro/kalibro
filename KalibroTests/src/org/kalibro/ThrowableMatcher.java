package org.kalibro;

import static org.junit.Assert.assertEquals;

public class ThrowableMatcher {

	private Throwable throwed;

	protected ThrowableMatcher(Throwable throwed) {
		this.throwed = throwed;
	}

	public ThrowableMatcher withMessage(String message) {
		assertEquals(message, throwed.getMessage());
		return this;
	}

	public ThrowableMatcher withCause(Class<? extends Throwable> causeClass) {
		ExtendedAsserts.assertClassEquals(causeClass, throwed.getCause());
		return this;
	}
}