package org.kalibro.core.processing;

import static org.kalibro.core.model.ConfigurationFixtures.*;

import javax.script.ScriptException;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.CompoundMetric;
import org.kalibro.core.model.CompoundMetricFixtures;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.MetricConfiguration;

public class ScriptValidatorTest extends KalibroTestCase {

	private Configuration configuration;
	private ScriptValidator validator;

	@Before
	public void setUp() {
		configuration = simpleConfiguration();
		validator = new ScriptValidator(configuration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldValidateSimpleReturn() {
		validate("return 0;");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldValidateExponentNotation() {
		validate("return 1E10;");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldValidateExpressions() {
		validate("flag = true; return flag ? 1 : 0;");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldValidateReferenceToValidIdentifiers() {
		validate(CompoundMetricFixtures.sc().getScript());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInvalidateSyntaxError() {
		assertInvalid("riturn 0;", ScriptException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInvalidateUnknownIdentifier() {
		assertInvalid("return something;", ScriptException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInvalidateNoReturn() {
		assertInvalid("acc = null;", NullPointerException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInvalidateNullReturn() {
		assertInvalid("return null;", NullPointerException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInvalidateNotNumberReturn() {
		assertInvalid("return 'My string';", ClassCastException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInvalidateInvalidCode() {
		assertInvalid("42", "return 1.0;", ScriptException.class);
	}

	private void assertInvalid(String script, Class<? extends Exception> exceptionClass) {
		assertInvalid("metric", script, exceptionClass);
	}

	private void assertInvalid(final String code, final String script, Class<? extends Exception> exceptionClass) {
		checkException(new Task() {

			@Override
			public void perform() throws Exception {
				validate(code, script);
			}
		}, RuntimeException.class, null, exceptionClass);
	}

	private void validate(String script) {
		validate("metric", script);
	}

	private void validate(String code, String script) {
		CompoundMetric metric = new CompoundMetric();
		metric.setName(code);
		metric.setScript(script);
		MetricConfiguration metricConfiguration = new MetricConfiguration(metric);
		metricConfiguration.setCode(code);
		configuration.addMetricConfiguration(metricConfiguration);
		validator.validateScriptOf(new MetricConfiguration(metric));
	}
}