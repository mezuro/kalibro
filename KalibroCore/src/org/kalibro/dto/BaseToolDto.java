package org.kalibro.dto;

import java.util.Set;

import org.kalibro.BaseTool;
import org.kalibro.NativeMetric;

/**
 * Data transfer object for {@link BaseTool}.
 * 
 * @author Carlos Morais
 */
public abstract class BaseToolDto extends DataTransferObject<BaseTool> {

	@Override
	public BaseTool convert() {
		String description = (description() == null) ? "" : description();
		return new BaseTool(name(), description, supportedMetrics(), collectorClassName());
	}

	protected abstract String name();

	protected abstract String description();

	protected abstract Set<NativeMetric> supportedMetrics();

	protected abstract String collectorClassName();
}