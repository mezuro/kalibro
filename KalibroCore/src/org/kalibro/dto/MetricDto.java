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

	protected abstract boolean compound();

	protected abstract String name();

	protected abstract Granularity scope();

	protected abstract String description();

	protected abstract Set<Language> languages();

	protected abstract String script();
}