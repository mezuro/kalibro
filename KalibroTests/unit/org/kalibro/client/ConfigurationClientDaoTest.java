package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;
import org.kalibro.Configuration;
import org.kalibro.service.ConfigurationEndpoint;
import org.kalibro.service.xml.ConfigurationXmlRequest;
import org.kalibro.service.xml.ConfigurationXmlResponse;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(ConfigurationClientDao.class)
public class ConfigurationClientDaoTest extends ClientTest<// @formatter:off
	Configuration, ConfigurationXmlRequest, ConfigurationXmlResponse,
	ConfigurationEndpoint, ConfigurationClientDao> {// @formatter:on

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
	public void shouldGetConfigurationOfProject() {
		when(port.configurationOf(ID)).thenReturn(response);
		assertSame(entity, client.configurationOf(ID));
	}

	@Test
	public void shouldGetAll() {
		when(port.allConfigurations()).thenReturn(Arrays.asList(response));
		assertDeepList(client.all(), entity);
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