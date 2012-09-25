package org.kalibro.core.processing;

import java.util.Collection;
import java.util.Map;

import javax.mail.Message.RecipientType;

import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.kalibro.KalibroSettings;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.model.Module;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;
import org.kalibro.dao.DaoFactory;

public class ProcessProjectTask extends VoidTask {

	private Project project;

	public ProcessProjectTask(String projectName) {
		project = DaoFactory.getProjectDao().getProject(projectName);
	}

	@Override
	protected void perform() {
		try {
			processProject();
		} catch (Throwable error) {
			reportError(error);
		}
	}

	private void processProject() {
		ProjectResult projectResult = new LoadSourceTask(project).executeSubTask();
		Map<Module, ModuleResult> resultMap = new CollectMetricsTask(projectResult).executeSubTask();
		Collection<ModuleResult> moduleResults = new AnalyzeResultsTask(projectResult, resultMap).executeSubTask();

		DaoFactory.getProjectResultDao().save(projectResult);
		saveModuleResults(moduleResults, projectResult);
		project.setState(ProjectState.READY);
		DaoFactory.getProjectDao().save(project);
		sendMail();
	}

	private void sendMail() {
		Mailer mailer = KalibroSettings.load().getMailSettings().createMailer();
		for (String mail : project.getMailsToNotify()) {
			Email email = new Email();
			email.addRecipient(mail, mail, RecipientType.TO);
			email.setSubject("Project " + project.getName() + " processing results");
			email.setText("Processing results in project " + project.getName() + " has finished succesfully.");
			mailer.sendMail(email);
		}
	}

	private void saveModuleResults(Collection<ModuleResult> moduleResults, ProjectResult projectResult) {
		ModuleResultDatabaseDao moduleResultDao = (ModuleResultDatabaseDao) DaoFactory.getModuleResultDao();
		for (ModuleResult moduleResult : moduleResults)
			moduleResultDao.save(moduleResult, projectResult);
	}

	private void reportError(Throwable error) {
		project.setError(error);
		DaoFactory.getProjectDao().save(project);
	}
}
