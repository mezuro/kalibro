package org.kalibro;

import java.util.Set;

import org.kalibro.core.ProjectStateChangeSupport;
import org.kalibro.core.ProjectStateListener;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.core.persistence.dao.DaoFactory;

public abstract class KalibroFacade {

	protected DaoFactory daoFactory;
	protected ProjectStateChangeSupport changeSupport;

	protected KalibroFacade() {
		daoFactory = createDaoFactory();
		changeSupport = new ProjectStateChangeSupport();
	}

	protected DaoFactory getDaoFactory() {
		return daoFactory;
	}

	protected abstract DaoFactory createDaoFactory();

	protected abstract Set<RepositoryType> getSupportedRepositoryTypes();

	protected abstract void processProject(String projectName);

	protected abstract void processPeriodically(String projectName, Integer periodInDays);

	protected void addProjectStateListener(Project project, ProjectStateListener listener) {
		changeSupport.addProjectStateListener(project.getName(), listener);
	}

	protected void removeProjectStateListener(ProjectStateListener listener) {
		changeSupport.removeProjectStateListener(listener);
	}

	protected void fireProjectStateChanged(Project project) {
		changeSupport.fireProjectStateChanged(project.getName(), project.getState());
	}
}