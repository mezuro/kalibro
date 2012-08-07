package org.cvsanaly;

import static org.cvsanaly.CVSAnalyMetric.*;
import static org.junit.Assert.*;
import static org.kalibro.core.model.enums.Granularity.*;

import org.cvsanaly.entities.MetricResult;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Language;
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
	public void shouldRetrieveCorrectField() {
		MetricResult exampleResult = CVSAnalyStub.getExampleEntities().get(1);
		double actual = CVSAnalyMetric.NUMBER_OF_SOURCE_LINES_OF_CODE.getMetricValue(exampleResult);
		assertDoubleEquals(exampleResult.getNumberOfSourceLinesOfCode(), actual);
		
		actual = CVSAnalyMetric.NUMBER_OF_COMMENTS.getMetricValue(exampleResult);
		assertDoubleEquals(exampleResult.getNumberOfComments(), actual);
	}
	
}
