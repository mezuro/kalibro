package org.kalibro.service;

import org.junit.Test;
import org.kalibro.BaseTool;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.BaseToolDao;

public class BaseToolEndpointTest extends EndpointTest<BaseTool, BaseToolDao, BaseToolEndpoint> {

	@Override
	protected BaseTool loadFixture() {
		return loadFixture("inexistent", BaseTool.class);
	}

	@Test
	public void shouldGetAll() {
		when(dao.all()).thenReturn(asSortedSet(entity));
		assertDeepDtoList(port.allBaseTools(), entity);
	}
}