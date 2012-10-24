package org.kalibro.service;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.kalibro.MetricConfiguration;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.MetricConfigurationDao;
import org.kalibro.service.xml.MetricConfigurationXmlRequest;
import org.powermock.reflect.Whitebox;

public class MetricConfigurationEndpointTest extends
	EndpointTest<MetricConfiguration, MetricConfigurationDao, MetricConfigurationEndpoint> {

	private static final Long ID = new Random().nextLong();
	private static final Long CONFIGURATION_ID = new Random().nextLong();

	@Override
	protected MetricConfiguration loadFixture() {
		MetricConfiguration metricConfiguration = loadFixture("lcom4", MetricConfiguration.class);
		Whitebox.setInternalState(metricConfiguration, "id", ID);
		return metricConfiguration;
	}

	@Override
	protected List<String> fieldsThatShouldBeProxy() {
		return list("baseTool", "readingGroup", "ranges");
	}

	@Test
	public void shouldGetMetricConfigurationsOfConfiguration() {
		when(dao.metricConfigurationsOf(CONFIGURATION_ID)).thenReturn(sortedSet(entity));
		assertDeepDtoList(list(entity), port.metricConfigurationsOf(CONFIGURATION_ID));
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity, CONFIGURATION_ID)).thenReturn(ID);
		assertEquals(ID, port.saveMetricConfiguration(new MetricConfigurationXmlRequest(entity), CONFIGURATION_ID));
	}

	@Test
	public void shouldDelete() {
		port.deleteMetricConfiguration(ID);
		verify(dao).delete(ID);
	}
}