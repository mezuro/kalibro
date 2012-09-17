package org.kalibro.core.concurrent;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.TestCase;

public class TaskReportTest extends TestCase {

	private TaskReport report;

	@Test
	public void checkReportForTaskDone() {
		report = new TaskReport(42, null);
		assertEquals(42, report.getExecutionTime());
		assertTrue(report.isTaskDone());
		assertNull(report.getError());
	}

	@Test
	public void checkReportForTaskHalted() {
		NullPointerException error = new NullPointerException();
		report = new TaskReport(42, error);
		assertEquals(42, report.getExecutionTime());
		assertFalse(report.isTaskDone());
		assertSame(error, report.getError());
	}
}