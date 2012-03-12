package org.kalibro.desktop.old.controllers;

import static org.kalibro.desktop.old.utilities.DialogUtils.*;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import org.kalibro.Kalibro;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.Project;
import org.kalibro.desktop.old.KalibroFrame;
import org.kalibro.desktop.old.listeners.KalibroFrameListener;
import org.kalibro.desktop.settings.SettingsDialog;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;

public class KalibroController implements KalibroFrameListener {

	private static KalibroFrame frame;

	public static KalibroFrame frame() {
		return frame;
	}

	public static String chooseConfiguration(Component parent, String title, boolean withNone) {
		List<String> configurations = Kalibro.getConfigurationDao().getConfigurationNames();
		if (withNone)
			configurations.add("<none>");
		return choose(parent, "Configuration", title, configurations.toArray(new String[0]));
	}

	public KalibroController() {
		frame = new KalibroFrame(this);
		frame.setVisible(true);
	}

	private boolean noConfiguration() {
		boolean noConfiguration = Kalibro.getConfigurationDao().getConfigurationNames().isEmpty();
		if (noConfiguration)
			message(frame, "No configurations found", "No configuration");
		return noConfiguration;
	}

	@Override
	public void openConfiguration() {
		try {
			if (noConfiguration())
				return;
			String configuration = chooseConfiguration(frame, "Open configuration", false);
			if (configuration != null)
				new ConfigurationController(frame.desktopPane(), configuration);
		} catch (Exception exception) {
			new ErrorDialog(frame).show(exception);
		}
	}

	@Override
	public void removeConfiguration() {
		try {
			if (noConfiguration())
				return;
			String configuration = chooseConfiguration(frame, "Remove configuration", false);
			if (configuration != null)
				Kalibro.getConfigurationDao().removeConfiguration(configuration);
		} catch (Exception exception) {
			new ErrorDialog(frame).show(exception);
		}
	}

	@Override
	public void newProject() {
		try {
			new ProjectController(frame.desktopPane());
		} catch (Exception exception) {
			new ErrorDialog(frame).show(exception);
		}
	}

	private boolean noProject() {
		boolean noProject = Kalibro.getProjectDao().getProjectNames().isEmpty();
		if (noProject)
			message(frame, "No projects found", "No project");
		return noProject;
	}

	private Project chooseProject(Component parent, String title) throws Exception {
		showWaitingDialog("Loading projects list...");
		class ListProjectTask extends SwingWorker<List<Project>, Object> {

			@Override
			protected List<Project> doInBackground() throws Exception {
				frame.setEnabled(false);
				List<Project> projects = new ArrayList<Project>();
				for (String projectName : Kalibro.getProjectDao().getProjectNames())
					projects.add(Kalibro.getProjectDao().getProject(projectName));
				return projects;
			}
		}
		ListProjectTask listTask = new ListProjectTask();
		listTask.execute();
		final List<Project> projects = listTask.get();

		class CreateOptionsTask extends SwingWorker<List<String>, Object> {

			@Override
			protected List<String> doInBackground() throws Exception {
				// JOB ------------------
				List<String> options = new ArrayList<String>();
				for (Project project : projects)
					options.add(project + " (" + project.getState() + ")");
				// JOB ------------------

				hideWaitingDialog();
				frame.setEnabled(true);
				return options;
			}
		}
		CreateOptionsTask optionsTask = new CreateOptionsTask();
		optionsTask.execute();
		List<String> options = optionsTask.get();

		String chosen = choose(parent, "Project", title, options.toArray(new String[options.size()]));
		return (chosen == null) ? null : projects.get(options.indexOf(chosen));
	}

	@Override
	public void openProject() {
		try {
			if (noProject())
				return;
			final Project chosen = chooseProject(frame, "Open project");
			if (chosen != null) {
				showWaitingDialog("Openning project...");
				class NeWControllerTask extends SwingWorker<Object, Object> {

					@Override
					protected Object doInBackground() throws Exception {
						frame.setEnabled(false);
						new ProjectController(frame.desktopPane(), chosen);
						hideWaitingDialog();
						frame.setEnabled(true);
						return null;
					}
				}
				NeWControllerTask controllerTask = new NeWControllerTask();
				controllerTask.execute();
			}
		} catch (Exception exception) {
			new ErrorDialog(frame).show(exception);
		}
	}

	@Override
	public void removeProject() {
		try {
			if (noProject())
				return;
			final Project chosen = chooseProject(frame, "Remove project");
			if (chosen != null) {
				showWaitingDialog("Removing project...");
				class RemoveTask extends SwingWorker<Object, Object> {

					@Override
					protected Object doInBackground() throws Exception {
						frame.setEnabled(false);
						Kalibro.getProjectDao().removeProject(chosen.getName());
						hideWaitingDialog();
						frame.setEnabled(true);
						return null;
					}
				}
				RemoveTask controllerTask = new RemoveTask();
				controllerTask.execute();
			}
		} catch (Exception exception) {
			new ErrorDialog(frame).show(exception);
		}
	}

	private boolean allClosed() {
		boolean allClosed = frame.desktopPane().getAllFrames().length == 0;
		if (! allClosed) {
			String message = "All projects and configurations should be closed before editing preferences.";
			message(frame, message, "Close all");
		}
		return allClosed;
	}

	@Override
	public void editSettings() {
		if (allClosed())
			new SettingsDialog().setVisible(true);
	}
}