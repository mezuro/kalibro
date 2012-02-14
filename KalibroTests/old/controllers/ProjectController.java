package org.kalibro.desktop.old.controllers;

import static org.kalibro.desktop.old.utilities.DialogUtils.*;

import java.io.File;

import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.kalibro.Kalibro;
import org.kalibro.core.ProjectStateListener;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.core.persistence.ModuleResultCsvExporter;
import org.kalibro.desktop.old.listeners.ProjectFrameListener;
import org.kalibro.desktop.old.project.ProjectFrame;
import org.kalibro.desktop.swingextension.dialog.ErrorDialog;
import org.kalibro.desktop.swingextension.dialog.FileChooser;

public class ProjectController implements ProjectFrameListener, ProjectStateListener {

	private ProjectFrame frame;

	private boolean isNew, changed;
	private Project project;
	private Configuration configuration;

	public ProjectController(JDesktopPane desktopPane) throws Exception {
		isNew = true;
		frame = new ProjectFrame(this);
		desktopPane.add(frame);
		frame.setSelected(true);
		changed();
	}

	public ProjectController(JDesktopPane desktopPane, Project project) throws Exception {
		this(desktopPane);
		this.project = project;
		isNew = false;
		frame.project(project);
		initializeState();
		saved();
	}

	private void initializeState() throws Exception {
		ProjectState projectState = project.getState();
		frame.projectState(project, projectState);
		if (projectState == ProjectState.READY) {
			ProjectResult result = Kalibro.getProjectResultDao().getLastResultOf(project.getName());
//			if (project.hasConfiguration()) {
//				configuration = project.getConfiguration();
//				frame.result(result, configuration);
//			} else
//				frame.result(result);
			frame.setMaximum(true);
		}
	}

	private void changed() {
		String title = (project == null) ? "New project" : "Project - " + project + "*";
		frame.setTitle(title);
		changed = true;
	}

	@Override
	public void projectChanged() {
		changed();
	}

	private void saveNewProject() {
		Kalibro.getProjectDao().save(project);
		Kalibro.addProjectStateListener(project, this);
		Kalibro.processProject(project.getName());
		isNew = false;
	}

	private void saved() {
		frame.setTitle("Project - " + project);
		changed = false;
	}

	@Override
	public void projectSave() {
		message(
			frame,
			"The task of saving and analyzing project is server side.\nIt can take several minutes it finishes, but you can keep working with Kalibro",
			"Saving project");
		try {
			project = frame.project();
			if (isNew || ! Kalibro.getProjectResultDao().hasResultsFor(project.getName()))
				saveNewProject();
			else
				Kalibro.getProjectDao().save(project);
			frame.project(project);
			saved();
		} catch (Exception exception) {
			new ErrorDialog(frame).show(exception);
		}
	}

	private boolean closingConfirmed() {
		String message = "Project has been modified. Save changes?";
		int answer = yesNoCancelConfirmation(frame, message, "Save project");
		if (answer == JOptionPane.YES_OPTION)
			projectSave();
		return (answer != JOptionPane.CANCEL_OPTION);
	}

	@Override
	public void frameClosing() {
		if (! changed || closingConfirmed())
			frame.dispose();
	}

	private void validateState(ProjectState state) {
		if (state == ProjectState.ERROR) {
			Kalibro.removeProjectStateListener(this);
			new ErrorDialog(frame).show(project.getError());
		}
	}

	private void projectAnalyzed() {
		Kalibro.removeProjectStateListener(this);
		ProjectResult result = Kalibro.getProjectResultDao().getLastResultOf(project.getName());
//		if (project.hasConfiguration()) {
//			configuration = project.getConfiguration();
//			frame.result(result, configuration);
//		} else
//			frame.result(result);
	}

	@Override
	public void projectStateChanged(String projectName, ProjectState newProjectState) {
		try {
			frame.projectState(project, newProjectState);
			validateState(newProjectState);
			if (newProjectState == ProjectState.READY)
				projectAnalyzed();
		} catch (Exception exception) {
			new ErrorDialog(frame).show(exception);
		}
	}

	@Override
	public void chooseConfiguration() {
		try {
			final String chosen = KalibroController.chooseConfiguration(frame, "Choose configuration", true);
			if (chosen == null)
				return;

			showWaitingDialog("Applying configuration...");
			class ApplyConfigurationTask extends SwingWorker<Object, Object> {

				@Override
				protected Object doInBackground() throws Exception {
					frame.setEnabled(false);

					ProjectResult result = Kalibro.getProjectResultDao().getLastResultOf(project.getName());
					if (chosen.equals("<none>")) {
						configuration = null;
						frame.result(result);
					} else {
						configuration = Kalibro.getConfigurationDao().getConfiguration(chosen);
						frame.result(result, configuration);
					}

					hideWaitingDialog();
					frame.setEnabled(true);
					return null;
				}
			}
			ApplyConfigurationTask controllerTask = new ApplyConfigurationTask();
			controllerTask.execute();

		} catch (Exception exception) {
			new ErrorDialog(frame).show(exception);
		}
	}

	@Override
	public void requestAnalysis() {
		try {
			project = frame.project();
			Kalibro.addProjectStateListener(project, this);
			Kalibro.processProject(project.getName());
		} catch (Exception exception) {
			new ErrorDialog(frame).show(exception);
		}
	}

	@Override
	public void exportResults(ModuleResult moduleResult) {
		try {
			String suggestion = moduleResult.getModule().toString() + ".csv";
			FileChooser fileChooser = new FileChooser(frame);
			if (fileChooser.chooseFileToSave(suggestion))
				new ModuleResultCsvExporter(moduleResult).exportTo(fileChooser.getChosenFile());
		} catch (Exception exception) {
			new ErrorDialog(frame).show(exception);
		}
	}

	@Override
	public String fillFilePath() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int status = fileChooser.showOpenDialog(frame);

		String path = "";
		if (status == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			path = file.getPath();
		}
		return path;
	}
}