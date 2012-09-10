package org.kalibro.service.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.core.dto.DataTransferObject;
import org.kalibro.core.model.Module;
import org.kalibro.core.model.enums.Granularity;

@XmlRootElement(name = "Module")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModuleXml extends DataTransferObject<Module> {

	private String name;
	private Granularity granularity;

	public ModuleXml() {
		super();
	}

	public ModuleXml(Module module) {
		name = module.getName();
		granularity = module.getGranularity();
	}

	@Override
	public Module convert() {
		return new Module(granularity, name);
	}
}