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
		fixture.setCollectorClass(null);
		return fixture;
	}

	@Test
	public void shouldListBaseToolNames() {
		when(dao.getBaseToolNames()).thenReturn(asList("42"));
		assertDeepEquals(asList("42"), port.getBaseToolNames());
	}

	@Test
	public void shouldGetBaseToolByName() {
		when(dao.getBaseTool("42")).thenReturn(entity);
		assertDeepDtoEquals(entity, port.getBaseTool("42"));
	}
}