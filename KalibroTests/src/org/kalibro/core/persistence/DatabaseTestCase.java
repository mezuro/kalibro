package org.kalibro.core.persistence;

import org.junit.After;
import org.junit.Before;
import org.kalibro.AcceptanceTest;

public abstract class DatabaseTestCase extends AcceptanceTest {

	protected DatabaseDaoFactory daoFactory;

	@Before
	public void createDaoFactory() {
		daoFactory = getDaoFactoryForTest();
	}

	@After
	public void deleteDaoFactory() throws Throwable {
		daoFactory.finalize();
	}

	protected abstract DatabaseDaoFactoryForTest getDaoFactoryForTest();
}