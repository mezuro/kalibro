package org.kalibro.client.dao;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.client.EndpointClient;
import org.kalibro.client.MetricConfigurationPortDao;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.service.MetricConfigurationEndpoint;
import org.kalibro.service.entities.MetricConfigurationXml;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MetricConfigurationPortDao.class, EndpointClient.class})
public class MetricConfigurationPortDaoTest extends TestCase {

	private static final String CONFIGURATION_NAME = "MetricConfigurationPortDaoTest";

	private MetricConfiguration configuration;
	private MetricConfigurationXml configurationXml;

	private MetricConfigurationPortDao dao;
	private MetricConfigurationEndpoint port;

	@Before
	public void setUp() throws Exception {
		mockProject();
		createSupressedDao();
	}

	private void mockProject() throws Exception {
		configuration = mock(MetricConfiguration.class);
		configurationXml = mock(MetricConfigurationXml.class);
		whenNew(MetricConfigurationXml.class).withArguments(configuration).thenReturn(configurationXml);
		when(configurationXml.convert()).thenReturn(configuration);
	}

	private void createSupressedDao() {
		suppress(constructor(EndpointClient.class, String.class, Class.class));
		dao = new MetricConfigurationPortDao("");

		port = mock(MetricConfigurationEndpoint.class);
		Whitebox.setInternalState(dao, "port", port);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSave() {
		dao.save(configuration, CONFIGURATION_NAME);
		verify(port).saveMetricConfiguration(same(configurationXml), eq(CONFIGURATION_NAME));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetMetricConfiguration() {
		when(port.getMetricConfiguration("", CONFIGURATION_NAME)).thenReturn(configurationXml);
		assertSame(configuration, dao.getMetricConfiguration("", CONFIGURATION_NAME));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testRemoveMetricConfiguration() {
		dao.removeMetricConfiguration("", CONFIGURATION_NAME);
		verify(port).removeMetricConfiguration("", CONFIGURATION_NAME);
	}
}