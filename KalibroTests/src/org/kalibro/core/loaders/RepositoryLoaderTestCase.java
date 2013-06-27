package org.kalibro.core.loaders;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.kalibro.core.concurrent.VoidTask;

public abstract class RepositoryLoaderTestCase extends LoaderTestCase {

	@Test
	public void checkMetadataDirectoryName() {
		assertEquals(expectedMetadataDirectoryName(), loader().metadataDirectoryName());
	}

	protected abstract String expectedMetadataDirectoryName();

	protected abstract List<String> expectedLatestCommitCommand();

	protected RepositoryLoader loader() {
		return (RepositoryLoader) loader;
	}

	@Test
	public void shouldNotRollBackOneCommitWhenIsNotUpdatable() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				loader().rollBackOneCommit(false);
			}
		}).throwsException().withMessage("Repository content has not been loaded yet.");
	}

	@Test
	public abstract void shouldRollBackOneCommitWhenIsUpdatable() throws Exception;

	@Test
	public abstract void shouldNotRollBackWhenReachedFirstCommit() throws Exception;

	@Test
	public void shouldReturnToLatestCommit() {
		assertDeepEquals(expectedLatestCommitCommand(), loader().returnToLatestCommit());
	}
}