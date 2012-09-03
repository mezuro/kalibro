package org.kalibro.core;

import java.util.Set;

import org.kalibro.KalibroSettings;
import org.kalibro.client.KalibroClient;
import org.kalibro.core.model.enums.RepositoryType;

public final class Kalibro {

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
		return KalibroSettings.load().clientSide() ? new KalibroClient() : new KalibroLocal();
	}

	private Kalibro() {
		return;
	}
}