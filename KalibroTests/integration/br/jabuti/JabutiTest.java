package br.jabuti;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroException;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;

import br.jabuti.JabutiMetricCollector;

public class JabutiTest {

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
		jabuti.getBaseTool();
		project = new File(System.getenv("HOME") + "/vending");
		metrics = new HashSet<NativeMetric>();
		allNodesEI = new NativeMetric("All Nodes Exception Independent", Granularity.APPLICATION, Language.JAVA);
		metrics.add(allNodesEI);
		allNodesED = new NativeMetric("All Nodes Exception Dependent",   Granularity.APPLICATION, Language.JAVA);
		metrics.add(allNodesED);
		allEdgesEI = new NativeMetric("All Edges Exception Independent", Granularity.APPLICATION, Language.JAVA);
		metrics.add(allEdgesEI);
		allEdgesED = new NativeMetric("All Edges Exception Dependent",   Granularity.APPLICATION, Language.JAVA);
		metrics.add(allEdgesED);
		allUsesEI = new NativeMetric("All Uses Exception Independent",  Granularity.APPLICATION, Language.JAVA);
		metrics.add(allUsesEI);
		allUsesED = new NativeMetric("All Uses Exception Dependent",    Granularity.APPLICATION, Language.JAVA);
		metrics.add(allUsesED);
		allPotEI = new NativeMetric("All Pot Exception Independent",   Granularity.APPLICATION, Language.JAVA);
		metrics.add(allPotEI);
		allPotED = new NativeMetric("All Pot Exception Dependent",     Granularity.APPLICATION, Language.JAVA);
		metrics.add(allPotED);
	}
	
	@Test
	public void jabutiInstallTest() {
		assertTrue(new File("/usr/local/bin/jabuti").isFile());
		assertTrue(new File("/usr/local/lib/jabuti.jar").isFile());
	}

	@Test
	public void vendingProjectTest() {
		assertTrue(new File(System.getenv("HOME") + "/vending").isDirectory());
	}
	
	@Test
	public void checkBaseTool() {
		BaseTool baseTool = jabuti.getBaseTool();
		assertNotNull(baseTool);
		assertEquals(JabutiMetricCollector.class, baseTool.getCollectorClass());
		assertNotNull(baseTool.getSupportedMetrics());
	}

	@Test
	public void collectMetricsOk() throws Exception {
		jabuti.getBaseTool();
		Set<NativeModuleResult> result = jabuti.collectMetrics(project, metrics);
		assertNotNull(result);
		assertEquals(5, result.size()); // só retorna global, por enquanto
	}
	
	@Test(expected=KalibroException.class)
	public void collectMetricsFail() throws Exception {		
		jabuti.collectMetrics(new File("/tmp"), metrics);		
	}
	
	@Test
	public void listSizeResultTest() {
		assertEquals(metrics.size(), jabuti.getBaseTool().getSupportedMetrics().size());
	}
	
	@Test
	public void listMetricSupportedTest() {
		assertEquals(metrics, jabuti.getBaseTool().getSupportedMetrics());
	}
	
	@Test
	public void shouldParseResultsOutputToModuleResults() throws Exception {
		Set<NativeModuleResult> results = jabuti.collectMetrics(project, metrics);
		assertNotNull(results);
		assertFalse(results.isEmpty());

		Map<Integer, Map<Integer, Double>> resultFile = this.outputJabutiMap();
		// Test Run Global
		for (int j =0; j < 5; j++) {
			NativeModuleResult result = results.toArray(new NativeModuleResult[0])[j];
			assertEquals(metrics.size(), result.getMetricResults().size());
			Map<Integer, Double> valueMap = resultFile.get(j);
			
			for (int y =0; y < 8; y++) {
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
		while((line = buffer.readLine()) != null) {
			if ("---".equals(line)) {
				x++; 
				y = 0;
				buffer.readLine();
				value = new HashMap<Integer, Double>();
				continue;
			}
			value.put(y, new Double(line.split(":")[1].replace("\t", "").trim()));
			map.put(x-1, value);
			y++;
		}
		
		return map;
	}

}