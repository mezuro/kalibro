package org.kalibro.core.settings;

import org.codemonkey.simplejavamail.Mailer;
import org.codemonkey.simplejavamail.TransportStrategy;
import org.kalibro.core.abstractentity.AbstractEntity;

/**
 * SMTP settings for the service to send notifications (SSL).
 * 
 * @author Carlos Morais
 * @author Eduardo Morais
 */
public class MailSettings extends AbstractEntity<MailSettings> {

	private String smtpHost;
	private Integer smtpPort;
	private String senderMail;
	private String password;

	public MailSettings() {
		setSmtpHost("smtp.gmail.com");
		setSmtpPort(465);
		setSenderMail("example@gmail.com");
		setPassword("");
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public Integer getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(Integer smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getSenderMail() {
		return senderMail;
	}

	public void setSenderMail(String username) {
		this.senderMail = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Mailer createMailer() {
		return new Mailer(smtpHost, smtpPort, senderMail, password, TransportStrategy.SMTP_SSL);
	}
}