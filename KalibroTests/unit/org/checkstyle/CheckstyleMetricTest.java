package org.checkstyle;

import static org.checkstyle.CheckstyleMetric.*;
import static org.junit.Assert.*;
import static org.kalibro.core.model.enums.Granularity.CLASS;
import static org.kalibro.core.model.enums.Language.JAVA;
import static org.mockito.Matchers.anyString;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.EnumerationTest;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Statistic;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class CheckstyleMetricTest extends EnumerationTest<CheckstyleMetric> {

	@Override
	protected Class<CheckstyleMetric> enumerationClass() {
		return CheckstyleMetric.class;
	}

	private CheckstyleConfiguration configuration;

	@Before
	public void setUp() {
		configuration = PowerMockito.mock(CheckstyleConfiguration.class);
		PowerMockito.when(configuration.getChildByName(anyString())).thenReturn(configuration);
	}

	@Test
	public void checkMessageKeys() {
		for (CheckstyleMetric metric : CheckstyleMetric.values())
			assertSame(metric, getMetricFor(metric.getMessageKey()));
	}

	@Test
	public void checkNativeMetrics() {
		for (CheckstyleMetric metric : CheckstyleMetric.values())
			verifyNativeMetric(metric);
	}

	private void verifyNativeMetric(CheckstyleMetric metric) {
		NativeMetric expected = new NativeMetric("" + metric, CLASS, JAVA);
		expected.setOrigin("Checkstyle");
		assertDeepEquals(expected, metric.getNativeMetric());
	}

	@Test
	public void checkAggregationType() {
		for (CheckstyleMetric metric : CheckstyleMetric.values())
			if (metric.name().startsWith("AVERAGE"))
				assertEquals(Statistic.AVERAGE, metric.getAggregationType());
			else if (metric.getAggregationType() == Statistic.COUNT)
				assertTrue(metric.name().endsWith("S") || metric.name().endsWith("COUNT"));
	}

	@Test
	public void shouldAddToCheckerIfNotTreeWalker() {
		FILE_LENGTH.addToChecker(configuration);
		InOrder order = Mockito.inOrder(configuration);
		order.verify(configuration).getChildByName("FileLength");
		order.verify(configuration).addMessageKey(FILE_LENGTH.getMessageKey());
		order.verify(configuration).addAttributeName("max");
		Mockito.verifyNoMoreInteractions(configuration);
	}

	@Test
	public void shouldAddToTreeWalkerIfTreeWalker() {
		FAN_OUT.addToChecker(configuration);
		InOrder order = Mockito.inOrder(configuration);
		order.verify(configuration).getChildByName("TreeWalker");
		order.verify(configuration).getChildByName("ClassFanOutComplexity");
		order.verify(configuration).addMessageKey(FAN_OUT.getMessageKey());
		order.verify(configuration).addAttributeName("max");
		Mockito.verifyNoMoreInteractions(configuration);
	}
}