package org.kalibro;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

import java.util.HashMap;
import java.util.Map;

import org.kalibro.core.Environment;
import org.kalibro.core.model.abstracts.AbstractEntity;
import org.kalibro.core.model.abstracts.Print;

/**
 * Database preferences.
 * 
 * @author Carlos Morais
 */
public class DatabaseSettings extends AbstractEntity<DatabaseSettings> {

	@Print(order = 1, comment = "Possibilities: APACHE_DERBY, MYSQL")
	private SupportedDatabase databaseType;

	@Print(order = 2)
	private String jdbcUrl;

	@Print(order = 3)
	private String username;

	@Print(order = 4)
	private String password;

	public DatabaseSettings() {
		setDatabaseType(SupportedDatabase.MYSQL);
		setJdbcUrl("jdbc:mysql://localhost:3306/kalibro");
		setUsername("kalibro");
		setPassword("kalibro");
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

	public Map<String, String> toPersistenceProperties() {
		Map<String, String> persistenceProperties = new HashMap<String, String>();
		persistenceProperties.put(DDL_GENERATION, Environment.ddlGeneration());
		persistenceProperties.put(JDBC_DRIVER, databaseType.getDriverClassName());
		persistenceProperties.put(JDBC_URL, jdbcUrl);
		persistenceProperties.put(JDBC_USER, username);
		persistenceProperties.put(JDBC_PASSWORD, password);
		return persistenceProperties;
	}
}