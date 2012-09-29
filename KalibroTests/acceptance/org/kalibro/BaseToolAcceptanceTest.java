package org.kalibro;

import static org.junit.Assert.*;

import java.util.SortedSet;

import org.junit.experimental.theories.Theory;
import org.kalibro.tests.AcceptanceTest;

public class BaseToolAcceptanceTest extends AcceptanceTest {

	private SortedSet<BaseTool> baseTools;

	@Theory
	public void baseToolsShouldBeThere(SupportedDatabase databaseType) {
		changeDatabase(databaseType);
		baseTools = BaseTool.all();
		verifyAndRemove("org.analizo.AnalizoMetricCollector");
		verifyAndRemove("org.analizo.CheckstyleMetricCollector");
		verifyAndRemove("org.analizo.CvsAnalyMetricCollector");
		assertTrue(baseTools.isEmpty());
	}

	private void verifyAndRemove(String collectorClassName) {
		BaseTool baseTool = baseTools.first();
		assertEquals(collectorClassName, baseTool.getCollectorClassName());

		BaseTool expected = new BaseTool(collectorClassName);
		assertEquals(expected.getName(), baseTool.getName());
		assertEquals(expected.getDescription(), baseTool.getDescription());
		assertEquals(expected.getSupportedMetrics(), baseTool.getSupportedMetrics());
		baseTools.remove(baseTool);
	}
}