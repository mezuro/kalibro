package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class SetEqualityEvaluatorTest extends KalibroTestCase {

	private SetEqualityEvaluator evaluator;

	@Before
	public void setUp() {
		evaluator = new SetEqualityEvaluator();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldEvaluateAnyTypeOfSet() {
		assertTrue(evaluator.canEvaluate(new HashSet<Object>()));
		assertTrue(evaluator.canEvaluate(new TreeSet<String>()));

		assertFalse(evaluator.canEvaluate(this));
		assertFalse(evaluator.canEvaluate(evaluator));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConsiderSize() {
		assertFalse(evaluator.equals(newHashSet("a", "b", "c"), newHashSet("a", "b")));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConsiderElements() {
		assertFalse(evaluator.equals(newTreeSet("a", "b"), newTreeSet("c", "d")));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotConsiderOrder() {
		assertTrue(evaluator.equals(newTreeSet("a", "b"), newTreeSet("b", "a")));
		assertTrue(evaluator.equals(newHashSet("d", "c", "c"), newHashSet("c", "d")));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotConsiderTypeOfSet() {
		assertTrue(evaluator.equals(newTreeSet("a", "b"), newHashSet("b", "a")));
	}

	private HashSet<String> newHashSet(String... elements) {
		return new HashSet<String>(Arrays.asList(elements));
	}

	private TreeSet<String> newTreeSet(String... elements) {
		return new TreeSet<String>(Arrays.asList(elements));
	}
}