package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Configuration;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.service.xml.ConfigurationXml;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(ConfigurationEndpointImpl.class)
public class ConfigurationEndpointImplTest extends
	EndpointImplementorTest<Configuration, ConfigurationXml, ConfigurationDao, ConfigurationEndpointImpl> {

	private static final Long ID = Math.abs(new Random().nextLong());

	@Test
	public void shouldConfirmExistence() {
		when(dao.exists(ID)).thenReturn(true);
		assertFalse(implementor.configurationExists(-1L));
		assertTrue(implementor.configurationExists(ID));
	}

	@Test
	public void shouldGetById() {
		when(dao.get(ID)).thenReturn(entity);
		assertSame(xml, implementor.getConfiguration(ID));
	}

	@Test
	public void shouldGetConfigurationOfRepository() {
		when(dao.configurationOf(ID)).thenReturn(entity);
		assertSame(xml, implementor.configurationOf(ID));
	}

	@Test
	public void shouldGetAll() {
		doReturn(sortedSet(entity)).when(dao).all();
		assertDeepEquals(list(xml), implementor.allConfigurations());
	}

	@Test
	public void shouldSave() {
		when(dao.save(entity)).thenReturn(ID);
		assertEquals(ID, implementor.saveConfiguration(xml));
	}

	@Test
	public void shouldDelete() {
		implementor.deleteConfiguration(ID);
		verify(dao).delete(ID);
	}
}