package org.kalibro.core;

import java.util.Set;

import org.kalibro.KalibroException;
import org.kalibro.KalibroSettings;
import org.kalibro.client.KalibroClient;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.core.persistence.dao.*;

public final class Kalibro {

	private static KalibroFacade facade;
	private static KalibroSettings settings;

	public static boolean settingsFileExists() {
		return KalibroSettings.exists();
	}

	public static KalibroSettings currentSettings() {
		if (settings == null)
			settings = KalibroSettings.load();
		return settings;
	}

	public static void changeSettings(KalibroSettings newSettings) {
		settings = newSettings;
		createFacade();
		newSettings.save();
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

	public static Integer getProcessPeriod(String projectName) {
		return getFacade().getProcessPeriod(projectName);
	}

	public static void cancelPeriodicProcess(String projectName) {
		getFacade().cancelPeriodicProcess(projectName);
	}

	private static KalibroFacade getFacade() {
		if (facade == null)
			createFacade();
		return facade;
	}

	private static void createFacade() {
		try {
			facade = currentSettings().clientSide() ? new KalibroClient() : new KalibroLocal();
		} catch (KalibroException exception) {
			settings = null;
			throw exception;
		}
	}

	private Kalibro() {
		return;
	}
}