package org.kalibro.core.concurrent;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class TaskReportTest extends KalibroTestCase {

	private TaskReport report;

	@Test(timeout = UNIT_TIMEOUT)
	public void checkReportForTaskDone() {
		report = new TaskReport(42, null);
		assertEquals(42, report.getExecutionTime());
		assertTrue(report.isTaskDone());
		assertNull(report.getError());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkReportForTaskHalted() {
		NullPointerException error = new NullPointerException();
		report = new TaskReport(42, error);
		assertEquals(42, report.getExecutionTime());
		assertFalse(report.isTaskDone());
		assertSame(error, report.getError());
	}
}