package org.cvsanaly;

import java.io.File;
import java.sql.*;
import java.util.Random;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.concurrent.Writer;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CvsAnalyDatabaseFetcher.class, DriverManager.class})
public class CvsAnalyDatabaseFetcherTest extends UnitTest {

	private static final String PATH = "/cvs/analy/database/fetcher/test";
	private static final Double RESULT = new Random().nextDouble();

	private File databaseFile;
	private Writer<NativeModuleResult> resultWriter;

	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	private CvsAnalyDatabaseFetcher fetcher;

	@Before
	public void setUp() throws SQLException {
		databaseFile = mock(File.class);
		resultWriter = mock(Writer.class);
		mockFields();
		fetcher = new CvsAnalyDatabaseFetcher();
	}

	private void mockFields() throws SQLException {
		connection = mock(Connection.class);
		statement = mock(Statement.class);
		resultSet = mock(ResultSet.class);
		mockStatic(DriverManager.class);
		when(DriverManager.getConnection(anyString())).thenReturn(connection);
		when(connection.createStatement()).thenReturn(statement);
		when(statement.executeQuery(anyString())).thenReturn(resultSet);
	}

	@Test
	public void shouldOpenConnectionToDatabaseFile() throws Exception {
		when(databaseFile.getAbsolutePath()).thenReturn(PATH);

		queryMetrics();
		verifyStatic();
		DriverManager.getConnection("jdbc:sqlite:" + PATH);
	}

	@Test
	public void shouldQueryColumnsAccordingToWantedMetrics() throws Exception {
		NativeMetric loc = new NativeMetric("Lines of Code", Granularity.CLASS);
		NativeMetric halstead = new NativeMetric("Halstead Volume", Granularity.CLASS);

		String query = "SELECT file_links.file_path, metrics.loc, metrics.halstead_vol\n" + loadResource("query");
		queryMetrics(set(halstead, loc));
		verify(statement).executeQuery(query);
	}

	@Test
	public void shouldWriteResultAccordingToQuery() throws Exception {
		NativeMetric loc = loadFixture("loc", CvsAnalyMetric.class);
		when(resultSet.next()).thenAnswer(new NextAnswer());
		when(resultSet.getString("file_path")).thenReturn(PATH);
		when(resultSet.getDouble("loc")).thenReturn(RESULT);
		NativeModuleResult expected = new NativeModuleResult(new Module(Granularity.CLASS, PATH.split("/")));
		expected.addMetricResult(new NativeMetricResult(loc, RESULT));

		queryMetrics(set(loc));
		verify(resultWriter).write(deepEq(expected));
	}

	@Test
	public void shouldCloseEverythingAfterQuerying() throws Exception {
		queryMetrics();
		shouldCloseEverything();
	}

	@Test
	public void shouldCloseEvenIfExceptionIsThrown() throws Exception {
		SQLException exception = new SQLException();
		when(resultSet.next()).thenThrow(exception);
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				queryMetrics();
			}
		}).doThrow(exception);
		shouldCloseEverything();
	}

	private void shouldCloseEverything() throws SQLException {
		InOrder order = Mockito.inOrder(resultWriter, databaseFile, resultSet, statement, connection);
		order.verify(resultWriter).close();
		order.verify(databaseFile).delete();
		order.verify(resultSet).close();
		order.verify(statement).close();
		order.verify(connection).close();
	}

	private void queryMetrics() throws Exception {
		queryMetrics(CvsAnalyMetric.supportedMetrics());
	}

	private void queryMetrics(Set<NativeMetric> wantedMetrics) throws Exception {
		fetcher.queryMetrics(databaseFile, wantedMetrics, resultWriter);
	}

	private static class NextAnswer implements Answer<Boolean> {

		private int count;

		@Override
		public Boolean answer(InvocationOnMock invocation) throws Throwable {
			return count++ == 0;
		}
	}
}