package org.kalibro.core.processing;

import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.kalibro.KalibroSettings;
import org.kalibro.MailSettings;

public abstract class MailSender {

	private static final MailSettings MAIL_SETTINGS = KalibroSettings.load().getMailSettings();

	public static void sendEmail(Email email) {
		Mailer mailer = MAIL_SETTINGS.createMailer();
		mailer.sendMail(email);
	}

	public static Email createEmptyEmailWithSender() {
		Email email = new Email();
		email.setFromAddress(MAIL_SETTINGS.getSender(), MAIL_SETTINGS.getSenderMail());
		return email;
	}
}
