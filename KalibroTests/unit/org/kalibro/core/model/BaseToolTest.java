package org.kalibro.core.model;

import static org.junit.Assert.*;
import static org.kalibro.core.model.BaseToolFixtures.*;

import java.util.Arrays;

import org.analizo.AnalizoStub;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;

public class BaseToolTest extends TestCase {

	private BaseTool analizo;

	@Before
	public void setUp() {
		analizo = newAnalizoStub();
	}

	@Test
	public void checkInitialization() {
		assertEquals("Analizo", analizo.getName());
		assertEquals("", analizo.getDescription());
		assertEquals(AnalizoStub.class, analizo.getCollectorClass());
		assertEquals(analizoStub().getSupportedMetrics(), analizo.getSupportedMetrics());
	}

	@Test
	public void shouldSetOriginOnSupportedMetrics() {
		NativeMetric metric1 = new NativeMetric("Metric 1", Granularity.CLASS, Language.JAVA);
		NativeMetric metric2 = new NativeMetric("Metric 2", Granularity.METHOD, Language.C);
		assertNull(metric1.getOrigin());
		assertNull(metric2.getOrigin());

		analizo.setSupportedMetrics(Arrays.asList(metric1, metric2));
		assertEquals(analizo.getName(), metric1.getOrigin());
		assertEquals(analizo.getName(), metric2.getOrigin());
	}

	@Test
	public void toStringShouldBeName() {
		assertEquals("Analizo", "" + analizo);
	}

	@Test
	public void testCollectorCreation() {
		analizo.createMetricCollector();
	}

	@Test
	public void checkErrorCreatingCollector() {
		analizo.setCollectorClass(null);
		assertThat(new VoidTask() {

			@Override
			public void perform() {
				analizo.createMetricCollector();
			}
		}).throwsException().withMessage("Could not create metric collector of base tool 'Analizo'")
			.withCause(NullPointerException.class);
	}
}