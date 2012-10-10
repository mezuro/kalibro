package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.ProcessState;

/**
 * XML element for mapping times to {@link ProcessState}.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "stateTime")
@XmlAccessorType(XmlAccessType.FIELD)
public class StateTimeXml {

	@XmlElement
	private ProcessState state;

	@XmlElement
	private Long time;

	public StateTimeXml() {
		super();
	}

	public StateTimeXml(ProcessState state, Long time) {
		this.state = state;
		this.time = time;
	}

	public ProcessState state() {
		return state;
	}

	public Long time() {
		return time;
	}
}