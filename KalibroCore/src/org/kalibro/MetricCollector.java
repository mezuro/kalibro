package org.kalibro;

import java.io.File;
import java.util.Set;

/**
 * Interface between Kalibro and the metric collector tool.
 * 
 * @author Carlos Morais
 * @author Paulo Meirelles
 */
public interface MetricCollector {

	/** Returns the collector tool name. */
	String name();

	/** Returns the collector tool description. */
	String description();

	/** Returns the metrics supported by the collector. */
	Set<NativeMetric> supportedMetrics();

	/**
	 * Collect metric results for a software project.<br/>
	 * At most one result should have a module with granularity = {@link Granularity}.APPLICATION
	 * 
	 * @param codeDirectory Directory where the source code is located.
	 * @param wantedMetrics Set of metrics Kalibro wants from the collector.
	 * @return Set of {@link NativeModuleResult}s obtained.
	 */
	Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> wantedMetrics) throws Exception;
}