package org.kalibro.core.processing;

import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.kalibro.KalibroSettings;
import org.kalibro.MailSettings;

public abstract class MailSender {

	private static MailSettings mailSettings() {
		return KalibroSettings.load().getMailSettings();
	}

	public static void sendEmail(Email email) {
		Mailer mailer = mailSettings().createMailer();
		mailer.sendMail(email);
	}

	public static Email createEmptyEmailWithSender() {
		Email email = new Email();
		MailSettings mailSettings = mailSettings();
		email.setFromAddress(mailSettings.getSender(), mailSettings.getSenderMail());
		return email;
	}
}
