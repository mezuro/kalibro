package org.kalibro.dto;

import org.kalibro.Granularity;
import org.kalibro.Module;

public abstract class ModuleDto extends DataTransferObject<Module> {

	@Override
	public Module convert() {
		return new Module(granularity(), name());
	}

	public abstract Granularity granularity();

	public abstract String[] name();
}