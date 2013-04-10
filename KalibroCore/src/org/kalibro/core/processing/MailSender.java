package org.kalibro.core.processing;

import java.util.SortedSet;

import org.codemonkey.simplejavamail.Mailer;
import org.kalibro.KalibroSettings;
import org.kalibro.ProcessingNotification;
import org.kalibro.Repository;
import org.kalibro.core.concurrent.TaskListener;
import org.kalibro.core.concurrent.TaskReport;
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

	private void sendEmail() {
		Mailer mailer = KalibroSettings.load().getMailSettings().createMailer();
		SortedSet<ProcessingNotification> notifications = DaoFactory.getProcessingNotificationDao().
			notificationsOf(repository);
		for (ProcessingNotification notification : notifications)
			mailer.sendMail(notification.createEmail());
	}
}