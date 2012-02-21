package org.kalibro.core;

import org.kalibro.core.model.enums.ProjectState;

public interface ProjectStateListener {

	void projectStateChanged(String projectName, ProjectState newProjectState);
}