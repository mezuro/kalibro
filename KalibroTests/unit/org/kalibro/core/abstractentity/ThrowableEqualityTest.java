package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class ThrowableEqualityTest extends UnitTest {

	private ThrowableEquality equality;

	@Before
	public void setUp() {
		equality = new ThrowableEquality();
	}

	@Test
	public void shouldEvaluateThrowable() {
		assertTrue(equality.canEvaluate(new Error()));
		assertTrue(equality.canEvaluate(new Exception()));
		assertTrue(equality.canEvaluate(new RuntimeException()));

		assertFalse(equality.canEvaluate(null));
		assertFalse(equality.canEvaluate(this));
		assertFalse(equality.canEvaluate(equality));
	}

	@Test
	public void classesShouldBeEqual() {
		assertFalse(equality.equals(new Exception(), new RuntimeException()));
	}

	@Test
	public void messagesShouldBeEqual() {
		assertFalse(equality.equals(new Exception("42"), new Exception("28")));
	}

	@Test
	public void causesShouldBeEqual() {
		assertFalse(equality.equals(new Exception(), new Exception(new NullPointerException())));
	}

	@Test
	public void stackTracesShouldBeEqual() {
		Exception value = new Exception();
		assertFalse(equality.equals(value, new Exception()));
	}

	@Test
	public void allFourShouldBeDeepEqual() {
		assertTrue(equality.equals(new Exception(), new Exception()));
	}
}