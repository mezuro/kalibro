package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.Granularity;
import org.kalibro.Module;
import org.kalibro.dto.ModuleDto;

/**
 * XML element for {@link Module}.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "module")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModuleXml extends ModuleDto {

	@XmlElement
	private String[] name;

	@XmlElement
	private Granularity granularity;

	public ModuleXml() {
		super();
	}

	public ModuleXml(Module module) {
		name = module.getName();
		granularity = module.getGranularity();
	}

	@Override
	public Granularity granularity() {
		return granularity;
	}

	@Override
	public String[] name() {
		return name;
	}
}