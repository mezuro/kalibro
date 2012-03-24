package org.kalibro.core.persistence.database.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.kalibro.core.util.DataTransferObject;

@Entity(name = "Error")
public class ErrorRecord implements DataTransferObject<Throwable> {

	@Id
	@OneToOne(optional = false)
	@JoinColumn(name = "projectName", nullable = false, referencedColumnName = "name")
	@SuppressWarnings("unused" /* used by JPA */)
	private ProjectRecord project;

	@Column
	private String message;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "error", orphanRemoval = true)
	private List<StackTraceElementRecord> stackTrace;

	public ErrorRecord() {
		super();
	}

	public ErrorRecord(Throwable error, ProjectRecord project) {
		this.project = project;
		message = error.getMessage();
		initializeStackTrace(error);
	}

	private void initializeStackTrace(Throwable error) {
		stackTrace = new ArrayList<StackTraceElementRecord>();
		for (int i = 0; i < error.getStackTrace().length; i++)
			stackTrace.add(new StackTraceElementRecord(error.getStackTrace()[i], this, i));
	}

	@Override
	public Throwable convert() {
		Throwable error = new Throwable(message);
		convertStackTrace(error);
		return error;
	}

	private void convertStackTrace(Throwable error) {
		StackTraceElement[] stackTraceConverted = new StackTraceElement[stackTrace.size()];
		for (StackTraceElementRecord element : stackTrace)
			element.putIntoStackTrace(stackTraceConverted);
		error.setStackTrace(stackTraceConverted);
	}
}