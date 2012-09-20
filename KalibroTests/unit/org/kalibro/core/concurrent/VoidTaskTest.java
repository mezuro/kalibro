package org.kalibro.core.concurrent;

import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.kalibro.TestCase;

public class VoidTaskTest extends TestCase {

	@Test
	public void shouldSparePerformOfReturnClauseAndComputeNull() throws Throwable {
		VoidTask task = new VoidTask() {

			@Override
			protected void perform() {
				assert true;
			}
		};
		assertNull(task.compute());
	}
}