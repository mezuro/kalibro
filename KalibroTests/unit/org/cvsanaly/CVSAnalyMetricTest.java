package org.cvsanaly;

import static org.cvsanaly.CVSAnalyMetric.*;
import static org.junit.Assert.*;
import static org.kalibro.core.model.enums.Granularity.*;

import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Language;


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
}
