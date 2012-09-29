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

	/** Returns the {@link BaseTool} describing the metric collector tool. */
	BaseTool getBaseTool();

	/**
	 * Collect metric results for a software project.<br/>
	 * At most one result should have a module with granularity = {@link Granularity}.APPLICATION
	 * 
	 * @param codeDirectory Directory where the source code is located.
	 * @param metrics Set of metrics Kalibro wants from the collector.
	 * @return Set of {@link NativeModuleResult}s obtained.
	 */
	Set<NativeModuleResult> collectMetrics(File codeDirectory, Set<NativeMetric> metrics) throws Exception;
}