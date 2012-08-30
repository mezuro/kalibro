package org.kalibro.client.dao;

import static org.mockito.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.client.EndpointPortFactory;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.model.MetricConfigurationFixtures;
import org.kalibro.service.MetricConfigurationEndpoint;
import org.kalibro.service.entities.MetricConfigurationXml;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EndpointPortFactory.class)
public class MetricConfigurationPortDaoTest extends TestCase {

	private static final String CONFIGURATION_NAME = "MetricConfigurationPortDaoTest";

	private MetricConfiguration configuration;

	private MetricConfigurationPortDao dao;
	private MetricConfigurationEndpoint port;

	@Before
	public void setUp() {
		mockPort();
		dao = new MetricConfigurationPortDao();
		configuration = MetricConfigurationFixtures.metricConfiguration("cbo");
	}

	private void mockPort() {
		port = PowerMockito.mock(MetricConfigurationEndpoint.class);
		PowerMockito.mockStatic(EndpointPortFactory.class);
		PowerMockito.when(EndpointPortFactory.getEndpointPort(MetricConfigurationEndpoint.class)).thenReturn(port);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSave() {
		dao.save(configuration, CONFIGURATION_NAME);

		ArgumentCaptor<MetricConfigurationXml> captor = ArgumentCaptor.forClass(MetricConfigurationXml.class);
		Mockito.verify(port).saveMetricConfiguration(captor.capture(), eq(CONFIGURATION_NAME));
		assertDeepEquals(configuration, captor.getValue().convert());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetMetricConfiguration() {
		MetricConfigurationXml xml = new MetricConfigurationXml(configuration);
		PowerMockito.when(port.getMetricConfiguration("", CONFIGURATION_NAME)).thenReturn(xml);
		assertDeepEquals(configuration, dao.getMetricConfiguration("", CONFIGURATION_NAME));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testRemoveMetricConfiguration() {
		dao.removeMetricConfiguration("", CONFIGURATION_NAME);
		Mockito.verify(port).removeMetricConfiguration("", CONFIGURATION_NAME);
	}
}