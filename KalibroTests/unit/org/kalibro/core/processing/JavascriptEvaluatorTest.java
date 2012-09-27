package org.kalibro.core.processing;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.ThrowableMatcher;
import org.kalibro.tests.UnitTest;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Context.class)
public class JavascriptEvaluatorTest extends UnitTest {

	private JavascriptEvaluator evaluator;

	@Before
	public void setUp() {
		evaluator = new JavascriptEvaluator();
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
			protected void perform() {
				evaluator.addVariable("1bad.name", 13.0);
			}
		}).throwsException().withMessage("Invalid identifier: 1bad.name");
	}

	@Test
	public void shouldValidateFunctionName() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				evaluator.addFunction("badFunction'sName", "return 13;");
			}
		}).throwsException().withMessage("Invalid identifier: badFunction'sName");
	}

	@Test
	public void shouldValidateSyntax() {
		shouldThrowExceptionEvaluating("riturn 0;").withCause(EvaluatorException.class);
	}

	@Test
	public void shouldValidateReferences() {
		shouldThrowExceptionEvaluating("return something;").withCause(EcmaError.class);
	}

	@Test
	public void shouldReturnDouble() {
		shouldThrowExceptionEvaluating("acc = null;").withCause(ClassCastException.class);
		shouldThrowExceptionEvaluating("return null;").withCause(NullPointerException.class);
		shouldThrowExceptionEvaluating("return 'My string';").withCause(ClassCastException.class);
	}

	private ThrowableMatcher shouldThrowExceptionEvaluating(final String body) {
		return assertThat(new VoidTask() {

			@Override
			protected void perform() {
				addFunctionAndExecute("f", body);
			}
		}).throwsException();
	}

	private Double addFunctionAndExecute(String functionName, String body) {
		evaluator.addFunction(functionName, body);
		return evaluator.evaluate(functionName);
	}

	@Test
	public void shouldExitContextOnClose() {
		spy(Context.class);
		evaluator.close();
		verifyStatic();
		Context.exit();
	}
}