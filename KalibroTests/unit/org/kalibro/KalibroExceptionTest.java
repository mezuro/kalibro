package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Test;

public class KalibroExceptionTest extends TestCase {

	private static final String MESSAGE = "KalibroExceptionTest message";
	private static final Throwable CAUSE = new Exception();

	private KalibroException exception;

	@Test
	public void shouldBeUnchecked() {
		assertEquals(RuntimeException.class, KalibroException.class.getSuperclass());
	}

	@Test
	public void shouldContructWithMessage() {
		exception = new KalibroException(MESSAGE);
		assertSame(MESSAGE, exception.getMessage());
		assertNull(exception.getCause());
	}

	@Test
	public void shouldConstructWithMessageAndCause() {
		exception = new KalibroException(MESSAGE, CAUSE);
		assertSame(MESSAGE, exception.getMessage());
		assertSame(CAUSE, exception.getCause());
	}
}