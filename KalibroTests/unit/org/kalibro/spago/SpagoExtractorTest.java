package org.kalibro.spago;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import it.eng.spago4q.bo.ProjectDetail;
import it.eng.spago4q.extractors.bo.GenericItemInterface;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.command.StringOutputStream;
import org.kalibro.core.model.*;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SpagoExtractor.class)
public class SpagoExtractorTest extends KalibroTestCase {

	@BeforeClass
	public static void init() {
		System.setOut(new PrintStream(new StringOutputStream()));
		System.setErr(new PrintStream(new StringOutputStream()));
		emmaCoverage();
	}

	private static void emmaCoverage() {
		SpagoExtractor spagoExtractor = new SpagoExtractor();
		spagoExtractor.setUp();
		spagoExtractor.tearDown();
	}

	private ModuleResult result;
	private KalibroClientForSpago client;

	private SpagoExtractor extractor;

	@Before
	public void setUp() throws Exception {
		client = PowerMockito.mock(KalibroClientForSpago.class);
		result = ModuleResultFixtures.helloWorldApplicationResult();
		PowerMockito.whenNew(KalibroClientForSpago.class).withArguments(anyString()).thenReturn(client);
		PowerMockito.when(client.getLastApplicationResult(anyString())).thenReturn(result);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldProduceErrorItemOnException() throws IOException {
		extractor = new SpagoExtractorStub("HelloWorld_broken.xml");
		List<GenericItemInterface> items = extractor.extract();
		assertEquals(1, items.size());

		GenericItemInterface item = items.get(0);
		String expectedStackTrace = IOUtils.toString(getClass().getResourceAsStream("parseExceptionTrace.txt"));
		assertEquals("Extraction failed.", item.getValue("Resource"));
		assertEquals(expectedStackTrace, item.getValue("Metric"));
		assertEquals("0.0", item.getValue("Value"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotIncludeJavaProjects() throws IOException {
		extractor = new SpagoExtractorStub("HelloWorld_Java.xml");
		assertTrue(extractor.extract().isEmpty());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnEmptyIfProjectHasNoResults() throws IOException {
		PowerMockito.when(client.hasResultsFor(anyString())).thenReturn(false);
		extractor = new SpagoExtractorStub("HelloWorld.xml");
		assertTrue(extractor.extract().isEmpty());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveAndProcessProject() throws IOException {
		PowerMockito.when(client.hasResultsFor(anyString())).thenReturn(anyBoolean());
		extractor = new SpagoExtractorStub("HelloWorld.xml");
		extractor.extract();
		Mockito.verify(client).saveAndProcess(any(Project.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testNormalExtraction() throws IOException {
		PowerMockito.when(client.hasResultsFor(anyString())).thenReturn(true);

		extractor = new SpagoExtractorStub("HelloWorld.xml");
		List<GenericItemInterface> items = extractor.extract();
		assertEquals(result.getMetricResults().size(), items.size());
		int index = 0;
		for (MetricResult metricResult : result.getMetricResults()) {
			checkResult(items.get(index), metricResult.getMetric().getName(), "" + metricResult.getValue());
			index++;
		}
	}

	private void checkResult(GenericItemInterface item, String metric, String value) {
		assertEquals("HelloWorld-1.0R1", item.getValue("Resource"));
		assertEquals(metric, item.getValue("Metric"));
		assertEquals(value, item.getValue("Value"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetAverageForNonApplicationResults() throws IOException {
		result.getMetricResults().clear();
		result.addMetricResult(MetricResultFixtures.metricResult("loc", Double.NaN, 1.0, 2.0, 3.0));
		PowerMockito.when(client.hasResultsFor(anyString())).thenReturn(true);

		extractor = new SpagoExtractorStub("HelloWorld.xml");
		GenericItemInterface item = extractor.extract().get(0);
		assertEquals("2.0", item.getValue("Value"));
	}
}

class SpagoExtractorStub extends SpagoExtractor {

	private ProjectDetail projectDetail;

	public SpagoExtractorStub(String xmlFileName) throws IOException {
		String detail = IOUtils.toString(getClass().getResourceAsStream(xmlFileName));
		projectDetail = new ProjectDetail(1, "HelloWorld-1.0R1", detail);
	}

	@Override
	protected String readOperationParameterValue(String parameterName) {
		return "file:///";
	}

	@Override
	public ArrayList<ProjectDetail> getProjectList() {
		return new ArrayList<ProjectDetail>(Arrays.asList(projectDetail));
	}
}