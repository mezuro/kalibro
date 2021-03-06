package org.kalibro;

import java.io.File;
import java.util.Set;

import org.kalibro.core.concurrent.Writer;

/**
 * Interface between Kalibro and the metric collector tool.<br/>
 * All implementations of this interface should have a public default constructor. The constructor should verify if the
 * collector is working and throw an exception otherwise. The complete name of the class should be registered at the
 * configuration file 'META-INF/collectors' for the collector to be used by Kalibro.
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
	 * @param resultWriter Where the {@link NativeModuleResult}s obtained should be written to.
	 */
	void collectMetrics(
		File codeDirectory, Set<NativeMetric> wantedMetrics, Writer<NativeModuleResult> resultWriter) throws Exception;
}