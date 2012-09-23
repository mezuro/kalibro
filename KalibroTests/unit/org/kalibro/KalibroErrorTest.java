package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class KalibroErrorTest extends UnitTest {

	private static final String MESSAGE = "KalibroErrorTest message";
	private static final Throwable CAUSE = new Exception();

	private KalibroError error;

	@Test
	public void shouldBeAnError() {
		assertEquals(Error.class, KalibroError.class.getSuperclass());
	}

	@Test
	public void shouldContructWithMessage() {
		error = new KalibroError(MESSAGE);
		assertSame(MESSAGE, error.getMessage());
		assertNull(error.getCause());
	}

	@Test
	public void shouldConstructWithMessageAndCause() {
		error = new KalibroError(MESSAGE, CAUSE);
		assertSame(MESSAGE, error.getMessage());
		assertSame(CAUSE, error.getCause());
	}
}