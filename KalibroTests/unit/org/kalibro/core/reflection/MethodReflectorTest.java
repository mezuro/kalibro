package org.kalibro.core.reflection;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.UnitTest;

public class MethodReflectorTest extends UnitTest {

	private static double max(double a, Double b) {
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

	@Test
	public void shouldGetSimpleClassName() {
		assertEquals("MethodReflectorTest", reflector.getClassName());
	}

	@Test
	public void shouldGetReturnTypeOfMethods() {
		assertEquals(double.class, reflector.getReturnType("max", 42.0, 42.0));
		assertEquals(void.class, reflector.getReturnType("throwThis", new Exception()));
	}

	@Test
	public void shouldInvokeMethods() {
		double a = Math.random();
		double b = Math.random();
		assertEquals(max(a, b), reflector.invoke("max", a, b));
	}

	@Test
	public void shouldFindSuperclassMethod() {
		assertEquals(Timeout.class, reflector.getReturnType("testTimeout"));
	}

	@Test
	public void shouldThrowErrorWhenInvokingNonstaticMethod() {
		assertThat(invoke("setUp")).throwsError().withCause(NullPointerException.class)
			.withMessage(expectedMessage("invoking", "setUp"));
	}

	@Test
	public void shouldThrowErrorWhenFindingInexistentMethod() {
		assertThat(invoke("inexistent")).throwsError()
			.withMessage(expectedMessage("finding", "inexistent"));
	}

	@Test
	public void shouldThrowErrorWhenFindingMethodWithDifferentNumberOfArguments() {
		assertThat(invoke("max", 42.0)).throwsError()
			.withMessage(expectedMessage("finding", "max"));
	}

	@Test
	public void shouldThrowErrorWhenFindingMethodWithIncompatibleArguments() {
		assertThat(invoke("throwThis", 42.0)).throwsError()
			.withMessage(expectedMessage("finding", "throwThis"));
	}

	@Test
	public void shouldThrowSameUncheckedExceptionThrownByInvokedMethod() {
		NullPointerException exception = new NullPointerException();
		assertThat(invoke("throwThis", exception)).doThrow(exception);
	}

	@Test
	public void shouldThrowKalibroExceptionWrappingCheckedExceptionThrownByInvokedMethod() {
		assertThat(invoke("throwThis", new FileNotFoundException())).throwsException()
			.withMessage(expectedMessage("invoking", "throwThis")).withCause(FileNotFoundException.class);
	}

	private VoidTask invoke(final String method, final Object... parameters) {
		return new VoidTask() {

			@Override
			protected void perform() {
				reflector.invoke(method, parameters);
			}
		};
	}

	private String expectedMessage(String verb, String methodName) {
		return "Error " + verb + " method: " + getClass().getName() + "." + methodName;
	}
}