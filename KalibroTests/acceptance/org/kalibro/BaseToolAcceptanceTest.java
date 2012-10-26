package org.kalibro;

import org.analizo.AnalizoMetricCollector;
import org.checkstyle.CheckstyleMetricCollector;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.AcceptanceTest;

public class BaseToolAcceptanceTest extends AcceptanceTest {

	@Before
	public void setUp() {
		prepareSettings(SupportedDatabase.APACHE_DERBY);
	}

	@Test
	public void shouldGetBaseToolNames() {
		assertDeepEquals(set("Analizo", "Checkstyle"), BaseTool.allNames());
	}

	@Test
	public void shouldGetByName() {
		shouldGetBaseTool("Analizo", AnalizoMetricCollector.class);
		shouldGetBaseTool("Checkstyle", CheckstyleMetricCollector.class);
	}

	private void shouldGetBaseTool(String baseToolName, Class<? extends MetricCollector> collectorClass) {
		assertDeepEquals(new BaseTool(collectorClass.getName()), BaseTool.get(baseToolName));
	}
}