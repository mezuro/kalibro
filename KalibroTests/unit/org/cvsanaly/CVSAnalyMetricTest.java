package org.cvsanaly;

import static org.cvsanaly.CVSAnalyMetric.*;
import static org.junit.Assert.*;
import static org.kalibro.core.model.enums.Granularity.*;

import org.cvsanaly.entities.MetricResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Language;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class CVSAnalyMetricTest extends KalibroTestCase {
	
	@Test(timeout = UNIT_TIMEOUT)
	public void checkToString() {
		assertEquals("Number of lines of code", "" + NUMBER_OF_LINES_OF_CODE);
		assertEquals("Number of comments", "" + NUMBER_OF_COMMENTS);
	}
	
	@Test(timeout = UNIT_TIMEOUT)
	public void checkNativeMetrics() {
		for (CVSAnalyMetric metric : CVSAnalyMetric.values()) {
			NativeMetric expected = new NativeMetric("" + metric, CLASS, Language.values());
			expected.setOrigin("CVSAnaly");
			assertDeepEquals(expected, metric.getNativeMetric());
		}
	}
	
	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCallCorrectMethodToGetMetricValue() throws ReflectiveOperationException {
		MetricResult mockResult = PowerMockito.mock(MetricResult.class);
		CVSAnalyMetric.NUMBER_OF_SOURCE_LINES_OF_CODE.getMetricValue(mockResult);
		Mockito.verify(mockResult).getNumberOfSourceCodeLines();
		
		CVSAnalyMetric.NUMBER_OF_COMMENTS.getMetricValue(mockResult);
		Mockito.verify(mockResult).getNumberOfComments();
	}
}
