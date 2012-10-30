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

	@Column(name = "\"throwable_class\"", nullable = false)
	private String throwableClass;

	@Column(name = "\"detail_message\"")
	private String detailMessage;

	@CascadeOnDelete
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "throwable", orphanRemoval = true)
	private Collection<StackTraceElementRecord> stackTrace;

	@CascadeOnDelete
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "\"cause\"", referencedColumnName = "\"id\"")
	private ThrowableRecord cause;

	public ThrowableRecord() {
		super();
	}

	public ThrowableRecord(Throwable throwable) {
		throwableClass = throwable.getClass().getName();
		detailMessage = throwable.getMessage();
		setStackTrace(throwable.getStackTrace());
		setCause(throwable.getCause());
	}

	private void setStackTrace(StackTraceElement[] stackTrace) {
		this.stackTrace = new ArrayList<StackTraceElementRecord>(stackTrace.length);
		for (int i = 0; i < stackTrace.length; i++)
			this.stackTrace.add(new StackTraceElementRecord(stackTrace[i], this, i));
	}

	private void setCause(Throwable cause) {
		if (cause != null)
			this.cause = new ThrowableRecord(cause);
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
	public StackTraceElement[] stackTrace() {
		StackTraceElement[] converted = new StackTraceElement[stackTrace.size()];
		for (StackTraceElementRecord element : stackTrace)
			element.addTo(converted);
		return converted;
	}

	@Override
	public Throwable cause() {
		return cause == null ? null : cause.convert();
	}
}