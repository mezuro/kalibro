package org.kalibro.service.xml;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.dto.ThrowableDto;

/**
 * XML element for {@link Throwable}.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "throwable")
@XmlAccessorType(XmlAccessType.FIELD)
public class ThrowableXml extends ThrowableDto {

	@XmlElement(required = true)
	private String targetString;

	@XmlElement
	private String message;

	@XmlElement
	private ThrowableXml cause;

	@XmlElement(name = "stackTraceElement")
	private List<StackTraceElementXml> stackTrace;

	public ThrowableXml() {
		super();
	}

	public ThrowableXml(Throwable throwable) {
		targetString = throwable.toString();
		message = throwable.getMessage();
		stackTrace = createDtos(Arrays.asList(throwable.getStackTrace()), StackTraceElementXml.class);
		setCause(throwable.getCause());
	}

	private void setCause(Throwable cause) {
		if (cause != null)
			this.cause = new ThrowableXml(cause);
	}

	@Override
	public String targetString() {
		return targetString;
	}

	@Override
	public String message() {
		return message;
	}

	@Override
	public Throwable cause() {
		return cause == null ? null : cause.convert();
	}

	@Override
	public StackTraceElement[] stackTrace() {
		return toList(stackTrace).toArray(new StackTraceElement[0]);
	}
}