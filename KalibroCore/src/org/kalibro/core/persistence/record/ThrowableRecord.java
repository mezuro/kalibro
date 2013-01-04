package org.kalibro.core.persistence.record;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.kalibro.dto.ThrowableDto;

/**
 * Java Persistence API entity for {@link Throwable}.
 * 
 * @author Carlos Morais
 */
@Entity(name = "Throwable")
@Table(name = "\"THROWABLE\"")
public class ThrowableRecord extends ThrowableDto {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"", nullable = false)
	@SuppressWarnings("unused" /* used by JPA */)
	private Long id;

	@Lob
	@Column(name = "\"target_string\"", nullable = false)
	private String targetString;

	@Lob
	@Column(name = "\"message\"")
	private String message;

	@CascadeOnDelete
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "\"cause\"", referencedColumnName = "\"id\"")
	private ThrowableRecord cause;

	@CascadeOnDelete
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "throwable", orphanRemoval = true)
	private Collection<StackTraceElementRecord> stackTrace;

	public ThrowableRecord() {
		super();
	}

	public ThrowableRecord(Throwable throwable) {
		targetString = throwable.toString();
		message = throwable.getMessage();
		setCause(throwable.getCause());
		setStackTrace(throwable.getStackTrace());
	}

	private void setCause(Throwable cause) {
		if (cause != null)
			this.cause = new ThrowableRecord(cause);
	}

	private void setStackTrace(StackTraceElement[] stackTrace) {
		this.stackTrace = new ArrayList<StackTraceElementRecord>(stackTrace.length);
		for (int i = 0; i < stackTrace.length; i++)
			this.stackTrace.add(new StackTraceElementRecord(stackTrace[i], this, i));
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
		StackTraceElement[] converted = new StackTraceElement[stackTrace.size()];
		for (StackTraceElementRecord element : stackTrace)
			element.addTo(converted);
		return converted;
	}
}