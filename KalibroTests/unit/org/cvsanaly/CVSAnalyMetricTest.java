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
@PrepareForTest(CVSAnalyMetric.class)
public class CVSAnalyMetricTest extends EnumerationTest<CVSAnalyMetric> {

	@Override
	protected Class<CVSAnalyMetric> enumerationClass() {
		return CVSAnalyMetric.class;
	}

	@Test
	public void checkNativeMetrics() {
		for (CVSAnalyMetric metric : CVSAnalyMetric.values()) {
			NativeMetric expected = new NativeMetric("" + metric, CLASS, Language.values());
			assertDeepEquals(expected, metric.getNativeMetric());
		}
	}

	@Test
	public void shouldRetrieveCorrectField() {
		MetricResult exampleResult = CVSAnalyStub.getExampleEntities().get(1);
		double actual = CVSAnalyMetric.NUMBER_OF_SOURCE_LINES_OF_CODE.getMetricValue(exampleResult);
		assertDoubleEquals(exampleResult.getNumberOfSourceLinesOfCode(), actual);

		actual = CVSAnalyMetric.NUMBER_OF_COMMENTS.getMetricValue(exampleResult);
		assertDoubleEquals(exampleResult.getNumberOfComments(), actual);
	}
}