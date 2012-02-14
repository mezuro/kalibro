package org.kalibro.service.entities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.core.util.DataTransferObject;

@XmlRootElement(name = "Error")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorXml implements DataTransferObject<Exception> {

	private String message;

	@XmlElement(name = "stackTraceElement")
	private List<StackTraceElementXml> stackTrace;

	public ErrorXml() {
		super();
	}

	public ErrorXml(Exception error) {
		message = error.getMessage();
		initializeStackTrace(error);
	}

	private void initializeStackTrace(Exception error) {
		stackTrace = new ArrayList<StackTraceElementXml>();
		for (StackTraceElement element : error.getStackTrace())
			stackTrace.add(new StackTraceElementXml(element));
	}

	@Override
	public Exception convert() {
		Exception error = new Exception(message);
		convertStackTrace(error);
		return error;
	}

	private void convertStackTrace(Exception error) {
		StackTraceElement[] stackTraceConverted = new StackTraceElement[stackTrace.size()];
		for (int i = 0; i < stackTrace.size(); i++)
			stackTraceConverted[i] = stackTrace.get(i).convert();
		error.setStackTrace(stackTraceConverted);
	}
}