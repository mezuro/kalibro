package org.kalibro.service;

import org.junit.Test;
import org.kalibro.BaseTool;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.BaseToolDao;

public class BaseToolEndpointTest extends EndpointTest<BaseTool, BaseToolDao, BaseToolEndpoint> {

	private static final String NAME = mock(String.class);

	@Override
	protected BaseTool loadFixture() {
		return loadFixture("inexistent", BaseTool.class);
	}

	@Test
	public void shouldGetAllNames() {
		when(dao.allNames()).thenReturn(sortedSet(NAME));
		assertDeepEquals(list(NAME), port.allBaseToolNames());
	}

	@Test
	public void shouldGetByName() {
		when(dao.get(NAME)).thenReturn(entity);
		assertDeepDtoEquals(entity, port.getBaseTool(NAME));
	}
}