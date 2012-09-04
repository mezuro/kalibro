package org.kalibro;

import org.codemonkey.simplejavamail.Mailer;
import org.codemonkey.simplejavamail.TransportStrategy;
import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.Print;

/**
 * SMTP settings for the service to send notifications (SSL).
 * 
 * @author Carlos Morais
 * @author Eduardo Morais
 */
public class MailSettings extends AbstractEntity<MailSettings> {

	@Print (order = 1)
	private String smtpHost;
	@Print (order = 2)
	private Integer smtpPort;
	@Print (order = 3)
	private String senderMail;
	@Print (order = 4, comment = "This password shall be stored securely in the future\n")
	private String password;

	public MailSettings() {
		setSmtpHost("smtp.gmail.com");
		setSmtpPort(465);
		setSenderMail("example@gmail.com");
		setPassword("securepassword");
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