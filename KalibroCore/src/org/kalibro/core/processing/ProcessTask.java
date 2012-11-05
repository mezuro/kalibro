package org.kalibro.core.processing;

import java.io.File;

import javax.mail.Message.RecipientType;

import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.kalibro.*;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.concurrent.TaskListener;
import org.kalibro.core.concurrent.TaskReport;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.persistence.DatabaseDaoFactory;

/**
 * Performs a {@link Processing} for a {@link Repository} according to its {@link Configuration}.
 * 
 * @author Carlos Morais
 */
public class ProcessTask extends VoidTask implements TaskListener<Void> {

	File codeDirectory;
	Processing processing;
	DatabaseDaoFactory daoFactory;
	Producer<NativeModuleResult> resultProducer;

	public ProcessTask(Repository repository) {
		daoFactory = new DatabaseDaoFactory();
		processing = daoFactory.createProcessingDao().createProcessingFor(repository);
		resultProducer = new Producer<NativeModuleResult>();
	}

	@Override
	protected void perform() {
		new LoadingTask().prepare(this).execute();
		new CollectingTask().prepare(this).executeInBackground();
		new AnalyzingTask().prepare(this).execute();
		sendMail();
	}

	private void sendMail() {
		Mailer mailer = KalibroSettings.load().getMailSettings().createMailer();
		Email email = new Email();
		Repository repository = processing.getRepository();
		email.setSubject("Repository " + repository.getCompleteName() + " processing results");
		email.setText("Processing results in repository " + repository.getName() + " has finished succesfully.");
		for (String mail : repository.getMailsToNotify()) {
			email.addRecipient(mail, mail, RecipientType.TO);
		}
		mailer.sendMail(email);
	}

	@Override
	public synchronized void taskFinished(TaskReport<Void> report) {
		processing.setStateTime(getTaskState(report), report.getExecutionTime());
		if (processing.getState().isTemporary())
			updateState(report);
		daoFactory.createProcessingDao().save(processing);
	}

	private void updateState(TaskReport<Void> report) {
		if (report.isTaskDone())
			processing.setState(getTaskState(report).nextState());
		else
			processing.setError(report.getError());
	}

	private ProcessState getTaskState(TaskReport<Void> report) {
		String taskClassName = report.getTask().getClass().getSimpleName();
		return ProcessState.valueOf(taskClassName.replace("Task", "").toUpperCase());
	}
}