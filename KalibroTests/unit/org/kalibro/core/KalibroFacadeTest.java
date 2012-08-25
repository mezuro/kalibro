package org.kalibro.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.persistence.dao.DaoFactory;
import org.powermock.api.mockito.PowerMockito;

public class KalibroFacadeTest extends TestCase {

	private DaoFactory factory;

	private KalibroFacade facade;

	@Before
	public void setUp() {
		factory = PowerMockito.mock(DaoFactory.class);
		facade = new FacadeStub(factory);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateDaoFactory() {
		assertSame(factory, facade.getDaoFactory());
	}
}