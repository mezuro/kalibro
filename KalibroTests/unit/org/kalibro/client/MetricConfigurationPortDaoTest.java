package org.kalibro.client;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.service.MetricConfigurationEndpoint;
import org.kalibro.service.entities.MetricConfigurationXml;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MetricConfigurationClientDao.class, EndpointClient.class})
public class MetricConfigurationPortDaoTest extends TestCase {

	private static final String CONFIGURATION_NAME = "MetricConfigurationPortDaoTest";

	private MetricConfiguration configuration;
	private MetricConfigurationXml configurationXml;

	private MetricConfigurationClientDao dao;
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
		dao = new MetricConfigurationClientDao("");

		port = mock(MetricConfigurationEndpoint.class);
		Whitebox.setInternalState(dao, "port", port);
	}

	@Test
	public void testSave() {
		dao.save(configuration, CONFIGURATION_NAME);
		verify(port).saveMetricConfiguration(same(configurationXml), eq(CONFIGURATION_NAME));
	}

	@Test
	public void testGetMetricConfiguration() {
		when(port.getMetricConfiguration("", CONFIGURATION_NAME)).thenReturn(configurationXml);
		assertSame(configuration, dao.getMetricConfiguration("", CONFIGURATION_NAME));
	}

	@Test
	public void testRemoveMetricConfiguration() {
		dao.removeMetricConfiguration("", CONFIGURATION_NAME);
		verify(port).removeMetricConfiguration("", CONFIGURATION_NAME);
	}
}