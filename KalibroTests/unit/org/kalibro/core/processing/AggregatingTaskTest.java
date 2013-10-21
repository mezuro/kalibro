package org.kalibro.core.processing;

import java.util.Random;

import org.junit.Test;
import org.kalibro.Processing;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;
import org.kalibro.tests.UnitTest;

public class AggregatingTaskTest extends UnitTest {

	private Processing processing;
	private ModuleResultDatabaseDao moduleResultDao;

	@Test
	public void shouldAggregateResults() {
		processing = mock(Processing.class);
		moduleResultDao = mock(ModuleResultDatabaseDao.class);
		new AggregatingTask(mockContext()).perform();

		verify(moduleResultDao).aggregateResults(processing.getId());
	}

	private ProcessContext mockContext() {
		ProcessContext context = new ProcessContext(null);
		context.processing = processing;
		context.moduleResultDao = moduleResultDao;
		when(processing.getId()).thenReturn(new Random().nextLong());
		return context;
	}
}