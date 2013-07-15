package org.kalibro.core.loaders;

import java.util.List;

import org.junit.Test;
import org.kalibro.KalibroException;
import org.kalibro.core.concurrent.VoidTask;

public class CvsLoaderTest extends RepositoryLoaderTestCase {

	@Override
	protected List<String> expectedValidationCommands() {
		return list("cvs --version");
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return list("cvs -z3 -d " + ADDRESS + " checkout -d . -P .");
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return list("cvs update");
	}

	@Override
	protected String expectedMetadataDirectoryName() {
		return "CVSROOT";
	}

	// FIXME historic processing does not work for CVS

	@Override
	public void shouldReturnToLatestCommit() {
		shouldCatchException();
	}

	@Override
	protected List<String> expectedLatestCommitCommand() {
		throw new KalibroException("Kalibro does not support CVS historic analysis.");
	}

	@Override
	@Test
	public void shouldRollBackOneCommitWhenIsUpdatable() throws Exception {
		shouldCatchException();
	}

	@Override
	@Test
	public void shouldNotRollBackWhenReachedFirstCommit() throws Exception {
		shouldCatchException();
	}

	private void shouldCatchException() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				loader().rollBackOneCommit(true);
			}
		}).throwsException().withMessage("Kalibro does not support CVS historic analysis.");
	}
}