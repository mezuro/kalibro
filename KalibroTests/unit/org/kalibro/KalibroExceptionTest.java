package org.kalibro;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class KalibroExceptionTest extends UnitTest {

	private static final String MESSAGE = "KalibroExceptionTest message";
	private static final Throwable CAUSE = new InvocationTargetException(new NullPointerException(), MESSAGE);
	private static final Throwable TARGET = CAUSE;

	private KalibroException exception;

	@Test
	public void shouldBeUnchecked() {
		assertEquals(RuntimeException.class, KalibroException.class.getSuperclass());
	}

	@Test
	public void checkMessageConstruction() {
		exception = new KalibroException(MESSAGE);
		assertSame(MESSAGE, exception.getMessage());
		assertNull(exception.getCause());
	}

	@Test
	public void shouldMessageAndCauseConstruction() {
		exception = new KalibroException(MESSAGE, CAUSE);
		assertSame(MESSAGE, exception.getMessage());
		assertSame(CAUSE, exception.getCause());
	}

	@Test
	public void checkTargetConstruction() {
		exception = new KalibroException(TARGET);
		assertSame(TARGET.getCause(), exception.getCause());
		assertSame(TARGET.getMessage(), exception.getMessage());
		assertDeepEquals(TARGET.getStackTrace(), exception.getStackTrace());
	}

	@Test
	public void toStringShouldBeEqualTargetToString() {
		assertEquals("org.kalibro.KalibroException", new KalibroException((String) null).toString());
		assertEquals(TARGET.toString(), new KalibroException(TARGET).toString());
	}
}