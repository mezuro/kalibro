package org.kalibro.service;

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
	public void shouldGetAll() {
		when(dao.all()).thenReturn(asSortedSet(entity));
		assertDeepEquals(asList(response), implementor.allBaseTools());
	}
}