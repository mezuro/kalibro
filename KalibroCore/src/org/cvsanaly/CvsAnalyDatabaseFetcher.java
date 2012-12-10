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

	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	void queryMetrics(
		File databaseFile, Set<NativeMetric> wantedMetrics, Writer<NativeModuleResult> resultWriter) throws Exception {
		try {
			Set<CvsAnalyMetric> metrics = CvsAnalyMetric.selectMetrics(wantedMetrics);
			executeQuery(databaseFile, metrics);
			while (resultSet.next())
				resultWriter.write(getModuleResult(metrics));
		} finally {
			resultWriter.close();
			databaseFile.delete();
			closeConnection();
		}
	}

	private void executeQuery(File databaseFile, Set<CvsAnalyMetric> wantedMetrics) throws Exception {
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getAbsolutePath());
		statement = connection.createStatement();
		String query = "SELECT file_links.file_path";
		for (CvsAnalyMetric metric : wantedMetrics)
			query += ", metrics." + metric.getColumn();
		query += "\n" + IOUtils.toString(getClass().getResourceAsStream("query"));
		resultSet = statement.executeQuery(query);
	}

	private NativeModuleResult getModuleResult(Set<CvsAnalyMetric> metrics) throws SQLException {
		String filename = resultSet.getString("file_path");
		Module module = new Module(Granularity.CLASS, filename.split(Pattern.quote(File.separator)));
		NativeModuleResult moduleResult = new NativeModuleResult(module);
		for (CvsAnalyMetric metric : metrics)
			moduleResult.addMetricResult(new NativeMetricResult(metric, resultSet.getDouble(metric.getColumn())));
		return moduleResult;
	}

	private void closeConnection() throws SQLException {
		resultSet.close();
		statement.close();
		connection.close();
	}
}