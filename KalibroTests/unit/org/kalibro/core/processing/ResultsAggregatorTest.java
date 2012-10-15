package org.kalibro.core.processing;

import static org.junit.Assert.assertFalse;
import static org.kalibro.Granularity.*;
import static org.kalibro.Language.JAVA;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.*;
import org.kalibro.tests.UnitTest;

public class ResultsAggregatorTest extends UnitTest {

	private Map<Module, ModuleResult> resultMap;
	private NativeMetric classMetric, packageMetric;

	private ResultsAggregator aggregator;

	@Before
	public void setUp() {
		Processing processing = newHelloWorldResult();
		processing.setResultsRoot(analizoCheckstyleTree());
		resultMap = analizoCheckstyleResultMap();
		aggregator = new ResultsAggregator(processing, resultMap);
		classMetric = new NativeMetric("Class_name_length", CLASS, JAVA);
		packageMetric = new NativeMetric("Package_name_length", PACKAGE, JAVA);
	}

	@Test
	public void checkAggregationOnOrg() {
		Module org = analizoCheckstyleTree().getModule();
		assertFalse(resultMap.containsKey(org));

		aggregator.aggregate();
		ModuleResult orgResult = resultMap.get(org);
		checkResult(orgResult, packageMetric, Double.NaN, 7.0);
		checkResult(orgResult, classMetric, Double.NaN, 22.0, 19.0, 25.0, 22.0);
	}

	@Test
	public void checkAggregationOnAnalizo() {
		ModuleResult analizo = resultMap.get(analizoNode().getModule());
		checkResult(analizo, packageMetric, 7.0);
		assertFalse(analizo.hasResultFor(classMetric));

		aggregator.aggregate();
		checkResult(analizo, packageMetric, 7.0);
		checkResult(analizo, classMetric, Double.NaN, 22.0, 19.0);
	}

	@Test
	public void checkAggregationOnCheckstyle() {
		Module checkstyle = checkstyleNode().getModule();
		assertFalse(resultMap.containsKey(checkstyle));

		aggregator.aggregate();
		ModuleResult checkstyleResult = resultMap.get(checkstyle);
		assertFalse(checkstyleResult.hasResultFor(packageMetric));
		checkResult(checkstyleResult, classMetric, Double.NaN, 25.0, 22.0);
	}

	@Test
	public void checkAggregationOnLeaf() {
		ModuleResult checkstyleOutputParser = resultMap.get(checkstyleOutputParserNode().getModule());
		assertFalse(checkstyleOutputParser.hasResultFor(packageMetric));
		checkResult(checkstyleOutputParser, classMetric, 22.0);

		aggregator.aggregate();
		assertFalse(checkstyleOutputParser.hasResultFor(packageMetric));
		checkResult(checkstyleOutputParser, classMetric, 22.0);
	}

	private void checkResult(ModuleResult result, NativeMetric metric, Double value, Double... descendantResults) {
		MetricResult metricResult = result.getResultFor(metric);
		assertDoubleEquals(value, metricResult.getValue());
		assertDeepEquals(asList(descendantResults), metricResult.getDescendantResults());
	}
}