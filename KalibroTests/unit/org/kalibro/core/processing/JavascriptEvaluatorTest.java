package org.kalibro.core.processing;

import java.lang.reflect.Constructor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.VoidTask;
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

	@Test
	public void shouldAddVariablesAndRetrieveTheirValues() {
		evaluator.addVariable("wonders", 7.0);
		evaluator.addVariable("diceFaces", 6.0);
		assertDoubleEquals(6.0, evaluator.evaluate("diceFaces"));
		assertDoubleEquals(7.0, evaluator.evaluate("wonders"));
	}

	@Test
	public void shouldAddFunctionsAndExecuteThem() {
		evaluator.addVariable("$wonders", 7.0);
		evaluator.addVariable("dice_faces", 6.0);
		assertDoubleEquals(42.0, addFunctionAndExecute("straightFoward", "return 42;"));
		assertDoubleEquals(42.0, addFunctionAndExecute("_answer", "return $wonders * dice_faces;"));
		assertDoubleEquals(1E6, addFunctionAndExecute("scientific_notation", "return 1E6;"));
		assertDoubleEquals(42E6, addFunctionAndExecute("calling", "return _answer() * scientific_notation();"));
		assertDoubleEquals(1.0, addFunctionAndExecute("conditional", "return $wonders > dice_faces ? 1 : 0;"));
	}

	@Test
	public void shouldValidateVariableName() {
		assertThat(new VoidTask() {

			@Override
			public void perform() {
				evaluator.addVariable("1bad.name", 13.0);
			}
		}).throwsException().withMessage("Invalid identifier: 1bad.name");
	}

	@Test
	public void shouldValidateFunctionName() {
		assertThat(new VoidTask() {

			@Override
			public void perform() {
				evaluator.addFunction("badFunction'sName", "return 13;");
			}
		}).throwsException().withMessage("Invalid identifier: badFunction'sName");
	}

	@Test
	public void shouldRemove() {
		evaluator.addVariable("variable", 42.0);
		assertDoubleEquals(42.0, evaluator.evaluate("variable"));

		evaluator.remove("variable");
		assertThat(new VoidTask() {

			@Override
			public void perform() {
				evaluator.evaluate("variable");
			}
		}).doThrow(ClassCastException.class);
	}

	@Test
	public void checkSyntaxError() {
		assertInvalid("riturn 0;", EvaluatorException.class);
	}

	@Test
	public void checkUnknownVariable() {
		assertInvalid("return something;", EcmaError.class);
	}

	@Test
	public void checkNoReturn() {
		assertInvalid("acc = null;", ClassCastException.class);
	}

	@Test
	public void checkNullReturn() {
		assertInvalid("return null;", NullPointerException.class);
	}

	@Test
	public void checkNotNumberReturn() {
		assertInvalid("return 'My string';", ClassCastException.class);
	}

	private void assertInvalid(final String body, Class<? extends Exception> exceptionClass) {
		assertThat(new VoidTask() {

			@Override
			public void perform() {
				addFunctionAndExecute("f", body);
			}
		}).doThrow(exceptionClass);
	}

	private Double addFunctionAndExecute(String functionName, String body) {
		evaluator.addFunction(functionName, body);
		return evaluator.evaluate(functionName);
	}

	@Test
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