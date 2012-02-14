package org.kalibro.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kalibro.core.model.enums.ProjectState;

public class ProjectStateChangeSupport {

	private Map<String, Set<ProjectStateListener>> listenerMap;

	public ProjectStateChangeSupport() {
		listenerMap = new HashMap<String, Set<ProjectStateListener>>();
	}

	public Set<String> getListenProjectNames() {
		return listenerMap.keySet();
	}

	public void addProjectStateListener(String projectName, ProjectStateListener listener) {
		if (! listenerMap.containsKey(projectName))
			listenerMap.put(projectName, new HashSet<ProjectStateListener>());
		listenerMap.get(projectName).add(listener);
	}

	public void removeProjectStateListener(ProjectStateListener listener) {
		Set<String> projectNames = new HashSet<String>(listenerMap.keySet());
		for (String projectName : projectNames) {
			Set<ProjectStateListener> listeners = listenerMap.get(projectName);
			listeners.remove(listener);
			if (listeners.isEmpty())
				listenerMap.remove(projectName);
		}
	}

	public void fireProjectStateChanged(String projectName, ProjectState newProjectState) {
		if (! listenerMap.containsKey(projectName))
			return;
		for (ProjectStateListener listener : listenerMap.get(projectName))
			new ProjectStateChangeFirer(projectName, newProjectState, listener).fire();
	}
}