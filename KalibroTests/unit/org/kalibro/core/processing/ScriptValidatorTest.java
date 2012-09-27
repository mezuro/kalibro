package org.kalibro.core.processing;

import static org.kalibro.ConfigurationFixtures.newConfiguration;
import static org.kalibro.MetricFixtures.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Configuration;
import org.kalibro.MetricConfiguration;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.ThrowableMatcher;
import org.kalibro.tests.UtilityClassTest;
import org.mozilla.javascript.EcmaError;

public class ScriptValidatorTest extends UtilityClassTest {

	private Configuration configuration;
	private MetricConfiguration cbo, sc;

	@Before
	public void setUp() {
		sc = new MetricConfiguration(newSc());
		configuration = newConfiguration("cbo", "lcom4");
		configuration.addMetricConfiguration(sc);
		cbo = configuration.getConfigurationFor(analizoMetric("cbo"));
		assertValid();
	}

	@Override
	protected Class<?> utilityClass() {
		return ScriptValidator.class;
	}

	@Test
	public void shouldValidateNativeCode() {
		cbo.setCode("42");
		assertInvalid("Invalid identifier: 42");
	}

	@Test
	public void shouldValidateCompoundCode() {
		sc.setCode("42");
		assertInvalid("Invalid identifier: 42");
	}

	@Test
	public void shouldValidateCompoundScript() {
		configuration.removeMetricConfiguration(cbo);
		assertInvalid("Error evaluating Javascript for: structuralComplexity").withCause(EcmaError.class);
	}

	private ThrowableMatcher assertInvalid(String message) {
		return assertThat(new VoidTask() {

			@Override
			protected void perform() throws Exception {
				assertValid();
			}
		}).throwsException().withMessage(message);
	}

	private void assertValid() {
		ScriptValidator.validate(configuration);
	}
}