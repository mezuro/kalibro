package org.kalibro.core.model;

import static org.kalibro.Granularity.*;
import static org.kalibro.core.model.MetricFixtures.analizoMetricCodes;
import static org.kalibro.core.model.MetricResultFixtures.analizoResult;

import org.kalibro.Granularity;
import org.kalibro.Module;
import org.kalibro.NativeModuleResult;

public final class NativeModuleResultFixtures {

	private static NativeModuleResult helloWorldApplicationResult = newHelloWorldApplicationResult();
	private static NativeModuleResult helloWorldClassResult = newHelloWorldClassResult();

	public static NativeModuleResult helloWorldApplicationResult() {
		return helloWorldApplicationResult;
	}

	public static NativeModuleResult newHelloWorldApplicationResult() {
		return newNativeResult("null", SOFTWARE);
	}

	public static NativeModuleResult helloWorldClassResult() {
		return helloWorldClassResult;
	}

	public static NativeModuleResult newHelloWorldClassResult() {
		return newNativeResult("HelloWorld", CLASS);
	}

	private static NativeModuleResult newNativeResult(String moduleName, Granularity moduleGranularity) {
		NativeModuleResult result = new NativeModuleResult(new Module(moduleGranularity, moduleName));
		for (String code : analizoMetricCodes())
			if (isCompatible(code, moduleGranularity))
				result.addMetricResult(analizoResult(code));
		return result;
	}

	private static boolean isCompatible(String code, Granularity scope) {
		boolean startsWithTotal = code.startsWith("total");
		return (scope == SOFTWARE && startsWithTotal) || (scope == CLASS && !startsWithTotal);
	}

	private NativeModuleResultFixtures() {
		return;
	}
}