package org.kalibro.client;

import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.kalibro.BaseTool;
import org.kalibro.service.BaseToolEndpoint;
import org.kalibro.service.xml.BaseToolXml;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(BaseToolClientDao.class)
public class BaseToolClientDaoTest extends ClientTest<BaseTool, BaseToolXml, BaseToolEndpoint, BaseToolClientDao> {

	private static final String NAME = "BaseToolClientDaoTest name";

	@Override
	protected Class<BaseTool> entityClass() {
		return BaseTool.class;
	}

	@Test
	public void shouldGetAllNames() {
		when(port.allBaseToolNames()).thenReturn(list(NAME));
		assertDeepEquals(set(NAME), client.allNames());
	}

	@Test
	public void shouldGetByName() {
		when(port.getBaseTool(NAME)).thenReturn(xml);
		assertSame(entity, client.get(NAME));
	}
}