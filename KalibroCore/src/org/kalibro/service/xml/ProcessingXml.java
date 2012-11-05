package org.kalibro.service.xml;

import java.util.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.ModuleResult;
import org.kalibro.ProcessState;
import org.kalibro.Processing;
import org.kalibro.dto.ProcessingDto;

/**
 * XML element for {@link Processing}.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "processing")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProcessingXml extends ProcessingDto {

	@XmlElement(required = true)
	private Long id;

	@XmlElement(required = true)
	private Date date;

	@XmlElement(required = true)
	private ProcessState state;

	@XmlElement
	private ThrowableXml error;

	@XmlElement(name = "processTime")
	private Collection<ProcessTimeXml> processTimes;

	@XmlElement
	private Long resultsRootId;

	public ProcessingXml() {
		super();
	}

	public ProcessingXml(Processing processing) {
		id = processing.getId();
		date = processing.getDate();
		setState(processing);
		setStateTimes(processing);
		setResultsRoot(processing.getResultsRoot());
	}

	private void setState(Processing processing) {
		state = processing.getState();
		if (state == ProcessState.ERROR) {
			state = processing.getStateWhenErrorOcurred();
			error = new ThrowableXml(processing.getError());
		}
	}

	private void setStateTimes(Processing processing) {
		processTimes = new ArrayList<ProcessTimeXml>();
		for (ProcessState processState : ProcessState.values()) {
			Long time = processing.getStateTime(processState);
			if (time != null)
				processTimes.add(new ProcessTimeXml(processState, time));
		}
	}

	private void setResultsRoot(ModuleResult resultsRoot) {
		resultsRootId = resultsRoot == null ? null : resultsRoot.getId();
	}

	@Override
	public Long id() {
		return id;
	}

	@Override
	public Date date() {
		return date;
	}

	@Override
	public ProcessState state() {
		return state;
	}

	@Override
	public Throwable error() {
		return error == null ? null : error.convert();
	}

	@Override
	public Map<ProcessState, Long> stateTimes() {
		Map<ProcessState, Long> map = new HashMap<ProcessState, Long>();
		if (processTimes != null)
			for (ProcessTimeXml stateTime : processTimes)
				map.put(stateTime.state(), stateTime.time());
		return map;
	}

	@Override
	public Long resultsRootId() {
		return resultsRootId;
	}
}