package org.kalibro.core.processing;

import javax.script.ScriptException;

import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;

public class ScriptEvaluatorTest extends KalibroTestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldEvaluateFunction() {
		invokeAndCheck("getZero", "function getZero() {return 0;}", 0.0);
		invokeAndCheck("getMillion", "function getMillion() {return 1E6;}", 1E6);
		invokeAndCheck("flagToInt", "flag = true; function flagToInt(){return flag ? 1 : 0;}", 1.0);
		invokeAndCheck("sc", "cbo = 3; lcom4 = 2; function sc(){ return cbo * lcom4;}", 6.0);
	}

	private void invokeAndCheck(String function, String script, Double result) {
		assertDoubleEquals(result, invoke(function, script));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkSyntaxError() {
		assertInvalid("riturn 0;", ScriptException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkUnknownIdentifier() {
		assertInvalid("return something;", ScriptException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkNoReturn() {
		assertInvalid("acc = null;", NullPointerException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkNullReturn() {
		assertInvalid("return null;", NullPointerException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkNotNumberReturn() {
		assertInvalid("return 'My string';", ClassCastException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkInvalidFunction() {
		assertInvalid("g", "function f() {return 0;}", NoSuchMethodException.class);
	}

	private void assertInvalid(String functionBody, Class<? extends Exception> exceptionClass) {
		assertInvalid("f", "function f(){" + functionBody + "}", exceptionClass);
	}

	private void assertInvalid(final String function, final String script, Class<? extends Exception> exceptionClass) {
		checkException(new Task() {

			@Override
			public void perform() throws Exception {
				invoke(function, script);
			}
		}, RuntimeException.class, null, exceptionClass);
	}

	private Double invoke(String function, String script) {
		return new ScriptEvaluator(script).invokeFunction(function);
	}
}