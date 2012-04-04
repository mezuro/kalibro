package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ModuleNodeFixtures.*;
import static org.kalibro.core.model.ModuleResultFixtures.*;
import static org.kalibro.core.model.ProjectResultFixtures.*;
import static org.kalibro.core.model.enums.Granularity.*;
import static org.kalibro.core.model.enums.Language.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.*;

public class ResultsAggregatorTest extends KalibroTestCase {

	private Map<Module, ModuleResult> resultMap;
	private NativeMetric classMetric, packageMetric;

	private ResultsAggregator aggregator;

	@Before
	public void setUp() {
		ProjectResult projectResult = newHelloWorldResult();
		projectResult.setSourceTree(analizoCheckstyleTree());
		resultMap = analizoCheckstyleResultMap();
		aggregator = new ResultsAggregator(projectResult, resultMap);
		classMetric = new NativeMetric("Class_name_length", CLASS, JAVA);
		packageMetric = new NativeMetric("Package_name_length", PACKAGE, JAVA);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkAggregationOnOrg() {
		ModuleResult org = resultMap.get(analizoCheckstyleTree().getModule());
		checkResult(org, packageMetric, 3.0);
		assertFalse(org.hasResultFor(classMetric));

		aggregator.aggregate();
		checkResult(org, packageMetric, 3.0, 7.0, 10.0);
		checkResult(org, classMetric, Double.NaN, 22.0, 19.0, 25.0, 22.0);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkAggregationOnAnalizo() {
		ModuleResult analizo = resultMap.get(analizoNode().getModule());
		checkResult(analizo, packageMetric, 7.0);
		assertFalse(analizo.hasResultFor(classMetric));

		aggregator.aggregate();
		checkResult(analizo, packageMetric, 7.0);
		checkResult(analizo, classMetric, Double.NaN, 22.0, 19.0);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkAggregationOnCheckstyle() {
		ModuleResult checkstyle = resultMap.get(checkstyleNode().getModule());
		checkResult(checkstyle, packageMetric, 10.0);
		assertFalse(checkstyle.hasResultFor(classMetric));

		aggregator.aggregate();
		checkResult(checkstyle, packageMetric, 10.0);
		checkResult(checkstyle, classMetric, Double.NaN, 25.0, 22.0);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkAggregationOnLeaf() {
		ModuleResult checkstyleOutputParser = resultMap.get(checkstyleOutputParserNode().getModule());
		assertFalse(checkstyleOutputParser.hasResultFor(packageMetric));
		checkResult(checkstyleOutputParser, classMetric, 22.0);

		aggregator.aggregate();
		assertFalse(checkstyleOutputParser.hasResultFor(packageMetric));
		checkResult(checkstyleOutputParser, classMetric, 22.0);
	}

	private void checkResult(ModuleResult result, NativeMetric metric, Double value, Double... descendentResults) {
		MetricResult metricResult = result.getResultFor(metric);
		assertDoubleEquals(value, metricResult.getValue());
		assertDeepEquals(metricResult.getDescendentResults(), descendentResults);
	}
}