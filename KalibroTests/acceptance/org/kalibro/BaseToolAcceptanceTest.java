package org.kalibro;

import org.analizo.AnalizoMetricCollector;
import org.checkstyle.CheckstyleMetricCollector;
import org.cvsanaly.CvsAnalyMetricCollector;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kalibro.tests.AcceptanceTest;

public class BaseToolAcceptanceTest extends AcceptanceTest {

	@BeforeClass
	public static void prepareSettings() {
		prepareSettings(SupportedDatabase.MYSQL);
	}

	@Test
	public void shouldGetBaseToolNames() {
		assertDeepEquals(set("Analizo", "Checkstyle", "CVSAnalY"), BaseTool.allNames());
	}

	@Test
	public void shouldGetByName() {
		shouldGetBaseTool("Analizo", AnalizoMetricCollector.class);
		shouldGetBaseTool("Checkstyle", CheckstyleMetricCollector.class);
		shouldGetBaseTool("CVSAnalY", CvsAnalyMetricCollector.class);
	}

	private void shouldGetBaseTool(String baseToolName, Class<? extends MetricCollector> collectorClass) {
		assertDeepEquals(new BaseTool(collectorClass.getName()), BaseTool.get(baseToolName));
	}
}