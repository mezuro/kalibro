package org.kalibro.service.xml;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.ModuleResult;

/**
 * XML element for mapping {@link ModuleResult} to dates.
 * 
 * @author Carlos Morais
 * @author João M. M. da Silva
 */
@XmlRootElement(name = "dateModuleResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class DateModuleResultXml {

	@XmlElement
	private Date date;

	@XmlElement
	private ModuleResultXml moduleResult;

	public DateModuleResultXml() {
		super();
	}

	public DateModuleResultXml(Date date, ModuleResult moduleResult) {
		this.date = date;
		this.moduleResult = new ModuleResultXml(moduleResult);
	}

	public Date date() {
		return date;
	}

	public ModuleResult moduleResult() {
		return moduleResult.convert();
	}
}