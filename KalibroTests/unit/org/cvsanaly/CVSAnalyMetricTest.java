package org.cvsanaly;

import static org.cvsanaly.CVSAnalyMetric.*;
import static org.junit.Assert.*;
import static org.kalibro.core.model.enums.Granularity.*;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.powermock.reflect.Whitebox.*;

import org.cvsanaly.entities.MetricResult;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Language;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CVSAnalyMetric.class)
public class CVSAnalyMetricTest extends KalibroTestCase {
	
	@BeforeClass
	public static void emmaCoverage() {
		CVSAnalyMetric.values();
		CVSAnalyMetric.valueOf("NUMBER_OF_LINES_OF_CODE");
	}
	
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
	public void shouldCallCorrectMethodToGetMetricValue() throws Exception {
		MetricResult mockResult = PowerMockito.mock(MetricResult.class);
		CVSAnalyMetric.NUMBER_OF_SOURCE_LINES_OF_CODE.getMetricValue(mockResult);
		Mockito.verify(mockResult).getNumberOfSourceCodeLines();
		
		CVSAnalyMetric.NUMBER_OF_COMMENTS.getMetricValue(mockResult);
		Mockito.verify(mockResult).getNumberOfComments();
	}
	
	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowClassNotFoundExceptionIfMethodCouldNotBeFound() {
		final CVSAnalyMetric type = spy(NUMBER_OF_LINES_OF_CODE);
		checkException(new Task() {

			@Override
			protected void perform() throws Throwable {
				invokeMethod(type, "getMethodFromMethodName", "bla");
			}
		}, ExceptionInInitializerError.class);
	}

}
