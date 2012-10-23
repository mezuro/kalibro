package org.checkstyle;

import static org.kalibro.core.Environment.logsDirectory;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.LocalizedMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
import org.kalibro.core.concurrent.Writer;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AuditEvent.class, CheckstyleListener.class, IOUtils.class, LocalizedMessage.class})
public class CheckstyleListenerTest extends UnitTest {

	private static final String CODE_PATH = "/code/directory/absolute/path";
	private static final String FILE_NAME = CODE_PATH + "/org/checkstyle/CheckstyleListenerTest.java";

	private CheckstyleMetric wantedMetric;
	private Writer<NativeModuleResult> resultWriter;

	private CheckstyleListener listener;

	@Before
	public void setUp() {
		resultWriter = mock(Writer.class);
		wantedMetric = loadFixture("fanOut", CheckstyleMetric.class);
		File codeDirectory = mock(File.class);
		when(codeDirectory.getAbsolutePath()).thenReturn(CODE_PATH);
		listener = new CheckstyleListener(codeDirectory, set((NativeMetric) wantedMetric), resultWriter);
	}

	@Test
	public void resultsShouldBeZeroForNoEvents() {
		shouldProduceResult(0.0);
	}

	@Test
	public void resultsShouldBeZeroForNoEventsWithoutValue() {
		shouldProduceResult(0.0, "{0}", "{0}");
	}

	@Test
	public void resultsShouldBeAggregationOfMessageValuesAccordingToWantedMetrics() {
		shouldProduceResult(10.0, "1", "2", "3", "4");
	}

	private void shouldProduceResult(Double value, String... values) {
		simulateCheckstyle(values);
		InOrder order = Mockito.inOrder(resultWriter);
		order.verify(resultWriter).write(deepEq(expectedResult(value)));
		order.verify(resultWriter).close();
	}

	private void simulateCheckstyle(String... values) {
		AuditEvent event = mockEvent("");
		listener.auditStarted(event);
		listener.fileStarted(event);
		for (String value : values)
			listener.addError(mockEvent(value));
		listener.fileFinished(event);
		listener.auditFinished(event);
	}

	private NativeModuleResult expectedResult(Double value) {
		Module module = new Module(Granularity.CLASS, "org", "checkstyle", "CheckstyleListenerTest.java");
		NativeModuleResult moduleResult = new NativeModuleResult(module);
		moduleResult.addMetricResult(new NativeMetricResult(wantedMetric, value));
		return moduleResult;
	}

	@Test
	public void shouldLogError() throws Exception {
		mockStatic(IOUtils.class);
		Throwable error = mock(Throwable.class);
		PrintStream logStream = mockLogStream();

		listener.addException(mockEvent(""), error);
		verifyStatic();
		IOUtils.write("\n\nError auditing " + FILE_NAME + "\n", logStream);
		verify(error).printStackTrace(logStream);
	}

	private PrintStream mockLogStream() throws Exception {
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		File file = mock(File.class);
		FileOutputStream fileStream = mock(FileOutputStream.class);
		PrintStream logStream = mock(PrintStream.class);

		whenNew(File.class).withArguments(logsDirectory(), "checkstyle." + today + ".log").thenReturn(file);
		whenNew(FileOutputStream.class).withArguments(file, true).thenReturn(fileStream);
		whenNew(PrintStream.class).withArguments(fileStream).thenReturn(logStream);
		return logStream;
	}

	@Test
	public void shouldNotThrowExceptionIfCannotLog() throws Exception {
		Throwable error = mock(Throwable.class);
		listener = spy(listener);
		doThrow(new IOException()).when(listener, "logError", FILE_NAME, error);

		listener.addException(mockEvent(""), error);
	}

	private AuditEvent mockEvent(String value) {
		AuditEvent event = mock(AuditEvent.class);
		LocalizedMessage localizedMessage = mock(LocalizedMessage.class);
		String messageKey = wantedMetric.getMessageKey();

		when(event.getFileName()).thenReturn(FILE_NAME);
		when(event.getLocalizedMessage()).thenReturn(localizedMessage);
		when(localizedMessage.getKey()).thenReturn(messageKey);
		when(event.getMessage()).thenReturn(messageKey + value);
		return event;
	}
}