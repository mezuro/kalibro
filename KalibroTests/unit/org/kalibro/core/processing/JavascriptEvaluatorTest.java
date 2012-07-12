package org.kalibro.core.processing;

import static org.powermock.api.mockito.PowerMockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Context.class)
public class JavascriptEvaluatorTest extends KalibroTestCase {

	private JavascriptEvaluator evaluator;

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddVariablesAndRetrieveTheirValues() {
		evaluator = new JavascriptEvaluator();
		evaluator.addVariable("wonders", 7.0);
		evaluator.addVariable("diceFaces", 6.0);
		assertDoubleEquals(6.0, evaluator.evaluate("diceFaces"));
		assertDoubleEquals(7.0, evaluator.evaluate("wonders"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddFunctionsAndExecuteThem() {
		evaluator = new JavascriptEvaluator();
		evaluator.addVariable("wonders", 7.0);
		evaluator.addVariable("diceFaces", 6.0);
		assertDoubleEquals(42.0, addFunctionAndExecute("straightFoward", "return 42;"));
		assertDoubleEquals(42.0, addFunctionAndExecute("usingVariables", "return wonders * diceFaces;"));
		assertDoubleEquals(1E6, addFunctionAndExecute("scientificNotation", "return 1E6;"));
		assertDoubleEquals(42E6, addFunctionAndExecute("calling", "return usingVariables() * scientificNotation();"));
		assertDoubleEquals(1.0, addFunctionAndExecute("conditional", "return wonders > diceFaces ? 1 : 0;"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldValidateVariableName() {
		evaluator = new JavascriptEvaluator();
		checkKalibroException(new Task() {

			@Override
			public void perform() throws Exception {
				evaluator.addVariable("1bad.name", 13.0);
			}
		}, "Invalid identifier: 1bad.name");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldValidateFunctionName() {
		evaluator = new JavascriptEvaluator();
		checkKalibroException(new Task() {

			@Override
			public void perform() throws Exception {
				evaluator.addFunction("badFunction'sName", "return 13;");
			}
		}, "Invalid identifier: badFunction'sName");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkSyntaxError() {
		assertInvalid("riturn 0;", EvaluatorException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkUnknownVariable() {
		assertInvalid("return something;", EcmaError.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkNoReturn() {
		assertInvalid("acc = null;", ClassCastException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkNullReturn() {
		assertInvalid("return null;", NullPointerException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkNotNumberReturn() {
		assertInvalid("return 'My string';", ClassCastException.class);
	}

	private void assertInvalid(final String body, Class<? extends Exception> exceptionClass) {
		evaluator = new JavascriptEvaluator();
		checkException(new Task() {

			@Override
			public void perform() throws Exception {
				addFunctionAndExecute("f", body);
			}
		}, exceptionClass);
	}

	private Double addFunctionAndExecute(String functionName, String body) {
		evaluator.addFunction(functionName, body);
		return evaluator.evaluate(functionName);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldExitContextOnFinalize() throws Throwable {
		evaluator = new JavascriptEvaluator();
		mockStatic(Context.class);
		evaluator.finalize();
		verifyStatic();
		Context.exit();
	}
}