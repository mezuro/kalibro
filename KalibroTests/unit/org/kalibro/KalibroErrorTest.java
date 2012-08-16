package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Test;

public class KalibroErrorTest extends KalibroTestCase {

	private static final String MESSAGE = "KalibroErrorTest message";

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddPrefixBeforeMessage() {
		assertEquals("Please report this bug: " + MESSAGE, new KalibroError(MESSAGE).getMessage());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAllowConstructionWithCause() {
		Throwable cause = new Exception();
		assertSame(cause, new KalibroError(MESSAGE, cause).getCause());
	}
}