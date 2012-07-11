package org.kalibro.core.processing;

import static org.powermock.api.mockito.PowerMockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.mozilla.javascript.Context;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Context.class)
public class JavascriptEvaluatorTest extends KalibroTestCase {

	private JavascriptEvaluator evaluator;

	@Before
	public void setUp() {
		evaluator = new JavascriptEvaluator();
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
		evaluator = new JavascriptEvaluator();
		evaluator.addVariable("wonders", 7.0);
		evaluator.addVariable("diceFaces", 6.0);
		evaluator.addFunction("answer", "return wonders * diceFaces;");
		assertDoubleEquals(42.0, evaluator.evaluate("answer"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldExitContextOnFinalize() throws Throwable {
		mockStatic(Context.class);
		evaluator.finalize();
		verifyStatic();
		Context.exit();
	}
}