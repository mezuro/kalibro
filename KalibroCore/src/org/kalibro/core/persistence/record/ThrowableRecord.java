package org.kalibro.core.persistence.record;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

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

	@OrderColumn(name = "\"index\"", nullable = false)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "throwable", orphanRemoval = true)
	private List<StackTraceElementRecord> stackTrace;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
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
		for (StackTraceElement element : stackTrace)
			this.stackTrace.add(new StackTraceElementRecord(element, this));
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
		for (int i = 0; i < converted.length; i++)
			converted[i] = stackTrace.get(i).convert();
		return converted;
	}

	@Override
	public Throwable cause() {
		return cause == null ? null : cause.convert();
	}
}