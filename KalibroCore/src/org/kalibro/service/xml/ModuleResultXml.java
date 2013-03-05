package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.Module;
import org.kalibro.ModuleResult;
import org.kalibro.dto.ModuleResultDto;

/**
 * XML element for {@link ModuleResult}.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "moduleResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModuleResultXml extends ModuleResultDto {

	@XmlElement(required = true)
	private Long id;

	@XmlElement(required = true)
	private ModuleXml module;

	@XmlElement
	private Double grade;

	@XmlElement
	private Integer height;

	@XmlElement
	private Long parentId;

	public ModuleResultXml() {
		super();
	}

	public ModuleResultXml(ModuleResult moduleResult) {
		id = moduleResult.getId();
		module = new ModuleXml(moduleResult.getModule());
		grade = moduleResult.getGrade();
		height = moduleResult.getHeight();
		parentId = moduleResult.hasParent() ? moduleResult.getParent().getId() : null;
	}

	@Override
	public Long id() {
		return id;
	}

	@Override
	public Module module() {
		return module.convert();
	}

	@Override
	public Double grade() {
		return grade;
	}

	@Override
	public Integer height() {
		return height;
	}

	@Override
	public Long parentId() {
		return parentId;
	}
}