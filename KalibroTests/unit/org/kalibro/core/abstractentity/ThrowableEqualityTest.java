package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class ThrowableEqualityTest extends KalibroTestCase {

	private ThrowableEquality equality;

	@Before
	public void setUp() {
		equality = new ThrowableEquality();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldEvaluateThrowable() {
		assertTrue(equality.canEvaluate(new Error()));
		assertTrue(equality.canEvaluate(new Exception()));
		assertTrue(equality.canEvaluate(new RuntimeException()));

		assertFalse(equality.canEvaluate(null));
		assertFalse(equality.canEvaluate(this));
		assertFalse(equality.canEvaluate(equality));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void classesShouldBeEqual() {
		assertFalse(equality.equals(new Exception(), new RuntimeException()));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void messagesShouldBeEqual() {
		assertFalse(equality.equals(new Exception("42"), new Exception("28")));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void causesShouldBeEqual() {
		assertFalse(equality.equals(new Exception(), new Exception(new NullPointerException())));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void stackTracesShouldBeEqual() {
		Exception value = new Exception();
		assertFalse(equality.equals(value, new Exception()));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void stackTraceAndCauseShouldBeDeepEqual() {
		assertTrue(equality.equals(new Exception(), new Exception()));
	}
}