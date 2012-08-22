package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroError;
import org.kalibro.KalibroException;
import org.kalibro.KalibroTestCase;

public class ThrowableEqualityEvaluatorTest extends KalibroTestCase {

	private ThrowableEqualityEvaluator evaluator;

	@Before
	public void setUp() {
		evaluator = new ThrowableEqualityEvaluator();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldEvaluateThrowable() {
		assertTrue(evaluator.canEvaluate(new Exception()));
		assertTrue(evaluator.canEvaluate(new KalibroError("")));
		assertTrue(evaluator.canEvaluate(new KalibroException("")));

		assertFalse(evaluator.canEvaluate(""));
		assertFalse(evaluator.canEvaluate(this));
		assertFalse(evaluator.canEvaluate(evaluator));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldEvaluateDeeply() {
		Throwable throwable = new KalibroException("DeepThrowable", new Exception());
		Exception other = new KalibroException(throwable.getMessage());
		assertFalse(evaluator.equals(throwable, other));

		other.setStackTrace(throwable.getStackTrace());
		assertFalse(evaluator.equals(throwable, other));

		other.initCause(throwable.getCause());
		assertTrue(evaluator.equals(throwable, other));
	}
}