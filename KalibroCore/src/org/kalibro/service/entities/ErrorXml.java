package org.kalibro.service.entities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.KalibroError;
import org.kalibro.dto.DataTransferObject;

@XmlRootElement(name = "Error")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorXml extends DataTransferObject<Throwable> {

	private String errorClass;

	private String message;

	private ErrorXml cause;

	@XmlElement(name = "stackTraceElement")
	private List<StackTraceElementXml> stackTrace;

	public ErrorXml() {
		super();
	}

	public ErrorXml(Throwable error) {
		errorClass = error.getClass().getCanonicalName();
		message = error.getMessage();
		if (error.getCause() != null)
			cause = new ErrorXml(error.getCause());
		initializeStackTrace(error);
	}

	private void initializeStackTrace(Throwable error) {
		stackTrace = new ArrayList<StackTraceElementXml>();
		for (StackTraceElement element : error.getStackTrace())
			stackTrace.add(new StackTraceElementXml(element));
	}

	@Override
	public Throwable convert() {
		Throwable error = initializeError();
		if (cause != null)
			error.initCause(cause.convert());
		convertStackTrace(error);
		return error;
	}

	private Throwable initializeError() {
		try {
			return (Throwable) Class.forName(errorClass).getConstructor(String.class).newInstance(message);
		} catch (Exception exception) {
			throw new KalibroError("Could not convert Error XML to Throwable", exception);
		}
	}

	private void convertStackTrace(Throwable error) {
		StackTraceElement[] stackTraceConverted = new StackTraceElement[stackTrace.size()];
		for (int i = 0; i < stackTrace.size(); i++)
			stackTraceConverted[i] = stackTrace.get(i).convert();
		error.setStackTrace(stackTraceConverted);
	}
}