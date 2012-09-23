package org.kalibro.service.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.dto.DataTransferObject;

@XmlRootElement(name = "stackTraceElement")
@XmlAccessorType(XmlAccessType.FIELD)
public class StackTraceElementXml extends DataTransferObject<StackTraceElement> {

	private String declaringClass;
	private String methodName;
	private String fileName;
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
	public StackTraceElement convert() {
		return new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
	}
}