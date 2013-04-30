package org.kalibro.core.processing;

import java.util.SortedSet;

import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.kalibro.KalibroSettings;
import org.kalibro.MailSettings;
import org.kalibro.ProcessingObserver;
import org.kalibro.Repository;
import org.kalibro.core.concurrent.TaskListener;
import org.kalibro.core.concurrent.TaskReport;
import org.kalibro.core.persistence.ProcessingObserverDatabaseDao;
import org.kalibro.dao.DaoFactory;

public class MailSender implements TaskListener<Void> {

	private final Repository repository;

	public MailSender(Repository repository) {
		this.repository = repository;
	}

	@Override
	public void taskFinished(TaskReport<Void> report) {
		if (report.isTaskDone())
			sendEmail();
	}
	
	public void sendEmail() {
		MailSettings mailSettings = KalibroSettings.load().getMailSettings();
		Mailer mailer = mailSettings.createMailer();
		
		ProcessingObserverDatabaseDao processingObserverDatabaseDao = 
			(ProcessingObserverDatabaseDao) DaoFactory.getProcessingObserverDao();
		SortedSet<ProcessingObserver> observers = processingObserverDatabaseDao.
			observersOf(repository.getId());
		for (ProcessingObserver observer : observers)
			mailer.sendMail(observer.prepareEmailToSend(createEmptyEmailWithSender(mailSettings), repository));
	}
	
	private Email createEmptyEmailWithSender(MailSettings mailSettings) {
		Email email = new Email();
		email.setFromAddress(mailSettings.getSender(), mailSettings.getSenderMail());
		return email;
	}
}
