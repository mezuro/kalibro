package org.kalibro;

import java.util.Set;

import org.kalibro.client.KalibroClient;
import org.kalibro.core.KalibroLocal;
import org.kalibro.core.ProjectStateListener;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.core.persistence.dao.*;
import org.kalibro.core.settings.KalibroSettings;

public final class Kalibro {

	private static KalibroFacade facade;
	private static KalibroSettings settings;

	public static boolean settingsFileExists() {
		return KalibroSettings.settingsFileExists();
	}

	public static KalibroSettings currentSettings() {
		if (settings == null)
			settings = KalibroSettings.load();
		return settings;
	}

	public static void changeSettings(KalibroSettings newSettings) {
		settings = newSettings;
		createFacade();
		newSettings.write();
	}

	public static BaseToolDao getBaseToolDao() {
		return getFacade().getDaoFactory().getBaseToolDao();
	}

	public static ConfigurationDao getConfigurationDao() {
		return getFacade().getDaoFactory().getConfigurationDao();
	}

	public static MetricConfigurationDao getMetricConfigurationDao() {
		return getFacade().getDaoFactory().getMetricConfigurationDao();
	}

	public static ProjectDao getProjectDao() {
		return getFacade().getDaoFactory().getProjectDao();
	}

	public static ProjectResultDao getProjectResultDao() {
		return getFacade().getDaoFactory().getProjectResultDao();
	}

	public static ModuleResultDao getModuleResultDao() {
		return getFacade().getDaoFactory().getModuleResultDao();
	}

	public static Set<RepositoryType> getSupportedRepositoryTypes() {
		return getFacade().getSupportedRepositoryTypes();
	}

	public static void processProject(String projectName) {
		getFacade().processProject(projectName);
	}

	public static void processPeriodically(String projectName, Integer periodInDays) {
		getFacade().processPeriodically(projectName, periodInDays);
	}

	public static void addProjectStateListener(Project project, ProjectStateListener listener) {
		getFacade().addProjectStateListener(project, listener);
	}

	public static void removeProjectStateListener(ProjectStateListener listener) {
		getFacade().removeProjectStateListener(listener);
	}

	public static void fireProjectStateChanged(Project project) {
		getFacade().fireProjectStateChanged(project);
	}

	private static KalibroFacade getFacade() {
		if (facade == null)
			createFacade();
		return facade;
	}

	private static void createFacade() {
		try {
			facade = currentSettings().isClient() ? new KalibroClient() : new KalibroLocal();
		} catch (KalibroException exception) {
			settings = null;
			throw exception;
		}
	}

	private Kalibro() {
		// Utility class
	}
}