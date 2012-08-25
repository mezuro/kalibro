package org.kalibro.core.loaders;

import static org.junit.Assert.*;
import static org.kalibro.core.model.RepositoryFixtures.*;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.command.FileProcessStreamLogger;
import org.kalibro.core.model.Repository;
import org.kalibro.core.model.enums.RepositoryType;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileProcessStreamLogger.class)
public abstract class LoaderIntegrationTest extends TestCase {

	protected RepositoryType repositoryType;
	protected Repository repository;

	@Before
	public void setUp() {
		repositoryType = getRepositoryType();
		repository = newHelloWorldRepository(repositoryType);
		if (repositoryType.supportsAuthentication()) {
			repository.setUsername("USERNAME");
			repository.setPassword("PASSWORD");
		}
		MemberModifier.suppress(FileProcessStreamLogger.class.getMethods());
	}

	protected abstract RepositoryType getRepositoryType();

	@After
	public void tearDown() throws IOException {
		FileUtils.cleanDirectory(projectsDirectory());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void validateLoaderInstallation() {
		assertTrue(repositoryType.isSupported());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testLoad() {
		File loaded = load();
		File updated = load();
		assertEquals(updated.lastModified(), loaded.lastModified());
	}

	protected File load() {
		repository.load(helloWorldDirectory());
		Iterator<File> files = FileUtils.iterateFiles(helloWorldDirectory(), new String[]{"c"}, true);
		File loaded = files.next();
		assertEquals("HelloWorld.c", loaded.getName());
		assertFalse(files.hasNext());
		return loaded;
	}
}