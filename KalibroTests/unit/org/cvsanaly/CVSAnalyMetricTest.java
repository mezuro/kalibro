package org.cvsanaly;

import static org.kalibro.Granularity.CLASS;

import org.cvsanaly.entities.MetricResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Language;
import org.kalibro.NativeMetric;
import org.kalibro.tests.EnumerationTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CvsAnalyMetric.class)
public class CVSAnalyMetricTest extends EnumerationTest<CvsAnalyMetric> {

	@Override
	protected Class<CvsAnalyMetric> enumerationClass() {
		return CvsAnalyMetric.class;
	}

	@Test
	public void checkNativeMetrics() {
		for (CvsAnalyMetric metric : CvsAnalyMetric.values()) {
			NativeMetric expected = new NativeMetric("" + metric, CLASS, Language.values());
			assertDeepEquals(expected, metric.getNativeMetric());
		}
	}

	@Test
	public void shouldRetrieveCorrectField() {
		MetricResult exampleResult = CvsAnalyStub.getExampleEntities().get(1);
		double actual = CvsAnalyMetric.NUMBER_OF_SOURCE_LINES_OF_CODE.getMetricValue(exampleResult);
		assertDoubleEquals(exampleResult.getNumberOfSourceLinesOfCode(), actual);

		actual = CvsAnalyMetric.NUMBER_OF_COMMENTS.getMetricValue(exampleResult);
		assertDoubleEquals(exampleResult.getNumberOfComments(), actual);
	}
}