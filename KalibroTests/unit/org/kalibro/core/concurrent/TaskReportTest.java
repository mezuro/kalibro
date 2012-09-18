package org.kalibro.core.concurrent;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;

public class TaskReportTest extends TestCase {

	private static final long EXECUTION_TIME = new Random().nextLong();

	private Task task;
	private Object result;
	private Throwable error;

	private TaskReport<Object> report;

	@Before
	public void setUp() {
		task = mock(Task.class);
		result = mock(Object.class);
		error = mock(Throwable.class);
		report = new TaskReport<Object>(task, EXECUTION_TIME, result, error);
	}

	@Test
	public void shouldGetTaskWhichGeneratedReport() {
		assertSame(task, report.getTask());
	}

	@Test
	public void shouldGetExecutionTime() {
		assertEquals(EXECUTION_TIME, report.getExecutionTime());
	}

	@Test
	public void shouldAnswerTaskDone() {
		assertFalse(report.isTaskDone());

		report = new TaskReport<Object>(task, EXECUTION_TIME, result, null);
		assertTrue(report.isTaskDone());
	}

	@Test
	public void shouldGetResult() {
		assertSame(result, report.getResult());
	}

	@Test
	public void shouldGetError() {
		assertSame(error, report.getError());
	}
}