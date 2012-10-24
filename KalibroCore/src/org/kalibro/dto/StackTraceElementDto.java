package org.kalibro.dto;

/**
 * Data transfer object for {@link StackTraceElement}.
 * 
 * @author Carlos Morais
 */
public abstract class StackTraceElementDto extends DataTransferObject<StackTraceElement> {

	@Override
	public StackTraceElement convert() {
		return new StackTraceElement(declaringClass(), methodName(), fileName(), lineNumber());
	}

	public abstract String declaringClass();

	public abstract String methodName();

	public abstract String fileName();

	public abstract int lineNumber();
}