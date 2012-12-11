package org.cvsanaly;

import java.io.File;
import java.sql.*;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.kalibro.*;
import org.kalibro.core.concurrent.Writer;

/**
 * Queries CVSAnalY database and writes results according to metrics queried.
 * 
 * @author Carlos Morais
 * @author Eduardo Morais
 */
class CvsAnalyDatabaseFetcher {

	private Set<CvsAnalyMetric> wantedMetrics;
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	CvsAnalyDatabaseFetcher(Set<NativeMetric> wantedMetrics) {
		this.wantedMetrics = CvsAnalyMetric.selectMetrics(wantedMetrics);
	}

	void queryMetrics(File databaseFile, Writer<NativeModuleResult> resultWriter) throws Exception {
		try {
			executeQuery(databaseFile);
			while (resultSet.next())
				resultWriter.write(getModuleResult());
		} finally {
			resultWriter.close();
			databaseFile.delete();
			closeConnection();
		}
	}

	private void executeQuery(File databaseFile) throws Exception {
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getAbsolutePath());
		statement = connection.createStatement();
		String query = "SELECT file_links.file_path";
		for (CvsAnalyMetric metric : wantedMetrics)
			query += ", metrics." + metric.getColumn();
		query += "\n" + IOUtils.toString(getClass().getResourceAsStream("query"));
		resultSet = statement.executeQuery(query);
	}

	private NativeModuleResult getModuleResult() throws SQLException {
		String filename = resultSet.getString("file_path");
		Module module = new Module(Granularity.CLASS, filename.split(Pattern.quote(File.separator)));
		NativeModuleResult moduleResult = new NativeModuleResult(module);
		for (CvsAnalyMetric metric : wantedMetrics)
			moduleResult.addMetricResult(new NativeMetricResult(metric, resultSet.getDouble(metric.getColumn())));
		return moduleResult;
	}

	private void closeConnection() throws SQLException {
		resultSet.close();
		statement.close();
		connection.close();
	}
}