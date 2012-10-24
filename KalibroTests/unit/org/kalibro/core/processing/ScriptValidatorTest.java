package org.kalibro.core.processing;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.CompoundMetric;
import org.kalibro.Configuration;
import org.kalibro.MetricConfiguration;
import org.kalibro.NativeMetric;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.ThrowableMatcher;
import org.kalibro.tests.UtilityClassTest;
import org.mozilla.javascript.EcmaError;

public class ScriptValidatorTest extends UtilityClassTest {

	private Configuration configuration;
	private MetricConfiguration cbo, sc;

	@Before
	public void setUp() {
		configuration = loadFixture("sc", Configuration.class);
		sc = configuration.getConfigurationFor(loadFixture("sc", CompoundMetric.class));
		cbo = configuration.getConfigurationFor(loadFixture("cbo", NativeMetric.class));
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
		assertInvalid("Error evaluating Javascript for: sc").withCause(EcmaError.class);
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