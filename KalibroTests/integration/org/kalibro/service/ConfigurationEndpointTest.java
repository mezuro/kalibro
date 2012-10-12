package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.kalibro.Configuration;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.service.xml.ConfigurationXmlRequest;
import org.powermock.reflect.Whitebox;

public class ConfigurationEndpointTest extends EndpointTest<Configuration, ConfigurationDao, ConfigurationEndpoint> {

	private static final Long ID = Math.abs(new Random().nextLong());

	@Override
	public Configuration loadFixture() {
		Configuration configuration = loadFixture("sc", Configuration.class);
		Whitebox.setInternalState(configuration, "id", ID);
		return configuration;
	}

	@Override
	public List<String> fieldsThatShouldBeProxy() {
		return asList("metricConfigurations");
	}

	@Test
	public void shouldConfirmExistence() {
		when(dao.exists(ID)).thenReturn(true);
		assertFalse(port.configurationExists(-1L));
		assertTrue(port.configurationExists(ID));
	}

	@Test
	public void shouldGetById() {
		when(dao.get(ID)).thenReturn(entity);
		assertDeepDtoEquals(entity, port.getConfiguration(ID));
	}

	@Test
	public void shouldGetConfigurationOfRepository() {
		when(dao.configurationOf(ID)).thenReturn(entity);
		assertDeepDtoEquals(entity, port.configurationOf(ID));
	}

	@Test
	public void shouldGetAll() {
		when(dao.all()).thenReturn(asSortedSet(entity));
		assertDeepDtoList(asList(entity), port.allConfigurations());
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity)).thenReturn(ID);
		assertEquals(ID, port.saveConfiguration(new ConfigurationXmlRequest(entity)));
	}

	@Test
	public void shouldDelete() {
		port.deleteConfiguration(ID);
		verify(dao).delete(ID);
	}
}