package org.kalibro.core.processing;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.Repository;
import org.kalibro.tests.UnitTest;

public class ProcessContextTest extends UnitTest {

	@Test
	public void shouldInitializeRepository() {
		Repository repository = mock(Repository.class);
		ProcessContext context = new ProcessContext(repository);
		assertSame(repository, context.repository);
	}
}