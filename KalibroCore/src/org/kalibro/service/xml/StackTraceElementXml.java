package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.dto.StackTraceElementDto;

/**
 * XML element for {@link StackTraceElement}.
 * 
 * @author Carlos Morais
 */
@XmlRootElement(name = "stackTraceElement")
@XmlAccessorType(XmlAccessType.FIELD)
public class StackTraceElementXml extends StackTraceElementDto {

	@XmlElement
	private String declaringClass;

	@XmlElement
	private String methodName;

	@XmlElement
	private String fileName;

	@XmlElement
	private Integer lineNumber;

	public StackTraceElementXml() {
		super();
	}

	public StackTraceElementXml(StackTraceElement stackTraceElement) {
		declaringClass = stackTraceElement.getClassName();
		methodName = stackTraceElement.getMethodName();
		fileName = stackTraceElement.getFileName();
		lineNumber = stackTraceElement.getLineNumber();
	}

	@Override
	public String declaringClass() {
		return declaringClass;
	}

	@Override
	public String methodName() {
		return methodName;
	}

	@Override
	public String fileName() {
		return fileName;
	}

	@Override
	public int lineNumber() {
		return lineNumber;
	}
}