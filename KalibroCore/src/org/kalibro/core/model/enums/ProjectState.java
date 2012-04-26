package org.kalibro.core.model.enums;

import org.kalibro.core.util.Identifier;

public enum ProjectState {

	NEW("Project $projectName was not processed"),
	LOADING("Loading $projectName from repository"),
	COLLECTING("Collecting metric values for $projectName"),
	ANALYZING("Processing metric results for $projectName"),
	READY("Processing of $projectName done"),
	ERROR("Error while processing $projectName");

	private String messageTemplate;

	private ProjectState(String messageTemplate) {
		this.messageTemplate = messageTemplate;
	}

	@Override
	public String toString() {
		return Identifier.fromConstant(name()).asText();
	}

	public boolean isTemporary() {
		return name().endsWith("ING");
	}

	public String getMessage(String projectName) {
		return messageTemplate.replace("$projectName", projectName);
	}
}