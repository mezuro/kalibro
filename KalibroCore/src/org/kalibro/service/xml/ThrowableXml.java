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

	@XmlElement
	private String throwableClass;

	@XmlElement
	private String detailMessage;

	@XmlElement
	private ThrowableXml cause;

	@XmlElement(name = "stackTraceElement")
	private List<StackTraceElementXml> stackTrace;

	public ThrowableXml() {
		super();
	}

	public ThrowableXml(Throwable throwable) {
		throwableClass = throwable.getClass().getName();
		detailMessage = throwable.getMessage();
		stackTrace = createDtos(Arrays.asList(throwable.getStackTrace()), StackTraceElementXml.class);
		setCause(throwable.getCause());
	}

	private void setCause(Throwable cause) {
		if (cause != null)
			this.cause = new ThrowableXml(cause);
	}

	@Override
	public String throwableClass() {
		return throwableClass;
	}

	@Override
	public String detailMessage() {
		return detailMessage;
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