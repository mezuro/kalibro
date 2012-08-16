package org.kalibro.core.model;

import java.io.File;

import org.kalibro.Kalibro;
import org.kalibro.KalibroException;
import org.kalibro.core.model.abstracts.AbstractEntity;
import org.kalibro.core.model.abstracts.IdentityField;
import org.kalibro.core.model.abstracts.SortingMethods;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.core.util.Identifier;

@SortingMethods("getName")
public class Project extends AbstractEntity<Project> {

	private Long id;

	@IdentityField
	private String name;

	private String license;
	private String description;
	private String configurationName;
	private Repository repository;

	private Throwable error;
	private ProjectState state;

	public Project() {
		setId(null);
		setName("");
		setLicense("");
		setDescription("");
		setConfigurationName("");
		setRepository(new Repository());
		setState(ProjectState.NEW);
	}

	@Override
	public String toString() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getConfigurationName() {
		return configurationName;
	}

	public void setConfigurationName(String configurationName) {
		this.configurationName = configurationName;
	}

	public void load() {
		repository.load(getDirectory());
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public ProjectState getState() {
		if (error != null)
			return ProjectState.ERROR;
		return state;
	}

	public String getStateMessage() {
		return getState().getMessage(name);
	}

	public ProjectState getStateWhenErrorOcurred() {
		assertHasError();
		return state;
	}

	public void setState(ProjectState state) {
		if (state == ProjectState.ERROR)
			throw new KalibroException("Use setError(Throwable) to put project in error state");
		error = null;
		this.state = state;
	}

	public Throwable getError() {
		assertHasError();
		return error;
	}

	private void assertHasError() {
		if (error == null)
			throw new KalibroException("Project " + name + " has no error");
	}

	public void setError(Throwable error) {
		this.error = error;
	}

	public File getDirectory() {
		File loadDirectory = Kalibro.currentSettings().getServerSettings().getLoadDirectory();
		return new File(loadDirectory, id + "-" + Identifier.fromText(name).asVariable());
	}
}