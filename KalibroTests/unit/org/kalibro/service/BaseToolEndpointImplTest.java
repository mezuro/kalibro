package org.kalibro.service;

import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Test;
import org.kalibro.core.model.BaseTool;
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
	public void testGetBaseToolNames() {
		List<String> names = mock(List.class);
		when(dao.getBaseToolNames()).thenReturn(names);
		assertSame(names, implementor.getBaseToolNames());
	}

	@Test
	public void testGetBaseTool() {
		when(dao.getBaseTool("42")).thenReturn(entity);
		assertSame(response, implementor.getBaseTool("42"));
	}
}