package org.kalibro;

import org.kalibro.core.Identifier;

/**
 * State of the process of automatically loading, analyzing and evaluating source code.
 * 
 * @author Carlos Morais
 */
public enum ProcessState {

	LOADING("Loading $name from repository"),
	COLLECTING("Collecting metric values for $name"),
	ANALYZING("Processing metric results for $name"),
	READY("Processing of $name done"),
	ERROR("Error while processing $name");

	private String messageTemplate;

	private ProcessState(String messageTemplate) {
		this.messageTemplate = messageTemplate;
	}

	@Override
	public String toString() {
		return Identifier.fromConstant(name()).asText();
	}

	public boolean isTemporary() {
		return name().endsWith("ING");
	}

	public ProcessState nextState() {
		return values()[(ordinal() + 1) % values().length];
	}

	public String getMessage(String name) {
		return messageTemplate.replace("$name", name);
	}
}