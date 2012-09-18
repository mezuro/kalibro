package org.kalibro.core.concurrent;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;

public class TaskReportTest extends TestCase {

	private static final long EXECUTION_TIME = new Random().nextLong();

	private Task task;
	private Throwable error;
	private TaskReport report;

	@Before
	public void setUp() {
		task = mock(Task.class);
		error = mock(Throwable.class);
		report = new TaskReport(task, EXECUTION_TIME, error);
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

		report = new TaskReport(task, EXECUTION_TIME, null);
		assertTrue(report.isTaskDone());
	}

	@Test
	public void shouldGetError() {
		assertSame(error, report.getError());
	}
}