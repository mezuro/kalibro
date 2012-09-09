package org.kalibro.core.processing;

import java.lang.reflect.Constructor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.Task;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Context.class)
public class JavascriptEvaluatorTest extends TestCase {

	private ScriptEvaluator evaluator;

	@Before
	public void setUp() {
		evaluator = JavascriptEvaluator.create();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddVariablesAndRetrieveTheirValues() {
		evaluator.addVariable("wonders", 7.0);
		evaluator.addVariable("diceFaces", 6.0);
		assertDoubleEquals(6.0, evaluator.evaluate("diceFaces"));
		assertDoubleEquals(7.0, evaluator.evaluate("wonders"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddFunctionsAndExecuteThem() {
		evaluator.addVariable("$wonders", 7.0);
		evaluator.addVariable("dice_faces", 6.0);
		assertDoubleEquals(42.0, addFunctionAndExecute("straightFoward", "return 42;"));
		assertDoubleEquals(42.0, addFunctionAndExecute("_answer", "return $wonders * dice_faces;"));
		assertDoubleEquals(1E6, addFunctionAndExecute("scientific_notation", "return 1E6;"));
		assertDoubleEquals(42E6, addFunctionAndExecute("calling", "return _answer() * scientific_notation();"));
		assertDoubleEquals(1.0, addFunctionAndExecute("conditional", "return $wonders > dice_faces ? 1 : 0;"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldValidateVariableName() {
		checkKalibroException(new Task() {

			@Override
			public void perform() throws Exception {
				evaluator.addVariable("1bad.name", 13.0);
			}
		}, "Invalid identifier: 1bad.name");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldValidateFunctionName() {
		checkKalibroException(new Task() {

			@Override
			public void perform() throws Exception {
				evaluator.addFunction("badFunction'sName", "return 13;");
			}
		}, "Invalid identifier: badFunction'sName");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemove() {
		evaluator.addVariable("variable", 42.0);
		assertDoubleEquals(42.0, evaluator.evaluate("variable"));

		evaluator.remove("variable");
		checkException(new Task() {

			@Override
			public void perform() throws Exception {
				evaluator.evaluate("variable");
			}
		}, ClassCastException.class);
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
		evaluator = createEvaluator();
		mockStatic(Context.class);
		((JavascriptEvaluator) evaluator).finalize();
		verifyStatic();
		Context.exit();
	}

	private JavascriptEvaluator createEvaluator() throws Exception {
		Constructor<JavascriptEvaluator> constructor = JavascriptEvaluator.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		return constructor.newInstance();
	}
}