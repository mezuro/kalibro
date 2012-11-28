package org.kalibro.service.xml;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Configuration;
import org.kalibro.dao.MetricConfigurationDao;
import org.kalibro.dto.DaoLazyLoader;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoLazyLoader.class)
public class ConfigurationXmlTest extends XmlTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		Configuration configuration = (Configuration) entity;
		mockStatic(DaoLazyLoader.class);
		when(DaoLazyLoader.createProxy(MetricConfigurationDao.class, "metricConfigurationsOf", configuration.getId()))
			.thenReturn(configuration.getMetricConfigurations());
	}

	@Override
	protected void verifyElements() {
		assertElement("id", Long.class);
		assertElement("name", String.class, true);
		assertElement("description", String.class);
	}

	@Test
	public void metricConfigurationsShouldBeEmptyIfHasNoId() {
		assertTrue(new ConfigurationXml().metricConfigurations().isEmpty());
	}
}