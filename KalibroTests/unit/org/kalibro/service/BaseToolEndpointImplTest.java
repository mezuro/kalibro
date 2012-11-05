package org.kalibro.service;

import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.kalibro.BaseTool;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.service.xml.BaseToolXml;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(BaseToolEndpointImpl.class)
public class BaseToolEndpointImplTest extends
	EndpointImplementorTest<BaseTool, BaseToolXml, BaseToolDao, BaseToolEndpointImpl> {

	private static final String NAME = "BaseToolEndpointImplTest name";

	@Test
	public void shouldGetAllNames() {
		when(dao.allNames()).thenReturn(sortedSet(NAME));
		assertDeepEquals(list(NAME), implementor.allBaseToolNames());
	}

	@Test
	public void shouldGetByName() {
		when(dao.get(NAME)).thenReturn(entity);
		assertSame(xml, implementor.getBaseTool(NAME));
	}
}