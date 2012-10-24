package org.kalibro;

/**
 * Represents the result of a {@link NativeMetric} for a {@link Module}.
 * 
 * @author Carlos Morais
 */
public class NativeMetricResult extends AbstractMetricResult {

	@SuppressWarnings("unused" /* used by SnakeYaml */)
	private NativeMetricResult() {
		this(new NativeMetric("", Granularity.CLASS), Double.NaN);
	}

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