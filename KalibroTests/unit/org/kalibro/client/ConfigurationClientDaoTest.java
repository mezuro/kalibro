package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Configuration;
import org.kalibro.service.ConfigurationEndpoint;
import org.kalibro.service.xml.ConfigurationXml;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(ConfigurationClientDao.class)
public class ConfigurationClientDaoTest extends
	ClientTest<Configuration, ConfigurationXml, ConfigurationXml, ConfigurationEndpoint, ConfigurationClientDao> {

	private static final Long ID = Math.abs(new Random().nextLong());

	@Override
	protected Class<Configuration> entityClass() {
		return Configuration.class;
	}

	@Test
	public void shouldConfirmExistence() {
		when(port.configurationExists(ID)).thenReturn(true);
		assertFalse(client.exists(-1L));
		assertTrue(client.exists(ID));
	}

	@Test
	public void shouldGetById() {
		when(port.getConfiguration(ID)).thenReturn(response);
		assertSame(entity, client.get(ID));
	}

	@Test
	public void shouldGetConfigurationOfRepository() {
		when(port.configurationOf(ID)).thenReturn(response);
		assertSame(entity, client.configurationOf(ID));
	}

	@Test
	public void shouldGetAll() {
		when(port.allConfigurations()).thenReturn(list(response));
		assertDeepEquals(set(entity), client.all());
	}

	@Test
	public void shouldSave() {
		when(port.saveConfiguration(request)).thenReturn(ID);
		assertEquals(ID, client.save(entity));
	}

	@Test
	public void shouldDelete() {
		client.delete(ID);
		verify(port).deleteConfiguration(ID);
	}
}