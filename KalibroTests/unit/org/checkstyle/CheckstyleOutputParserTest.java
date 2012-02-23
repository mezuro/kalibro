package org.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.LocalizedMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AuditEvent.class, LocalizedMessage.class})
public class CheckstyleOutputParserTest extends KalibroTestCase {

	private CheckstyleOutputParser parser;

	@Before
	public void setUp() {
		parser = new CheckstyleOutputParser();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldParseMetricResults() throws Exception {
		parser.auditStarted(null);
		new CheckstyleSimulator(false).perform();
		assertDeepEquals(parser.getResults(), CheckstyleStub.result());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldParseMetricResultsConcurrent() throws Exception {
		parser.auditStarted(null);
		new CheckstyleSimulator(true).executeInBackground();
		assertDeepEquals(parser.getResults(), CheckstyleStub.result());
	}

	private class CheckstyleSimulator extends Task {

		private boolean wait;

		public CheckstyleSimulator(boolean wait) {
			this.wait = wait;
		}

		@Override
		public void perform() throws Exception {
			if (wait)
				Thread.sleep(UNIT_TIMEOUT / 5);
			parser.addError(mockEvent("maxLen.file", "6"));
			parser.addError(mockEvent("too.many.methods", "1"));
			parser.auditFinished(null);
		}

		private AuditEvent mockEvent(String messageKey, String message) {
			LocalizedMessage localizedMessage = PowerMockito.mock(LocalizedMessage.class);
			PowerMockito.when(localizedMessage.getKey()).thenReturn(messageKey);

			AuditEvent event = PowerMockito.mock(AuditEvent.class);
			PowerMockito.when(event.getLocalizedMessage()).thenReturn(localizedMessage);
			PowerMockito.when(event.getFileName()).thenReturn("HelloWorld.java");
			PowerMockito.when(event.getMessage()).thenReturn(message);
			return event;
		}
	}
}