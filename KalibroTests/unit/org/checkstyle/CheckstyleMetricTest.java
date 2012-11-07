package org.checkstyle;

import static org.checkstyle.CheckstyleMetric.*;
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

	private CheckstyleMetric fanOut, fileLength;

	@Before
	public void setUp() {
		checker = mock(CheckstyleConfiguration.class);
		treeWalker = mock(CheckstyleConfiguration.class);
		when(checker.getChildByName("TreeWalker")).thenReturn(treeWalker);

		fanOut = loadFixture("fanOut", CheckstyleMetric.class);
		fileLength = loadFixture("fileLength", CheckstyleMetric.class);
	}

	@Test
	public void shouldGetSupportedMetrics() {
		Set<NativeMetric> supportedMetrics = supportedMetrics();
		assertTrue(supportedMetrics.contains(fanOut));
		assertTrue(supportedMetrics.contains(fileLength));
	}

	@Test
	public void allSupportedMetricsShouldHaveJavaFilesAsScope() {
		for (NativeMetric metric : supportedMetrics()) {
			assertEquals(CLASS, metric.getScope());
			assertEquals(set(JAVA), metric.getLanguages());
		}
	}

	@Test
	public void shouldSelectWantedMetrics() {
		Set<NativeMetric> wantedMetrics = set((NativeMetric) fanOut);
		assertDeepEquals(set(fanOut), selectMetrics(wantedMetrics));
	}

	@Test
	public void shouldGetMessageKey() {
		assertEquals("classFanOutComplexity", fanOut.getMessageKey());
		assertEquals("maxLen.file", fileLength.getMessageKey());
	}

	@Test
	public void shouldGetAggregationType() {
		assertEquals(Statistic.SUM, fanOut.getAggregationType());
		assertEquals(Statistic.AVERAGE, fileLength.getAggregationType());
	}

	@Test
	public void shouldAddToCheckerIfNotTreeWalker() {
		CheckstyleConfiguration fileLengthConf = mock(CheckstyleConfiguration.class);
		when(checker.getChildByName("FileLength")).thenReturn(fileLengthConf);

		fileLength.addToChecker(checker);
		verify(fileLengthConf).addMessageKey("maxLen.file");
		verify(fileLengthConf).addAttribute("max");
	}

	@Test
	public void shouldAddToTreeWalkerIfTreeWalker() {
		CheckstyleConfiguration classFanOut = mock(CheckstyleConfiguration.class);
		when(treeWalker.getChildByName("ClassFanOutComplexity")).thenReturn(classFanOut);

		fanOut.addToChecker(checker);
		verify(classFanOut).addMessageKey("classFanOutComplexity");
		verify(classFanOut).addAttribute("max");
	}
}