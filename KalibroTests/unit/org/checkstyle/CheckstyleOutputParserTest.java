package org.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AuditEvent.class)
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
			parser.addError(mockEvent("File length is 6 lines (max allowed is -1)."));
			parser.addError(mockEvent("Total number of methods is 1 (max allowed is -1)."));
			parser.auditFinished(null);
		}

		private AuditEvent mockEvent(String message) {
			AuditEvent event = PowerMockito.mock(AuditEvent.class);
			PowerMockito.when(event.getFileName()).thenReturn("HelloWorld.java");
			PowerMockito.when(event.getMessage()).thenReturn(message);
			return event;
		}
	}
}