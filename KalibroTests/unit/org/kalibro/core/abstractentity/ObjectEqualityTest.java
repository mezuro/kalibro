package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class ObjectEqualityTest extends UnitTest {

	private ObjectEquality equality;

	@Before
	public void setUp() {
		equality = new ObjectEquality();
	}

	@Test
	public void shouldEvaluateAnything() {
		assertTrue(equality.canEvaluate(""));
		assertTrue(equality.canEvaluate(null));
		assertTrue(equality.canEvaluate(this));
		assertTrue(equality.canEvaluate(equality));
	}

	@Test
	public void valuesShouldBeEqual() {
		assertTrue(equality.equals(42, 42));
		assertTrue(equality.equals(this, this));
		assertTrue(equality.equals(equality, equality));
		assertTrue(equality.equals("repeat", "repeat"));

		assertFalse(equality.equals(this, 42));
		assertFalse(equality.equals(equality, this));
		assertFalse(equality.equals(equality, new ObjectEquality()));
	}
}