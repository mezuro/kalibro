package org.kalibro.core.settings;

import java.sql.Driver;

public enum SupportedDatabase {

	APACHE_DERBY("Apache Derby", org.apache.derby.jdbc.EmbeddedDriver.class),
	MYSQL("Mysql", com.mysql.jdbc.Driver.class);

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