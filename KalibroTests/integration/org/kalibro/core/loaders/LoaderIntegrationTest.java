package org.kalibro.core.loaders;

import static org.junit.Assert.*;
import static org.kalibro.core.model.RepositoryFixtures.newHelloWorldRepository;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.IntegrationTest;
import org.kalibro.core.command.FileProcessStreamLogger;
import org.kalibro.core.model.Repository;
import org.kalibro.core.model.enums.RepositoryType;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileProcessStreamLogger.class)
public abstract class LoaderIntegrationTest extends IntegrationTest {

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
	public void tearDown() {
		FileUtils.deleteQuietly(repositoriesDirectory());
	}

	@Test
	public void validateLoaderInstallation() {
		assertTrue(repositoryType.isSupported());
	}

	@Test
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