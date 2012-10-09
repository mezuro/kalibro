package org.kalibro.dto;

import java.util.Set;

import org.kalibro.*;

/**
 * Data transfer object for {@link Metric}.
 * 
 * @author Carlos Morais
 */
public abstract class MetricDto extends DataTransferObject<Metric> {

	@Override
	public Metric convert() {
		Metric metric = compound() ? convertCompound() : convertNative();
		metric.setDescription(description() == null ? "" : description());
		return metric;
	}

	private CompoundMetric convertCompound() {
		CompoundMetric metric = new CompoundMetric(name());
		metric.setScope(scope());
		metric.setScript(script());
		return metric;
	}

	private NativeMetric convertNative() {
		return new NativeMetric(name(), scope(), languages().toArray(new Language[0]));
	}

	public abstract boolean compound();

	public abstract String name();

	public abstract Granularity scope();

	public abstract String description();

	public abstract Set<Language> languages();

	public abstract String script();
}