package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

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

	private List<String> expectedRollBackCommands(int revision) {
		return Arrays.asList("cvs checkout -r " + (revision - 1));
	}

	@Override
	@Test
	public void shouldRollBackOneCommitWhenIsUpdatable() throws Exception {
		assertDeepEquals(expectedRollBackCommands(1), loader().rollBackOneCommit(true));
	}
}