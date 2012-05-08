package org.kalibro.core.settings;

import java.util.Map;

import org.codemonkey.simplejavamail.Mailer;
import org.codemonkey.simplejavamail.TransportStrategy;

public class MailSettings {

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

	public MailSettings(Map<?, ?> settingsMap) {
		setSmtpHost("" + settingsMap.get("smtp_host"));
		setSmtpPort((Integer) settingsMap.get("smtp_port"));
		setSenderMail("" + settingsMap.get("sender_mail"));
		setPassword("" + settingsMap.get("password"));
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

	@Override
	public String toString() {
		return "    mail: # SMTP settings for the service to send notifications (SSL)\n" +
			"        smtp_host: \"" + smtpHost + "\"\n" +
			"        smtp_port: " + smtpPort + "\n" +
			"        sender_mail: \"" + senderMail + "\"\n" +
			"        password: \"" + password + "\"\n";
	}

	public Mailer createMailer() {
		return new Mailer(smtpHost, smtpPort, senderMail, password, TransportStrategy.SMTP_SSL);
	}
}