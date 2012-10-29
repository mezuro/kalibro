package org.kalibro;

import java.sql.Driver;

/**
 * Database types supported by Kalibro.
 * 
 * @author Carlos Morais
 */
public enum SupportedDatabase {

	APACHE_DERBY("Apache Derby", org.apache.derby.jdbc.EmbeddedDriver.class),
	MYSQL("Mysql", com.mysql.jdbc.Driver.class),
	POSTGRESQL("PostgreSQL", org.postgresql.Driver.class),
	SQLITE("SQLite", org.sqlite.JDBC.class);

	private String name;
	private String driverClassName;

	private SupportedDatabase(String name, Class<? extends Driver> driverClass) {
		this.name = name;
		this.driverClassName = driverClass.getName();
	}

	@Override
	public String toString() {
		return name;
	}

	public String getDriverClassName() {
		return driverClassName;
	}
}