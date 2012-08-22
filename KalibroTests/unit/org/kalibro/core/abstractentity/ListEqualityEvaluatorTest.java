package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class ListEqualityEvaluatorTest extends KalibroTestCase {

	private ListEqualityEvaluator evaluator;

	@Before
	public void setUp() {
		evaluator = new ListEqualityEvaluator();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldEvaluateAnyTypeOfList() {
		assertTrue(evaluator.canEvaluate(new ArrayList<Object>()));
		assertTrue(evaluator.canEvaluate(new LinkedList<String>()));

		assertFalse(evaluator.canEvaluate(this));
		assertFalse(evaluator.canEvaluate(evaluator));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConsiderSize() {
		assertFalse(evaluator.equals(Arrays.asList("a", "b", "c"), Arrays.asList("a", "b")));
		assertFalse(evaluator.equals(Arrays.asList(this), Arrays.asList(evaluator, this)));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConsiderOrder() {
		assertTrue(evaluator.equals(Arrays.asList("a", "b"), Arrays.asList("a", "b")));
		assertFalse(evaluator.equals(Arrays.asList(this, evaluator), Arrays.asList(evaluator, this)));
	}
}