package org.kalibro.core.loaders;

import static org.junit.Assert.*;
import static org.kalibro.core.Environment.dotKalibro;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.IntegrationTest;

public abstract class RepositoryIntegrationTest extends IntegrationTest {

	protected static File projectsDirectory() {
		return new File(dotKalibro(), "projects");
	}

	protected RepositoryLoader loader;

	@Before
	public void setUp() throws Exception {
		Class<?> loaderClass = Class.forName(getClass().getName().replace("Test", "Loader"));
		loader = (RepositoryLoader) loaderClass.getConstructor().newInstance();
	}

	@After
	public void tearDown() {
		FileUtils.deleteQuietly(projectsDirectory());
	}

	@Test
	public void validateLoader() {
		loader.validate();
	}

	@Test
	public void shouldLoadAndUpdate() {
		File loaded = loadAndCheck();
		File updated = loadAndCheck();
		assertEquals(updated.lastModified(), loaded.lastModified());
	}

	private File loadAndCheck() {
		load();
		Iterator<File> files = FileUtils.iterateFiles(projectsDirectory(), new String[]{"c"}, true);
		File loaded = files.next();
		assertEquals("HelloWorld.c", loaded.getName());
		assertFalse(files.hasNext());
		return loaded;
	}

	protected void load() {
		loader.load(address(), projectsDirectory());
	}

	protected abstract String address();
}