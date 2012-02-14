package org.analizo;

import static org.kalibro.core.model.enums.Granularity.*;

import java.io.File;
import java.util.*;

import org.kalibro.core.MetricCollector;
import org.kalibro.core.model.Module;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeMetricResult;
import org.kalibro.core.model.NativeModuleResult;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;

public class AnalizoStub implements MetricCollector {

	private static Map<String, NativeMetric> nativeMetrics = initializeMetrics();

	private static NativeModuleResult applicationResult = initializeApplicationResult();
	private static NativeModuleResult classResult = initializeClassResult();

	private static Map<String, NativeMetric> initializeMetrics() {
		nativeMetrics = new HashMap<String, NativeMetric>();
		initializeGlobalMetrics();
		initializeModuleMetrics();
		return nativeMetrics;
	}

	private static void initializeGlobalMetrics() {
		putMetric("total_abstract_classes", "Total Abstract Classes");
		putMetric("total_cof", "Total Coupling Factor");
		putMetric("total_loc", "Total Lines of Code");
		putMetric("total_methods_per_abstract_class", "Total number of methods per abstract class");
		putMetric("total_modules", "Total Number of Modules/Classes");
		String totalModules = "Total number of modules/classes with at least one defined ";
		putMetric("total_modules_with_defined_attributes", totalModules + "attributes");
		putMetric("total_modules_with_defined_methods", totalModules + "method");
		putMetric("total_nom", "Total Number of Methods");
	}

	private static void initializeModuleMetrics() {
		putMetric("acc", "Afferent Connections per Class (used to calculate COF - Coupling Factor)");
		putMetric("accm", "Average Cyclomatic Complexity per Method");
		putMetric("amloc", "Average Method LOC");
		putMetric("anpm", "Average Number of Parameters per Method");
		putMetric("cbo", "Coupling Between Objects");
		putMetric("dit", "Depth of Inheritance Tree");
		putMetric("lcom4", "Lack of Cohesion of Methods");
		putMetric("loc", "Lines of Code");
		putMetric("mmloc", "Max Method LOC");
		putMetric("noa", "Number of Attributes");
		putMetric("noc", "Number of Children");
		putMetric("nom", "Number of Methods");
		putMetric("npa", "Number of Public Attributes");
		putMetric("npm", "Number of Public Methods");
		putMetric("rfc", "Response For a Class");
		putMetric("sc", "Structural Complexity (CBO X LCOM4)");
	}

	private static void putMetric(String code, String name) {
		Granularity scope = code.startsWith("total") ? APPLICATION : CLASS;
		NativeMetric metric = new NativeMetric(name, scope, Language.values());
		nativeMetrics.put(code, metric);
	}

	private static NativeModuleResult initializeApplicationResult() {
		applicationResult = new NativeModuleResult(new Module(APPLICATION, "null"));
		addMetricResult(applicationResult, "total_abstract_classes", 0.0);
		addMetricResult(applicationResult, "total_cof", 1.0);
		addMetricResult(applicationResult, "total_loc", 4.0);
		addMetricResult(applicationResult, "total_methods_per_abstract_class", 0.0);
		addMetricResult(applicationResult, "total_modules", 1.0);
		addMetricResult(applicationResult, "total_modules_with_defined_attributes", 0.0);
		addMetricResult(applicationResult, "total_modules_with_defined_methods", 1.0);
		addMetricResult(applicationResult, "total_nom", 1.0);
		return applicationResult;
	}

	private static NativeModuleResult initializeClassResult() {
		classResult = new NativeModuleResult(new Module(CLASS, "HelloWorld"));
		addMetricResult(classResult, "acc", 0.0);
		addMetricResult(classResult, "accm", 1.0);
		addMetricResult(classResult, "amloc", 4.0);
		addMetricResult(classResult, "anpm", 2.0);
		addMetricResult(classResult, "cbo", 0.0);
		addMetricResult(classResult, "dit", 0.0);
		addMetricResult(classResult, "lcom4", 1.0);
		addMetricResult(classResult, "loc", 4.0);
		addMetricResult(classResult, "mmloc", 4.0);
		addMetricResult(classResult, "noa", 0.0);
		addMetricResult(classResult, "noc", 0.0);
		addMetricResult(classResult, "nom", 1.0);
		addMetricResult(classResult, "npa", 0.0);
		addMetricResult(classResult, "npm", 1.0);
		addMetricResult(classResult, "rfc", 1.0);
		addMetricResult(classResult, "sc", 0.0);
		return classResult;
	}

	private static void addMetricResult(NativeModuleResult moduleResult, String metricCode, Double value) {
		NativeMetric metric = nativeMetrics.get(metricCode);
		moduleResult.addMetricResult(new NativeMetricResult(metric, value));
	}

	public static Set<NativeMetric> nativeMetrics() {
		return new HashSet<NativeMetric>(nativeMetrics.values());
	}

	public static NativeMetric nativeMetric(String code) {
		return nativeMetrics.get(code);
	}

	public static Set<NativeModuleResult> collectMetrics() {
		return new TreeSet<NativeModuleResult>(Arrays.asList(applicationResult, classResult));
	}

	public static NativeModuleResult applicationResult() {
		return applicationResult;
	}

	public static NativeModuleResult classResult() {
		return classResult;
	}

	@Override
	public Set<NativeMetric> getSupportedMetrics() {
		return nativeMetrics();
	}

	@Override
	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> metrics) {
		return collectMetrics();
	}
}