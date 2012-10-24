package br.jabuti;

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.kalibro.*;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.concurrent.Writer;
import org.kalibro.tests.IntegrationTest;

public class JabutiTest extends IntegrationTest {

	private JabutiMetricCollector jabuti;
	private File project;
	private Set<NativeMetric> metrics;
	private NativeMetric allNodesEI;
	private NativeMetric allNodesED;
	private NativeMetric allEdgesEI;
	private NativeMetric allEdgesED;
	private NativeMetric allUsesEI;
	private NativeMetric allUsesED;
	private NativeMetric allPotEI;
	private NativeMetric allPotED;

	@Before
	public void setUp() throws Exception {
		jabuti = new JabutiMetricCollector();
		project = new File(samplesDirectory(), "jabuti");
		metrics = new HashSet<NativeMetric>();
		allNodesEI = new NativeMetric("All Nodes Exception Independent", Granularity.SOFTWARE, Language.JAVA);
		metrics.add(allNodesEI);
		allNodesED = new NativeMetric("All Nodes Exception Dependent", Granularity.SOFTWARE, Language.JAVA);
		metrics.add(allNodesED);
		allEdgesEI = new NativeMetric("All Edges Exception Independent", Granularity.SOFTWARE, Language.JAVA);
		metrics.add(allEdgesEI);
		allEdgesED = new NativeMetric("All Edges Exception Dependent", Granularity.SOFTWARE, Language.JAVA);
		metrics.add(allEdgesED);
		allUsesEI = new NativeMetric("All Uses Exception Independent", Granularity.SOFTWARE, Language.JAVA);
		metrics.add(allUsesEI);
		allUsesED = new NativeMetric("All Uses Exception Dependent", Granularity.SOFTWARE, Language.JAVA);
		metrics.add(allUsesED);
		allPotEI = new NativeMetric("All Pot Exception Independent", Granularity.SOFTWARE, Language.JAVA);
		metrics.add(allPotEI);
		allPotED = new NativeMetric("All Pot Exception Dependent", Granularity.SOFTWARE, Language.JAVA);
		metrics.add(allPotED);
	}

	@Test
	public void jabutiInstallTest() {
		assertTrue(new File("/usr/local/bin/jabuti").isFile());
		assertTrue(new File("/usr/local/lib/jabuti.jar").isFile());
	}

	@Test
	public void vendingProjectTest() {
		assertTrue(new File(samplesDirectory(), "jabuti").isDirectory());
	}

	@Test
	public void checkBaseTool() {
		assertEquals("", jabuti.name());
		assertEquals("", jabuti.description());
		assertDeepEquals(metrics, jabuti.supportedMetrics());
	}

	@Override
	protected Timeout testTimeout() {
		return new Timeout(90000);
	}

	@Test
	public void collectMetricsOk() throws Exception {
		Producer<NativeModuleResult> producer = new Producer<NativeModuleResult>();
		Writer<NativeModuleResult> resultWriter = producer.createWriter();
		jabuti.collectMetrics(project, metrics, resultWriter);

		assertTrue(producer.iterator().hasNext());
	}

	@Test(expected = KalibroException.class)
	public void collectMetricsFail() throws Exception {
		jabuti.collectMetrics(new File("/tmp"), metrics, new Producer<NativeModuleResult>().createWriter());
	}

	@Test
	public void shouldParseResultsOutputToModuleResults() throws Exception {
		Producer<NativeModuleResult> producer = new Producer<NativeModuleResult>();
		Writer<NativeModuleResult> resultWriter = producer.createWriter();
		jabuti.collectMetrics(project, metrics, resultWriter);
		Iterator<NativeModuleResult> results = producer.iterator();
		assertFalse(results.hasNext());

		Map<Integer, Map<Integer, Double>> resultFile = this.outputJabutiMap();
		// Test Run Global
		for (int j = 0; j < 3; j++) {
			NativeModuleResult result = results.next();
			assertEquals(metrics.size(), result.getMetricResults().size());
			Map<Integer, Double> valueMap = resultFile.get(j);

			for (int y = 0; y < 8; y++) {
				switch (y) {
					case 0:
						assertEquals(valueMap.get(y), result.getResultFor(allNodesEI).getValue());
						break;
					case 1:
						assertEquals(valueMap.get(y), result.getResultFor(allNodesED).getValue());
						break;
					case 2:
						assertEquals(valueMap.get(y), result.getResultFor(allEdgesEI).getValue());
						break;
					case 3:
						assertEquals(valueMap.get(y), result.getResultFor(allEdgesED).getValue());
						break;
					case 4:
						assertEquals(valueMap.get(y), result.getResultFor(allUsesEI).getValue());
						break;
					case 5:
						assertEquals(valueMap.get(y), result.getResultFor(allUsesED).getValue());
						break;
					case 6:
						assertEquals(valueMap.get(y), result.getResultFor(allPotEI).getValue());
						break;
					case 7:
						assertEquals(valueMap.get(y), result.getResultFor(allPotED).getValue());
						break;
				}
			}
		}

	}

	private Map<Integer, Map<Integer, Double>> outputJabutiMap() throws IOException {
		InputStream in = JabutiTest.class.getResourceAsStream("Jabuti-Output-Vending.txt");
		BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
		String line = null;

		Map<Integer, Map<Integer, Double>> map = new HashMap<Integer, Map<Integer, Double>>();
		Map<Integer, Double> value = null;
		int x = 0;
		int y = 0;
		while ((line = buffer.readLine()) != null) {
			if ("---".equals(line)) {
				x++;
				y = 0;
				buffer.readLine();
				value = new HashMap<Integer, Double>();
				continue;
			}
			value.put(y, new Double(line.split(":")[1].replace("\t", "").trim()));
			map.put(x - 1, value);
			y++;
		}

		return map;
	}

}