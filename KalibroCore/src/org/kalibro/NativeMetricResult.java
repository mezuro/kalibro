package org.kalibro;

/**
 * Represents the result of a {@link NativeMetric} for a {@link Module}.
 * 
 * @author Carlos Morais
 */
public final class NativeMetricResult extends AbstractMetricResult {

	/**
	 * Creates an instance of NativeMetricResult.
	 * 
	 * @param metric The {@link NativeMetric} of the result.
	 * @param value The value collected for the metric.
	 */
	public NativeMetricResult(NativeMetric metric, Double value) {
		super(metric, value);
	}
}