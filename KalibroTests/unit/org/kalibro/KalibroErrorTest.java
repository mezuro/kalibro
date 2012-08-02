package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.util.StackTracePrinter;

public class KalibroErrorTest extends KalibroTestCase {

	private static final String MESSAGE = "KalibroErrorTest message";

	private Throwable cause;
	private KalibroError error;

	@Before
	public void setUp() {
		cause = new Exception();
		error = new KalibroError(MESSAGE, cause);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkContructor() {
		assertEquals("Please report this bug: " + MESSAGE, error.getMessage());
		assertSame(cause, error.getCause());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrintStackTraceToString() {
		assertEquals(new StackTracePrinter().printStackTrace(error), error.getPrintedStackTrace());
	}
}