package org.kalibro.core.concurrent;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.TestCase;

public class TypedTaskReportTest extends TestCase {

	private TypedTaskReport<String> report;

	@Test
	public void checkReportForTaskDone() {
		report = new TypedTaskReport<String>(42, null, "My result");
		assertEquals(42, report.getExecutionTime());
		assertTrue(report.isTaskDone());
		assertEquals("My result", report.getResult());
		assertNull(report.getError());
	}

	@Test
	public void checkReportForTaskHalted() {
		NullPointerException error = new NullPointerException();
		report = new TypedTaskReport<String>(42, error, null);
		assertEquals(42, report.getExecutionTime());
		assertFalse(report.isTaskDone());
		assertNull(report.getResult());
		assertSame(error, report.getError());
	}
}