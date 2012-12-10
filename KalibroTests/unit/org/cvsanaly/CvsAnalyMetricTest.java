package org.cvsanaly;

import static org.cvsanaly.CvsAnalyMetric.*;
import static org.junit.Assert.*;
import static org.kalibro.Granularity.CLASS;
import static org.kalibro.Language.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.NativeMetric;
import org.kalibro.tests.UnitTest;

public class CvsAnalyMetricTest extends UnitTest {

	private CvsAnalyMetric loc, nfunctions;

	@Before
	public void setUp() {
		loc = loadFixture("loc", CvsAnalyMetric.class);
		nfunctions = loadFixture("nfunctions", CvsAnalyMetric.class);
	}

	@Test
	public void shouldGetSupportedMetrics() {
		Set<NativeMetric> supportedMetrics = supportedMetrics();
		assertTrue(supportedMetrics.contains(loc));
		assertTrue(supportedMetrics.contains(nfunctions));
	}

	@Test
	public void allSupportedMetricsShouldHaveFileScope() {
		for (NativeMetric metric : supportedMetrics()) {
			assertEquals(CLASS, metric.getScope());
			assertEquals(set(C, CPP, JAVA, PYTHON), metric.getLanguages());
		}
	}

	@Test
	public void shouldSelectWantedMetrics() {
		Set<NativeMetric> wantedMetrics = set((NativeMetric) loc);
		assertDeepEquals(set(loc), selectMetrics(wantedMetrics));
	}

	@Test
	public void shouldGetColumn() {
		assertEquals("loc", loc.getColumn());
		assertEquals("nfunctions", nfunctions.getColumn());
	}
}