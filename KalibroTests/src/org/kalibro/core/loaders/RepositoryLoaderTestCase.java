package org.kalibro.core.loaders;

import static org.junit.Assert.*;

import org.junit.Test;

public abstract class RepositoryLoaderTestCase extends LoaderTestCase {

	@Test
	public void checkMetadataDirectoryName() {
		assertEquals(expectedMetadataDirectoryName(), loader().metadataDirectoryName());
	}

	protected abstract String expectedMetadataDirectoryName();

	private RepositoryLoader loader() {
		return (RepositoryLoader) loader;
	}
}