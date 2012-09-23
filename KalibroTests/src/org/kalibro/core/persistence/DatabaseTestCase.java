package org.kalibro.core.persistence;

import org.junit.Before;
import org.kalibro.tests.IntegrationTest;

public abstract class DatabaseTestCase extends IntegrationTest {

	protected DatabaseDaoFactory daoFactory;

	@Before
	public void createDaoFactory() {
		daoFactory = getDaoFactoryForTest();
	}

	protected abstract DatabaseDaoFactoryForTest getDaoFactoryForTest();
}