package org.kalibro.client;

import org.junit.Test;
import org.kalibro.BaseTool;
import org.kalibro.service.BaseToolEndpoint;
import org.kalibro.service.xml.BaseToolXml;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(BaseToolClientDao.class)
public class BaseToolClientDaoTest extends
	ClientTest<BaseTool, BaseToolXml, BaseToolXml, BaseToolEndpoint, BaseToolClientDao> {

	@Override
	protected Class<BaseTool> entityClass() {
		return BaseTool.class;
	}

	@Test
	public void shouldGetAll() {
		when(port.allBaseTools()).thenReturn(asList(response));
		assertDeepEquals(asSet(entity), client.all());
	}
}