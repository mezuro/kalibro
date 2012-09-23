package org.analizo;

import static org.kalibro.core.model.BaseToolFixtures.newAnalizoStub;
import static org.kalibro.core.model.NativeModuleResultFixtures.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.kalibro.core.MetricCollector;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;

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