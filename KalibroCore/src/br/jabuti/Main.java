package br.jabuti;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.analizo.AnalizoMetricCollector;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeMetricResult;
import org.kalibro.core.model.NativeModuleResult;

public class Main {

	public static void main(String[] args) throws IOException {

//		AnalizoMetricCollector metricCollector = new AnalizoMetricCollector();
		JabutiMetricCollector metricCollector = new JabutiMetricCollector();

		// JBT_BASE_DIRECTORY
		File workDirectory = new File("/home/glesio/Documents/USP/pesquisa/jabuti/vending/");
		Set<NativeMetric> supportedMetrics =  metricCollector.getBaseTool().getSupportedMetrics();

		Set<NativeModuleResult> moduleResult = metricCollector.collectMetrics(workDirectory, supportedMetrics);
		for (NativeModuleResult nativeModuleResult : moduleResult) {
			for (NativeMetricResult nativeMetricResult : nativeModuleResult.getMetricResults()) {
				System.out.println(nativeMetricResult.getMetric() + " : " + nativeMetricResult.getValue());
			}
		}
	}

}
