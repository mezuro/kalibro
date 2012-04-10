package org.cvsanaly;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;


public class CVSAnalyMetricCollectorTest extends KalibroTestCase {
	private CVSAnalyMetricCollector cvsanaly;

	@Before
	public void setUp() {
		cvsanaly = new CVSAnalyMetricCollector();
	}
	@Test(timeout = UNIT_TIMEOUT)
	public void checkBaseTool() {
		assertDeepEquals(CVSAnalyStub.getBaseTool(), cvsanaly.getBaseTool());
	}
}
