package org.kalibro.service;

import java.net.MalformedURLException;

import org.analizo.AnalizoStub;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.persistence.dao.BaseToolDao;
import org.kalibro.core.persistence.dao.BaseToolDaoStub;

public class BaseToolEndpointTest extends KalibroServiceTestCase {

	private BaseTool analizo;
	private BaseToolEndpoint port;

	@Before
	public void setUp() throws MalformedURLException {
		analizo = new AnalizoStub().getBaseTool();
		analizo.setCollectorClass(null);
		BaseToolDao daoStub = new BaseToolDaoStub();
		daoStub.save(analizo);
		port = publishAndGetPort(new BaseToolEndpointImpl(daoStub), BaseToolEndpoint.class);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldListBaseToolNames() {
		assertDeepEquals(port.getBaseToolNames(), analizo.getName());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void shouldGetBaseToolByName() {
		assertDeepEquals(analizo, port.getBaseTool(analizo.getName()).convert());
	}
}