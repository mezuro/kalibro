package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.kalibro.Configuration;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.service.xml.ConfigurationXml;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(ConfigurationEndpointImpl.class)
public class ConfigurationEndpointImplTest extends EndpointImplementorTest
	<Configuration, ConfigurationXml, ConfigurationXml, ConfigurationDao, ConfigurationEndpointImpl> {

	@Override
	protected Class<Configuration> entityClass() {
		return Configuration.class;
	}

	@Test
	public void testSaveConfiguration() {
		implementor.saveConfiguration(request);
		verify(dao).save(entity);
	}

	@Test
	public void testGetConfigurationNames() {
		List<String> names = mock(List.class);
		when(dao.getConfigurationNames()).thenReturn(names);
		assertSame(names, implementor.getConfigurationNames());
	}

	@Test
	public void testConfirmConfiguration() {
		when(dao.hasConfiguration("42")).thenReturn(true);
		assertTrue(implementor.hasConfiguration("42"));

		when(dao.hasConfiguration("42")).thenReturn(false);
		assertFalse(implementor.hasConfiguration("42"));
	}

	@Test
	public void testGetConfiguration() {
		when(dao.getConfiguration("42")).thenReturn(entity);
		assertSame(response, implementor.getConfiguration("42"));
	}

	@Test
	public void testRemoveConfiguration() {
		implementor.deleteConfiguration(42L);
		verify(dao).delete(42L);
	}
}