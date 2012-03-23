package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.util.StackTracePrinter;

public class KalibroExceptionTest extends KalibroTestCase {

	private static final String MESSAGE = "KalibroExceptionTest message";

	private KalibroException exception;

	@Before
	public void setUp() {
		exception = new KalibroException(MESSAGE);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkMessageContructor() {
		assertSame(MESSAGE, exception.getMessage());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkMessageAndCauseContructor() {
		Throwable cause = new NullPointerException();
		exception = new KalibroException(MESSAGE, cause);
		assertSame(MESSAGE, exception.getMessage());
		assertSame(cause, exception.getCause());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintStackTraceToString() {
		assertEquals(new StackTracePrinter().printStackTrace(exception), exception.getPrintedStackTrace());
	}
}