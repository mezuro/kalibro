package org.kalibro.service;

import static org.kalibro.core.model.BaseToolFixtures.newAnalizoStub;

import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.dao.BaseToolDaoFake;
import org.kalibro.core.model.BaseTool;

public class BaseToolEndpointTest extends KalibroServiceTestCase {

	private BaseTool analizo;
	private BaseToolEndpoint port;

	@Before
	public void setUp() throws MalformedURLException {
		analizo = newAnalizoStub();
		analizo.setCollectorClass(null);
		BaseToolDaoFake daoFake = new BaseToolDaoFake();
		daoFake.save(analizo);
		port = publishAndGetPort(new BaseToolEndpointImpl(daoFake), BaseToolEndpoint.class);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldListBaseToolNames() {
		assertDeepList(port.getBaseToolNames(), analizo.getName());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldGetBaseToolByName() {
		assertDeepEquals(analizo, port.getBaseTool(analizo.getName()).convert());
	}
}