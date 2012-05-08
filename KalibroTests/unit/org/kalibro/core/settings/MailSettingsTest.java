package org.kalibro.core.settings;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.codemonkey.simplejavamail.Mailer;
import org.codemonkey.simplejavamail.TransportStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MailSettings.class)
public class MailSettingsTest extends KalibroTestCase {

	private MailSettings settings = new MailSettings();

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDefaultSettings() {
		assertEquals("smtp.gmail.com", settings.getSmtpHost());
		assertEquals(465, settings.getSmtpPort().intValue());
		assertEquals("example@gmail.com", settings.getSenderMail());
		assertEquals("", settings.getPassword());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkMapConstructor() {
		Map<?, ?> map = SettingsFixtures.mailSettingsMap();
		settings = new MailSettings(map);
		assertEquals(map.get("smtp_host"), settings.getSmtpHost());
		assertEquals(map.get("smtp_port"), settings.getSmtpPort());
		assertEquals(map.get("sender_mail"), settings.getSenderMail());
		assertEquals(map.get("password"), settings.getPassword());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkToString() throws IOException {
		String expected = IOUtils.toString(getClass().getResourceAsStream("mail.settings"));
		assertEquals(expected, "" + settings);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateMailer() throws Exception {
		Mailer mailer = mock(Mailer.class);
		whenNew(Mailer.class).withArguments(settings.getSmtpHost(), settings.getSmtpPort(), settings.getSenderMail(),
			settings.getPassword(), TransportStrategy.SMTP_SSL).thenReturn(mailer);
		assertSame(mailer, settings.createMailer());
	}
}