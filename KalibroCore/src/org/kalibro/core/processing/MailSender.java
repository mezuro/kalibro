package org.kalibro.core.processing;

import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.kalibro.KalibroSettings;
import org.kalibro.MailSettings;

public class MailSender {

	private MailSettings mailSettings;

	public MailSender() {
		this.mailSettings = KalibroSettings.load().getMailSettings();
	}
	
	public void sendEmail(Email email) {
		Mailer mailer = mailSettings.createMailer();
		mailer.sendMail(email);
	}
	
	public Email createEmptyEmailWithSender() {
		Email email = new Email();
		email.setFromAddress(mailSettings.getSender(), mailSettings.getSenderMail());
		return email;
	}
}
