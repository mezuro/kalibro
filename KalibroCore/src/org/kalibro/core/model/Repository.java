package org.kalibro.core.model;

import java.util.List;

import org.kalibro.core.model.abstracts.AbstractEntity;
import org.kalibro.core.model.enums.RepositoryType;

public class Repository extends AbstractEntity<Repository> {

	private RepositoryType type;
	private String address;
	private String username;
	private String password;

	public Repository() {
		this(RepositoryType.LOCAL_DIRECTORY, "");
	}

	public Repository(RepositoryType type, String address) {
		this(type, address, "", "");
	}

	public Repository(RepositoryType type, String address, String username, String password) {
		setType(type);
		setAddress(address);
		setUsername(username);
		setPassword(password);
	}

	public boolean hasAuthentication() {
		return ! (username.isEmpty() && password.isEmpty());
	}

	public List<String> getLoadCommands(String loadPath) {
		return type.getProjectLoader().getLoadCommands(this, loadPath);
	}

	public RepositoryType getType() {
		return type;
	}

	public void setType(RepositoryType type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
}