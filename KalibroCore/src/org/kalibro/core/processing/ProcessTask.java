package org.kalibro.core.processing;

import java.io.File;

import javax.mail.Message.RecipientType;

import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.kalibro.*;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.persistence.DatabaseDaoFactory;

/**
 * Performs a {@link Processing} for a {@link Repository} according to its {@link Configuration}.
 * 
 * @author Carlos Morais
 */
public class ProcessTask extends VoidTask {

	Processing processing;

	public ProcessTask(Repository repository) {
		processing = new DatabaseDaoFactory().createProcessingDao().createProcessingFor(repository);
	}

	@Override
	protected void perform() {
		File codeDirectory = new LoadSourceTask(processing).execute();
		Producer<NativeModuleResult> resultProducer = new Producer<NativeModuleResult>();
		new CollectMetricsTask(processing, codeDirectory, resultProducer).executeInBackground();
		new AnalyzeResultsTask(processing, resultProducer).execute();
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
}