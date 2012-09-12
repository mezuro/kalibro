package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Test;

public class KalibroErrorTest extends TestCase {

	private static final String MESSAGE = "KalibroErrorTest message";
	private static final Throwable CAUSE = new Exception();

	private KalibroError error;

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldBeAnError() {
		assertEquals(Error.class, KalibroError.class.getSuperclass());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldContructWithMessage() {
		error = new KalibroError(MESSAGE);
		assertSame(MESSAGE, error.getMessage());
		assertNull(error.getCause());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConstructWithMessageAndCause() {
		error = new KalibroError(MESSAGE, CAUSE);
		assertSame(MESSAGE, error.getMessage());
		assertSame(CAUSE, error.getCause());
	}
}