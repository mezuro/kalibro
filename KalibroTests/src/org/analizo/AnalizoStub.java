package org.analizo;

import static org.kalibro.core.model.BaseToolFixtures.newAnalizoStub;
import static org.kalibro.core.model.NativeModuleResultFixtures.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.kalibro.BaseTool;
import org.kalibro.NativeMetric;
import org.kalibro.NativeModuleResult;
import org.kalibro.core.MetricCollector;

public class AnalizoStub implements MetricCollector {

	@Override
	public BaseTool getBaseTool() {
		return newAnalizoStub();
	}

	@Override
	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> metrics) {
		HashSet<NativeModuleResult> results = new HashSet<NativeModuleResult>();
		results.add(newHelloWorldApplicationResult());
		results.add(newHelloWorldClassResult());
		return results;
	}
}