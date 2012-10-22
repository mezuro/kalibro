package org.checkstyle;

import static org.junit.Assert.*;
import static org.kalibro.Granularity.CLASS;
import static org.kalibro.Language.JAVA;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.NativeMetric;
import org.kalibro.Statistic;
import org.kalibro.tests.UnitTest;

public class CheckstyleMetricTest extends UnitTest {

	private CheckstyleConfiguration checker, treeWalker;

	@Before
	public void setUp() {
		checker = mock(CheckstyleConfiguration.class);
		treeWalker = mock(CheckstyleConfiguration.class);
		when(checker.getChildByName("TreeWalker")).thenReturn(treeWalker);
	}

	@Test
	public void shouldGetSupportedMetrics() {
		Set<CheckstyleMetric> supportedMetrics = CheckstyleMetric.supportedMetrics();
		assertTrue(supportedMetrics.contains(new NativeMetric("Average Method Length", CLASS, JAVA)));
		assertTrue(supportedMetrics.contains(new NativeMetric("File Length", CLASS, JAVA)));
		assertTrue(supportedMetrics.contains(new NativeMetric("Number of Methods", CLASS, JAVA)));
	}

	@Test
	public void allMetricsShouldHaveJavaFilesAsScope() {
		for (CheckstyleMetric metric : CheckstyleMetric.supportedMetrics()) {
			assertEquals(CLASS, metric.getScope());
			assertEquals(set(JAVA), metric.getLanguages());
		}
	}

	@Test
	public void shouldGetMetricByMessageKey() {
		CheckstyleMetric magicNumberCount = CheckstyleMetric.metricFor("magic.number");
		assertEquals("magic.number", magicNumberCount.getMessageKey());
		assertEquals("Magic Number Count", magicNumberCount.getName());
		assertEquals("A magic number is a numeric literal that is not defined as a constant. By default, " +
			"-1, 0, 1, and 2 are not considered to be magic numbers.\n", magicNumberCount.getDescription());
		assertEquals(Statistic.COUNT, magicNumberCount.getAggregationType());
	}

	@Test
	public void shouldAddToCheckerIfNotTreeWalker() {
		CheckstyleConfiguration fileLength = mock(CheckstyleConfiguration.class);
		when(checker.getChildByName("FileLength")).thenReturn(fileLength);

		CheckstyleMetric metric = CheckstyleMetric.metricFor("maxLen.file");
		metric.addToChecker(checker);
		verify(fileLength).addMessageKey("maxLen.file");
		verify(fileLength).addAttribute("max");
	}

	@Test
	public void shouldAddToTreeWalkerIfTreeWalker() {
		CheckstyleConfiguration fanOut = mock(CheckstyleConfiguration.class);
		when(treeWalker.getChildByName("ClassFanOutComplexity")).thenReturn(fanOut);

		CheckstyleMetric metric = CheckstyleMetric.metricFor("classFanOutComplexity");
		metric.addToChecker(checker);
		verify(fanOut).addMessageKey("classFanOutComplexity");
		verify(fanOut).addAttribute("max");
	}
}