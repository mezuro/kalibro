package org.kalibro.core.persistence;

import org.junit.Before;
import org.kalibro.AcceptanceTest;

public abstract class DatabaseTestCase extends AcceptanceTest {

	protected DatabaseDaoFactory daoFactory;

	@Before
	public void createDaoFactory() {
		daoFactory = getDaoFactoryForTest();
	}

	protected abstract DatabaseDaoFactoryForTest getDaoFactoryForTest();
}