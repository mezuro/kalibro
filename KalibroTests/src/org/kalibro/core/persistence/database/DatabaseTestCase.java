package org.kalibro.core.persistence.database;

import org.junit.After;
import org.junit.Before;
import org.kalibro.KalibroTestCase;

public abstract class DatabaseTestCase extends KalibroTestCase {

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