package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class StackTraceElementEqualityEvaluatorTest extends KalibroTestCase {

	private StackTraceElementEqualityEvaluator evaluator;

	@Before
	public void setUp() {
		evaluator = new StackTraceElementEqualityEvaluator();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldEvaluateOnlyStackTraceElements() {
		assertTrue(evaluator.canEvaluate(firstElement(new Exception())));
		assertFalse(evaluator.canEvaluate(null));
		assertFalse(evaluator.canEvaluate(this));
		assertFalse(evaluator.canEvaluate(""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void equalElementsShouldBeEqual() {
		verifyFirstElementEqual(true, new Exception(), new Exception());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void differentElementsShouldNotBeEqual() {
		Exception exception1 = new Exception();
		Exception exception2 = new Exception();
		verifyFirstElementEqual(false, exception1, exception2);
	}

	private void verifyFirstElementEqual(boolean equal, Exception exception1, Exception exception2) {
		assertEquals(equal, evaluator.equals(firstElement(exception1), firstElement(exception2)));
	}

	private StackTraceElement firstElement(Exception exception) {
		return exception.getStackTrace()[0];
	}
}