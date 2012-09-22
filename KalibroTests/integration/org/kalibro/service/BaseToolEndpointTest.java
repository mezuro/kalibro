package org.kalibro.service;

import static org.kalibro.core.model.BaseToolFixtures.newAnalizoStub;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.model.BaseTool;
import org.kalibro.dao.BaseToolDaoFake;

public class BaseToolEndpointTest extends OldEndpointTest {

	private BaseTool analizo;
	private BaseToolEndpoint port;

	@Before
	public void setUp() {
		analizo = newAnalizoStub();
		analizo.setCollectorClass(null);
		BaseToolDaoFake daoFake = new BaseToolDaoFake();
		daoFake.save(analizo);
		port = publishAndGetPort(new BaseToolEndpointImpl(daoFake), BaseToolEndpoint.class);
	}

	@Test
	public void shouldListBaseToolNames() {
		assertDeepList(port.getBaseToolNames(), analizo.getName());
	}

	@Test
	public void shouldGetBaseToolByName() {
		assertDeepEquals(analizo, port.getBaseTool(analizo.getName()).convert());
	}
}