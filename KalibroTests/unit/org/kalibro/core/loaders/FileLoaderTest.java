package org.kalibro.core.loaders;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class FileLoaderTest extends UnitTest {

	private FileLoader loader;

	@Before
	public void setUp() throws Exception {
		loader = mockAbstract(FileLoader.class);
	}

	@Test
	public void shouldNotBeUpdatable() {
		assertFalse(loader.isUpdatable(null));
	}
}
