package org.kalibro.core.persistence.record;

import javax.persistence.*;

import org.kalibro.dto.StackTraceElementDto;

/**
 * Java Persistence API entity for {@link StackTraceElement}.
 * 
 * @author Carlos Morais
 */
@Entity(name = "StackTraceElement")
@Table(name = "\"STACK_TRACE_ELEMENT\"")
public class StackTraceElementRecord extends StackTraceElementDto {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"throwable\"", referencedColumnName = "\"id\"")
	@SuppressWarnings("unused" /* used by JPA */)
	private ThrowableRecord throwable;

	@Id
	@GeneratedValue
	@Column(name = "\"id\"", nullable = false)
	@SuppressWarnings("unused" /* used by JPA */)
	private Long id;

	@Column(name = "\"declaring_class\"", nullable = false)
	private String declaringClass;

	@Column(name = "\"method_name\"", nullable = false)
	private String methodName;

	@Column(name = "\"file_name\"", nullable = false)
	private String fileName;

	@Column(name = "\"line_number\"", nullable = false)
	private Integer lineNumber;

	public StackTraceElementRecord() {
		super();
	}

	public StackTraceElementRecord(StackTraceElement element) {
		this(element, null);
	}

	public StackTraceElementRecord(StackTraceElement element, ThrowableRecord throwableRecord) {
		throwable = throwableRecord;
		declaringClass = element.getClassName();
		methodName = element.getMethodName();
		fileName = element.getFileName();
		lineNumber = element.getLineNumber();
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