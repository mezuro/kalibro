package org.kalibro.service.xml;

import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.BaseTool;
import org.kalibro.MetricConfiguration;
import org.kalibro.Statistic;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.dto.DaoLazyLoader;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoLazyLoader.class)
public class MetricConfigurationXmlResponseTest extends XmlTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		BaseTool baseTool = loadFixture("inexistent", BaseTool.class);
		mockStatic(DaoLazyLoader.class);
		when(DaoLazyLoader.createProxy(BaseToolDao.class, "get", "Inexistent")).thenReturn(baseTool);
	}

	@Override
	protected void verifyElements() {
		assertElement("id", Long.class);
		assertElement("code", String.class);
		assertElement("metric", MetricXmlResponse.class);
		assertElement("baseToolName", String.class);
		assertElement("weight", Double.class);
		assertElement("aggregationForm", Statistic.class);
	}

	@Test
	public void shouldReturnNullBaseToolForCompoundMetricConfiguration() {
		assertNull(new MetricConfigurationXmlResponse(new MetricConfiguration()).baseTool());
	}
}