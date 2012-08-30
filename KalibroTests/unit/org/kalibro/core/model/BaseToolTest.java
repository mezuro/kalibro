package org.kalibro.core.model;

import static org.junit.Assert.*;
import static org.kalibro.core.model.BaseToolFixtures.*;

import java.util.Arrays;

import org.analizo.AnalizoStub;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;

public class BaseToolTest extends TestCase {

	private BaseTool analizo;

	@Before
	public void setUp() {
		analizo = newAnalizoStub();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkInitialization() {
		assertEquals("Analizo", analizo.getName());
		assertEquals("", analizo.getDescription());
		assertEquals(AnalizoStub.class, analizo.getCollectorClass());
		assertEquals(analizoStub().getSupportedMetrics(), analizo.getSupportedMetrics());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetOriginOnSupportedMetrics() {
		NativeMetric metric1 = new NativeMetric("Metric 1", Granularity.CLASS, Language.JAVA);
		NativeMetric metric2 = new NativeMetric("Metric 2", Granularity.METHOD, Language.C);
		assertNull(metric1.getOrigin());
		assertNull(metric2.getOrigin());

		analizo.setSupportedMetrics(Arrays.asList(metric1, metric2));
		assertEquals(analizo.getName(), metric1.getOrigin());
		assertEquals(analizo.getName(), metric2.getOrigin());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void toStringShouldBeName() {
		assertEquals("Analizo", "" + analizo);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testCollectorCreation() {
		analizo.createMetricCollector();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkErrorCreatingCollector() {
		analizo.setCollectorClass(null);
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				analizo.createMetricCollector();
			}
		}, "Could not create metric collector of base tool 'Analizo'", NullPointerException.class);
	}
}