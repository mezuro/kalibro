package org.kalibro.service;

import static org.kalibro.core.model.BaseToolFixtures.newAnalizoStub;

import java.util.Arrays;

import org.junit.Test;
import org.kalibro.core.model.BaseTool;
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
		when(dao.getBaseToolNames()).thenReturn(Arrays.asList("42"));
		assertDeepList(port.getBaseToolNames(), "42");
	}

	@Test
	public void shouldGetBaseToolByName() {
		when(dao.getBaseTool("42")).thenReturn(entity);
		assertDeepDtoEquals(entity, port.getBaseTool("42"));
	}
}