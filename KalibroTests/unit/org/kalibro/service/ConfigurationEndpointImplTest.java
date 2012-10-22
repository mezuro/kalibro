package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Configuration;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.service.xml.ConfigurationXmlRequest;
import org.kalibro.service.xml.ConfigurationXmlResponse;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(ConfigurationEndpointImpl.class)
public class ConfigurationEndpointImplTest extends EndpointImplementorTest
	<Configuration, ConfigurationXmlRequest, ConfigurationXmlResponse, ConfigurationDao, ConfigurationEndpointImpl> {

	private static final Long ID = Math.abs(new Random().nextLong());

	@Override
	protected Class<Configuration> entityClass() {
		return Configuration.class;
	}

	@Test
	public void shouldConfirmExistence() {
		when(dao.exists(ID)).thenReturn(true);
		assertFalse(implementor.configurationExists(-1L));
		assertTrue(implementor.configurationExists(ID));
	}

	@Test
	public void shouldGetById() {
		when(dao.get(ID)).thenReturn(entity);
		assertSame(response, implementor.getConfiguration(ID));
	}

	@Test
	public void shouldGetConfigurationOfProject() {
		when(dao.configurationOf(ID)).thenReturn(entity);
		assertSame(response, implementor.configurationOf(ID));
	}

	@Test
	public void shouldGetAll() {
		when(dao.all()).thenReturn(sortedSet(entity));
		assertDeepEquals(list(response), implementor.allConfigurations());
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity)).thenReturn(ID);
		assertEquals(ID, implementor.saveConfiguration(request));
	}

	@Test
	public void shouldDelete() {
		implementor.deleteConfiguration(ID);
		verify(dao).delete(ID);
	}
}