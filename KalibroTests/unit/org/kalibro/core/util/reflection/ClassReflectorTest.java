package org.kalibro.core.util.reflection;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.Task;

public class ClassReflectorTest extends TestCase {

	private static double max(Double a, Double b) {
		return Math.max(a, b);
	}

	public static void throwThis(Throwable throwable) throws Throwable {
		throw throwable;
	}

	private ClassReflector reflector;

	@Before
	public void setUp() {
		reflector = new ClassReflector(ClassReflectorTest.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInvokeMethods() {
		double a = Math.random();
		double b = Math.random();
		assertEquals(max(a, b), reflector.invoke("max", a, b));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorWhenInvokingInexistentMethod() {
		checkKalibroError(new Task() {

			@Override
			public void perform() {
				reflector.invoke("inexistent");
			}
		}, "Error invoking method: " + getClass().getName() + ".inexistent", NoSuchMethodException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorWhenInvokingMethodWithDifferentNumberOfArguments() {
		checkKalibroError(new Task() {

			@Override
			public void perform() {
				reflector.invoke("max", 42.0);
			}
		}, "Error invoking method: " + getClass().getName() + ".max", NoSuchMethodException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorWhenInvokingMethodWithIncompatibleArguments() {
		checkKalibroError(new Task() {

			@Override
			public void perform() {
				reflector.invoke("throwThis", 42.0);
			}
		}, "Error invoking method: " + getClass().getName() + ".throwThis", NoSuchMethodException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowSameUncheckedExceptionThrownByInvokedMethod() {
		NullPointerException exception = new NullPointerException();
		try {
			reflector.invoke("throwThis", exception);
			fail("Did not throw expected exception");
		} catch (Exception caught) {
			assertSame(exception, caught);
		}
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowKalibroExceptionWrappingCheckedExceptionThrownByInvokedMethod() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				reflector.invoke("throwThis", new FileNotFoundException());
			}
		}, "Error invoking method: " + getClass().getName() + ".throwThis", FileNotFoundException.class);
	}
}