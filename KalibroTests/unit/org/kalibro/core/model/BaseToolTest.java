package org.kalibro.core.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.analizo.AnalizoMetricCollector;
import org.analizo.AnalizoStub;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.MetricCollector;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

public class BaseToolTest extends KalibroTestCase {

	private BaseTool analizo;

	@Before
	public void setUp() {
		analizo = BaseToolFixtures.analizoStub();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkInitialization() {
		assertEquals("Analizo", analizo.getName());
		assertEquals("", analizo.getDescription());
		assertEquals(AnalizoStub.class, analizo.getCollectorClass());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetSupportedMetricsFromMetricCollector() {
		Whitebox.setInternalState(analizo, "supportedMetrics", (Set<NativeMetric>) null);

		analizo = PowerMockito.spy(analizo);
		MetricCollector metricCollector = PowerMockito.mock(MetricCollector.class);
		Set<NativeMetric> supportedMetrics = new HashSet<NativeMetric>();
		PowerMockito.doReturn(metricCollector).when(analizo).createMetricCollector();
		PowerMockito.when(metricCollector.getSupportedMetrics()).thenReturn(supportedMetrics);

		assertSame(supportedMetrics, analizo.getSupportedMetrics());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetOriginOnSupportedMetrics() {
		NativeMetric nativeMetric = new NativeMetric("My metric", Granularity.CLASS, Language.JAVA);
		assertNull(nativeMetric.getOrigin());

		analizo.setSupportedMetrics(new HashSet<NativeMetric>(Arrays.asList(nativeMetric)));
		assertEquals(analizo.getName(), nativeMetric.getOrigin());
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

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetCollectorClassByName() {
		analizo.setCollectorClassName("org.analizo.AnalizoMetricCollector");
		assertEquals(AnalizoMetricCollector.class, analizo.getCollectorClass());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkErrorSettingCollectorByName() {
		checkKalibroError(new Task() {

			@Override
			public void perform() {
				analizo.setCollectorClassName("");
			}
		}, "Could not find metric collector class of base tool 'Analizo'", ClassNotFoundException.class);
	}
}