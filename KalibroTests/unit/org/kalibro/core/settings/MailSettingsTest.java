package org.kalibro.core.settings;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import org.codemonkey.simplejavamail.Mailer;
import org.codemonkey.simplejavamail.TransportStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.MailSettings;
import org.kalibro.TestCase;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MailSettings.class)
public class MailSettingsTest extends TestCase {

	private MailSettings settings = new MailSettings();

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDefaultSettings() {
		assertEquals("smtp.gmail.com", settings.getSmtpHost());
		assertEquals(465, settings.getSmtpPort().intValue());
		assertEquals("example@gmail.com", settings.getSenderMail());
		assertEquals("", settings.getPassword());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateMailer() throws Exception {
		Mailer mailer = mock(Mailer.class);
		whenNew(Mailer.class).withArguments(settings.getSmtpHost(), settings.getSmtpPort(), settings.getSenderMail(),
			settings.getPassword(), TransportStrategy.SMTP_SSL).thenReturn(mailer);
		assertSame(mailer, settings.createMailer());
	}
}