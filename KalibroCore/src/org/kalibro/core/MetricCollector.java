package org.kalibro.core;

import java.io.File;
import java.util.Set;

import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;
import org.kalibro.core.model.enums.Granularity;

/**
 * Interface between Kalibro and the metric collector tool.
 * 
 * @author Carlos Morais
 * @author Paulo Meirelles
 */
public interface MetricCollector {

	/** Returns the set of {@link NativeMetric}s provided by the metric collector tool. */
	public Set<NativeMetric> getSupportedMetrics();

	/**
	 * Collect metric results for a software project.<br/>
	 * At most one result should have a module with granularity = {@link Granularity}.APPLICATION
	 * 
	 * @param codeDirectory Directory where the source code is located.
	 * @param metrics Set of metrics Kalibro wants from the collector.
	 * @return Set of {@link NativeModuleResult}s obtained.
	 */
	public Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> metrics) throws Exception;
}