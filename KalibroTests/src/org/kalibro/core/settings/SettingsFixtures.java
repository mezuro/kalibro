package org.kalibro.core.settings;

import java.util.HashMap;
import java.util.Map;

public final class SettingsFixtures {

	public static Map<?, ?> kalibroSettingsMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("settings", "CLIENT");
		map.put("client", clientSettingsMap());
		map.put("server", serverSettingsMap());
		return map;
	}

	public static Map<?, ?> clientSettingsMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("service_address", "file:///");
		return map;
	}

	public static Map<?, ?> serverSettingsMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("load_directory", "/");
		map.put("database", databaseSettingsMap());
		return map;
	}

	public static Map<?, ?> databaseSettingsMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", "APACHE_DERBY");
		map.put("jdbc_url", "jdbc:derby:memory:kalibro;create=true");
		map.put("username", "user");
		map.put("password", "pass");
		return map;
	}

	private SettingsFixtures() {
		return;
	}
}