package org.kalibro.service;

import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.kalibro.BaseTool;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.service.xml.BaseToolXml;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(BaseToolEndpointImpl.class)
public class BaseToolEndpointImplTest extends
	EndpointImplementorTest<BaseTool, BaseToolXml, BaseToolXml, BaseToolDao, BaseToolEndpointImpl> {

	@Override
	protected Class<BaseTool> entityClass() {
		return BaseTool.class;
	}

	@Test
	public void shouldGetAllNames() {
		String name = mock(String.class);
		when(dao.allNames()).thenReturn(asSortedSet(name));
		assertDeepEquals(asList(name), implementor.allBaseToolNames());
	}

	@Test
	public void shouldGetByName() {
		String name = mock(String.class);
		when(dao.get(name)).thenReturn(entity);
		assertSame(response, implementor.getBaseTool(name));
	}
}