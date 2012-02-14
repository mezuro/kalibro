package org.kalibro.core.persistence.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.eclipse.persistence.annotations.PrimaryKey;
import org.kalibro.core.util.DataTransferObject;

@Entity(name = "StackTraceElement")
@PrimaryKey(columns = {@Column(name = "projectName"), @Column(name = "\"index\"")})
public class StackTraceElementRecord implements DataTransferObject<StackTraceElement> {

	@ManyToOne(optional = false)
	@JoinColumn(name = "projectName", nullable = false, referencedColumnName = "projectName")
	@SuppressWarnings("unused" /* used by JPA */)
	private ErrorRecord error;

	@Column(name = "\"index\"", nullable = false)
	private Integer index;

	@Column(nullable = false)
	private String declaringClass;

	@Column(nullable = false)
	private String methodName;

	@Column(nullable = false)
	private String fileName;

	@Column(nullable = false)
	private Integer lineNumber;

	public StackTraceElementRecord() {
		super();
	}

	public StackTraceElementRecord(StackTraceElement stackTraceElement, ErrorRecord error, Integer index) {
		this.error = error;
		this.index = index;
		declaringClass = stackTraceElement.getClassName();
		methodName = stackTraceElement.getMethodName();
		fileName = stackTraceElement.getFileName();
		lineNumber = stackTraceElement.getLineNumber();
	}

	@Override
	public StackTraceElement convert() {
		return new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
	}

	public void putIntoStackTrace(StackTraceElement[] stackTrace) {
		stackTrace[index] = convert();
	}
}