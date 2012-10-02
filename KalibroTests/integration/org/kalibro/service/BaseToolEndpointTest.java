package org.kalibro.service;

import static org.kalibro.BaseToolFixtures.newAnalizoStub;

import org.junit.Test;
import org.kalibro.BaseTool;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.BaseToolDao;

public class BaseToolEndpointTest extends EndpointTest<BaseTool, BaseToolDao, BaseToolEndpoint> {

	@Override
	protected BaseTool loadFixture() {
		BaseTool fixture = newAnalizoStub();
		return fixture;
	}

	@Test
	public void shouldGetAll() {
		when(dao.all()).thenReturn(asSortedSet(entity));
		assertDeepDtoList(port.allBaseTools(), entity);
	}
}