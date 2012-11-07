package br.jabuti;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Granularity;
import org.kalibro.Language;
import org.kalibro.NativeMetric;
import org.kalibro.NativeModuleResult;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.concurrent.Writer;
import org.kalibro.tests.UnitTest;

public class JabutiOutputParserTest extends UnitTest {

	private JabutiOutputParser outputParser;
	private HashSet<NativeMetric> metrics;
	private NativeMetric allNodesEI;
	private NativeMetric allNodesED;
	private NativeMetric allEdgesEI;
	private NativeMetric allEdgesED;
	private NativeMetric allUsesEI;
	private NativeMetric allUsesED;
	private NativeMetric allPotEI;
	private NativeMetric allPotED;

	@Before
	public void setUp() throws IOException {

		metrics = new LinkedHashSet<NativeMetric>();
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

		InputStream metricListOutput = getClass().getResourceAsStream("Jabuti-Output-MetricList.txt");
		outputParser = new JabutiOutputParser(metricListOutput);
	}

	@Test
	public void checkSupportedMetrics() {
		assertArrayEquals(metrics.toArray(), outputParser.getSupportedMetrics().toArray());
	}

	@Test
	public void checkParserResults() throws IOException {
		Producer<NativeModuleResult> producer = new Producer<NativeModuleResult>();
		Writer<NativeModuleResult> resultWriter = producer.createWriter();

		InputStream vendingOutput = getClass().getResourceAsStream("Jabuti-Output-Vending.txt");
		outputParser.parseResults(vendingOutput, metrics, resultWriter);
		Iterator<NativeModuleResult> results = producer.iterator();
		assertTrue(results.hasNext());

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
		InputStream in = this.getClass().getResourceAsStream("Jabuti-Output-Vending.txt");
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
