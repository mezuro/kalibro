package org.kalibro.util.reflection;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.Task;

public class MethodReflectorTest extends TestCase {

	private static double max(Double a, Double b) {
		return Math.max(a, b);
	}

	public static void throwThis(Throwable throwable) throws Throwable {
		throw throwable;
	}

	private MethodReflector reflector;

	@Before
	public void setUp() {
		reflector = new MethodReflector(MethodReflectorTest.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetSimpleClassName() {
		assertEquals("MethodReflectorTest", reflector.getClassName());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetReturnTypeOfMethods() {
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
		checkKalibroError(invokeTask("setUp"), expectedMessage("invoking", "setUp"), NullPointerException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorWhenFindingInexistentMethod() {
		checkKalibroError(invokeTask("inexistent"), expectedMessage("finding", "inexistent"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorWhenFindingMethodWithDifferentNumberOfArguments() {
		checkKalibroError(invokeTask("max", 42.0), expectedMessage("finding", "max"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowErrorWhenFindingMethodWithIncompatibleArguments() {
		checkKalibroError(invokeTask("throwThis", 42.0), expectedMessage("finding", "throwThis"));
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
		checkKalibroException(invokeTask("throwThis", new FileNotFoundException()),
			expectedMessage("invoking", "throwThis"), FileNotFoundException.class);
	}

	private Task invokeTask(final String method, final Object... parameters) {
		return new Task() {

			@Override
			public void perform() {
				reflector.invoke(method, parameters);
			}
		};
	}

	private String expectedMessage(String verb, String methodName) {
		return "Error " + verb + " method: " + getClass().getName() + "." + methodName;
	}
}