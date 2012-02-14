package org.kalibro.core.settings;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

import java.util.HashMap;
import java.util.Map;

import org.kalibro.core.model.abstracts.AbstractEntity;

public class DatabaseSettings extends AbstractEntity<DatabaseSettings> {

	private SupportedDatabase databaseType;
	private String jdbcUrl;
	private String username;
	private String password;

	public DatabaseSettings() {
		setDatabaseType(SupportedDatabase.MYSQL);
		setJdbcUrl("jdbc:mysql://localhost:3306/kalibro");
		setUsername("kalibro");
		setPassword("kalibro");
	}

	public DatabaseSettings(Map<?, ?> settingsMap) {
		setDatabaseType(SupportedDatabase.valueOf("" + settingsMap.get("type")));
		setJdbcUrl("" + settingsMap.get("jdbc_url"));
		setUsername("" + settingsMap.get("username"));
		setPassword("" + settingsMap.get("password"));
	}

	public SupportedDatabase getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(SupportedDatabase databaseType) {
		this.databaseType = databaseType;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		String string = "    database:\n" +
			"        type: " + databaseType.name() + " # Possibilities:";
		for (SupportedDatabase supportedDatabase : SupportedDatabase.values())
			string += " " + supportedDatabase.name();
		return string + "\n" +
			"        jdbc_url: \"" + jdbcUrl + "\"\n" +
			"        username: \"" + username + "\"\n" +
			"        password: \"" + password + "\"\n";
	}

	public Map<String, String> toPersistenceProperties() {
		Map<String, String> persistenceProperties = new HashMap<String, String>();
		persistenceProperties.put(DDL_GENERATION, CREATE_ONLY);
		persistenceProperties.put(JDBC_DRIVER, databaseType.getDriverClassName());
		persistenceProperties.put(JDBC_URL, jdbcUrl);
		persistenceProperties.put(JDBC_USER, username);
		persistenceProperties.put(JDBC_PASSWORD, password);
		return persistenceProperties;
	}
}