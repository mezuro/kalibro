package org.kalibro.core.processing;

import static org.kalibro.MetricConfigurationFixtures.metricConfiguration;
import static org.kalibro.MetricFixtures.sc;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.CompoundMetric;
import org.kalibro.KalibroException;
import org.kalibro.MetricConfiguration;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.UnitTest;
import org.mozilla.javascript.EcmaError;

public class ScriptValidatorTest extends UnitTest {

	private MetricConfiguration configuration;
	private ScriptValidator validator;

	@Before
	public void setUp() {
		validator = new ScriptValidator();
	}

	@Test
	public void shouldValidateNativeCode() {
		configuration = metricConfiguration("loc");
		assertValid();

		configuration.setCode("42");
		assertInvalid(KalibroException.class);
	}

	@Test
	public void shouldValidateCompoundCode() {
		CompoundMetric metric = new CompoundMetric();
		metric.setName("compound");
		configuration = new MetricConfiguration(metric);
		assertValid();

		configuration.setCode("42");
		assertInvalid(KalibroException.class);
	}

	@Test
	public void shouldValidateCompoundScript() {
		configuration = new MetricConfiguration(sc());
		assertInvalid(EcmaError.class);

		MetricConfiguration cboConfiguration = metricConfiguration("cbo");
		validator.add(cboConfiguration);
		validator.add(metricConfiguration("lcom4"));
		assertValid();

		validator.remove(cboConfiguration);
		assertInvalid(EcmaError.class);
	}

	private void assertValid() {
		validator.add(configuration);
	}

	private void assertInvalid(Class<? extends Exception> expectedExceptionClass) {
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Exception {
				assertValid();
			}
		}).throwsException().withMessage("Metric with invalid code or script: " + configuration.getMetric())
			.withCause(expectedExceptionClass);
	}
}