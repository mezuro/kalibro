package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class ArrayEqualityEvaluatorTest extends KalibroTestCase {

	private ArrayEqualityEvaluator evaluator;

	@Before
	public void setUp() {
		evaluator = new ArrayEqualityEvaluator();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldEvaluateAnyTypeOfArray() {
		assertTrue(evaluator.canEvaluate(new Object[0]));
		assertTrue(evaluator.canEvaluate(new String[1]));
		assertTrue(evaluator.canEvaluate(new Test[2]));
		assertTrue(evaluator.canEvaluate(new Before[3]));

		assertFalse(evaluator.canEvaluate(this));
		assertFalse(evaluator.canEvaluate(evaluator));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConsiderSize() {
		assertFalse(evaluator.equals(new String[]{"a", "b", "c"}, new String[]{"a", "b"}));
		assertFalse(evaluator.equals(new Object[]{this}, new Object[]{evaluator, this}));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConsiderOrder() {
		assertTrue(evaluator.equals(new String[]{"a", "b"}, new String[]{"a", "b"}));
		assertFalse(evaluator.equals(new Object[]{this, evaluator}, new Object[]{evaluator, this}));
	}
}