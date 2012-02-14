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
		ProjectResult projectResult = helloWorldResult();
		projectResult.setSourceTree(junitAnalizoTree());
		resultMap = junitAnalizoResultMap();
		aggregator = new ResultsAggregator(projectResult, resultMap);
		classMetric = new NativeMetric("Class_name_length", CLASS, JAVA);
		packageMetric = new NativeMetric("Package_name_length", PACKAGE, JAVA);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkAggregationOnOrg() {
		ModuleResult org = resultMap.get(junitAnalizoTree().getModule());
		checkResult(org, packageMetric, 3.0);
		assertFalse(org.hasResultFor(classMetric));

		aggregator.aggregate();
		checkResult(org, packageMetric, 3.0, 7.0);
		checkResult(org, classMetric, Double.NaN, 22.0, 19.0, 6.0, 17.0);
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
	public void checkAggregationOnJunit() {
		aggregator.aggregate();
		ModuleResult junit = resultMap.get(junitNode().getModule());
		assertFalse(junit.hasResultFor(packageMetric));
		checkResult(junit, classMetric, Double.NaN, 6.0, 17.0);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkAggregationOnLeaf() {
		ModuleResult comparisonFailure = resultMap.get(comparisonFailureNode().getModule());
		assertFalse(comparisonFailure.hasResultFor(packageMetric));
		checkResult(comparisonFailure, classMetric, 17.0);

		aggregator.aggregate();
		assertFalse(comparisonFailure.hasResultFor(packageMetric));
		checkResult(comparisonFailure, classMetric, 17.0);
	}

	private void checkResult(ModuleResult result, NativeMetric metric, Double value, Double... descendentResults) {
		MetricResult metricResult = result.getResultFor(metric);
		assertDoubleEquals(value, metricResult.getValue());
		assertDeepEquals(metricResult.getDescendentResults(), descendentResults);
	}
}