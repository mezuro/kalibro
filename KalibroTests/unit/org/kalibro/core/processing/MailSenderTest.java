package org.kalibro.core.processing;

import static org.junit.Assert.*;

import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroSettings;
import org.kalibro.MailSettings;
import org.kalibro.ServerSettings;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({KalibroSettings.class, MailSender.class})
public class MailSenderTest extends UnitTest {

	private MailSettings mailSettings;
	private ServerSettings serverSettings;
	private Mailer mailer;

	private static final String SENDER = "SenderTest";
	private static final String SENDER_MAIL = "SenderMailTest";

	@Before
	public void setUp() {
		mockMailSettings();
		mockStatic(KalibroSettings.class);
		KalibroSettings kalibroSettings = mock(KalibroSettings.class);
		serverSettings = mock(ServerSettings.class);
		when(KalibroSettings.load()).thenReturn(kalibroSettings);
		when(kalibroSettings.getServerSettings()).thenReturn(serverSettings);
		when(serverSettings.getMailSettings()).thenReturn(mailSettings);
	}

	private void mockMailSettings() {
		mailSettings = mock(MailSettings.class);
		mailer = mock(Mailer.class);
		when(mailSettings.createMailer()).thenReturn(mailer);
		when(mailSettings.getSender()).thenReturn(SENDER);
		when(mailSettings.getSenderMail()).thenReturn(SENDER_MAIL);
	}

	@Test
	public void shoudSendEmail() {
		MailSender.sendEmail(new Email());
		verify(mailer, once()).sendMail(any(Email.class));
	}

	@Test
	public void shouldCreateAnEmptyEmail() throws Exception {
		Email email = mock(Email.class);
		whenNew(Email.class).withNoArguments().thenReturn(email);
		assertSame(email, MailSender.createEmptyEmailWithSender());
		verify(email, once()).setFromAddress(SENDER, SENDER_MAIL);
	}
}
