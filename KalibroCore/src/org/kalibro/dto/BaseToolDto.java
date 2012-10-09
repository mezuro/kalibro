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

	public abstract String name();

	public abstract String description();

	public abstract Set<NativeMetric> supportedMetrics();

	public abstract String collectorClassName();
}