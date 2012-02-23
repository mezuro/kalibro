package org.kalibro.core.persistence.database;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.util.Directories;

public abstract class DatabaseTestCase extends KalibroTestCase {

	protected DatabaseDaoFactory daoFactory;

	@Before
	public void createDaoFactory() {
		new File(Directories.kalibro(), ".seeded").delete();
		daoFactory = new DatabaseDaoFactory(getTestSettings());
	}

	@After
	public void deleteDaoFactory() throws Throwable {
		daoFactory.finalize();
	}

	protected abstract DatabaseTestSettings getTestSettings();
}