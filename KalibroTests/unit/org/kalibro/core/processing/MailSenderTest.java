package org.kalibro.core.processing;

import static org.junit.Assert.*;

import org.codemonkey.simplejavamail.Email;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroSettings;
import org.kalibro.MailSettings;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(KalibroSettings.class)
public class MailSenderTest extends UnitTest {
	
	private MailSettings mailSettings;
	private KalibroSettings kalibroSettings;
	private Email email;
	
	private static final String SENDER = "SenderTest";
	private static final String SENDER_MAIL = "SenderMailTest";
	
	@Before
	public void setUp() {
		mailSettings = mock(MailSettings.class);
		mockStatic(KalibroSettings.class);
		kalibroSettings = mock(KalibroSettings.class);
		when(KalibroSettings.load()).thenReturn(kalibroSettings);
		when(kalibroSettings.getMailSettings()).thenReturn(mailSettings);
		when(mailSettings.getSender()).thenReturn(SENDER);
		when(mailSettings.getSenderMail()).thenReturn(SENDER_MAIL);
	}

	@Test
	public void shouldCreateAnEmptyEmail() throws Exception {
		email = mock(Email.class);
		when(email.getSubject()).thenReturn("Batata");
		whenNew(Email.class).withNoArguments().thenReturn(email);
		assertEquals(email, MailSender.createEmptyEmailWithSender());
		verify(email).setFromAddress(any(String.class), any(String.class));
	}

	
}
