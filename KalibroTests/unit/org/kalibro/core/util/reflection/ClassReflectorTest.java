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
	public void shouldGetReturnType() {
		assertEquals(double.class, reflector.getReturnType("max", 42.0, 42.0));
		assertEquals(void.class, reflector.getReturnType("throwThis", new Exception()));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInvokeMethods() {
		double a = Math.random();
		double b = Math.random();
		assertEquals(max(a, b), reflector.invoke("max", a, b));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorWhenInvokingNonstaticMethod() {
		checkKalibroError(new Task() {

			@Override
			public void perform() {
				reflector.invoke("setUp");
			}
		}, "Method " + getClass().getName() + ".setUp is not static", NullPointerException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorWhenFindingInexistentMethod() {
		checkKalibroError(new Task() {

			@Override
			public void perform() {
				reflector.invoke("inexistent");
			}
		}, expectedMessage("finding", "inexistent"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorWhenFindingMethodWithDifferentNumberOfArguments() {
		checkKalibroError(new Task() {

			@Override
			public void perform() {
				reflector.invoke("max", 42.0);
			}
		}, expectedMessage("finding", "max"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorWhenFindingMethodWithIncompatibleArguments() {
		checkKalibroError(new Task() {

			@Override
			public void perform() {
				reflector.invoke("throwThis", 42.0);
			}
		}, expectedMessage("finding", "throwThis"));
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
		}, expectedMessage("invoking", "throwThis"), FileNotFoundException.class);
	}

	private String expectedMessage(String verb, String methodName) {
		return "Error " + verb + " method: " + getClass().getName() + "." + methodName;
	}
}