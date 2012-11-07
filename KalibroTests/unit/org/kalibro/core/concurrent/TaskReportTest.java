package org.kalibro.core.concurrent;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class TaskReportTest extends UnitTest {

	private static final long EXECUTION_TIME = new Random().nextLong();

	private Task<Object> task;
	private Object result;
	private Throwable error;

	private TaskReport<Object> report;

	@Before
	public void setUp() {
		task = mock(Task.class);
		result = mock(Object.class);
		error = mock(Throwable.class);
		report = new TaskReport<Object>(task, System.currentTimeMillis() - EXECUTION_TIME, result);
	}

	@Test
	public void shouldGetTaskWhichGeneratedReport() {
		assertSame(task, report.getTask());
	}

	@Test
	public void shouldGetExecutionTime() {
		assertEquals(EXECUTION_TIME, report.getExecutionTime(), 10);
	}

	@Test
	public void shouldAnswerTaskDone() {
		assertTrue(report.isTaskDone());

		createErrorReport();
		assertFalse(report.isTaskDone());
	}

	@Test
	public void shouldGetResult() {
		assertSame(result, report.getResult());
	}

	@Test
	public void shouldGetError() {
		createErrorReport();
		assertSame(error, report.getError());
	}

	private void createErrorReport() {
		report = new TaskReport<Object>(task, 0, error);
	}
}