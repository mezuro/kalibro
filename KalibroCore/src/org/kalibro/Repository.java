package org.kalibro;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.Ignore;
import org.kalibro.core.abstractentity.SortingFields;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.RepositoryDao;

/**
 * Source code repository.
 * 
 * @author Carlos Morais
 */
@SortingFields("name")
public class Repository extends AbstractEntity<Repository> {

	private Long id;

	@IdentityField
	private String name;

	private String description;
	private String license;
	private String address;
	private RepositoryType type;
	private Integer processPeriod;

	private Configuration configuration;

	@Ignore
	private Project project;

	public Repository() {
		this("New repository", RepositoryType.LOCAL_DIRECTORY, "");
	}

	public Repository(String name, RepositoryType type, String address) {
		setName(name);
		setDescription("");
		setLicense("");
		setAddress(address);
		setType(type);
		setProcessPeriod(0);
	}

	public Long getId() {
		return id;
	}

	public boolean hasId() {
		return id != null;
	}

	public String getName() {
		return name;
	}

	public String getCompleteName() {
		String projectName = (project == null) ? "" : project.getName() + " - ";
		return projectName + name;
	}

	public void setName(String name) {
		if (project != null)
			for (Repository other : project.getRepositories())
				if (other != this)
					assertNoNameConflict(other, name);
		this.name = name;
	}

	void assertNoConflictWith(Repository other) {
		assertNoNameConflict(other, name);
	}

	private void assertNoNameConflict(Repository other, String theName) {
		throwExceptionIf(other.name.equals(theName),
			"Repository named \"" + theName + "\" already exists in the project.");
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public RepositoryType getType() {
		return type;
	}

	public void setType(RepositoryType type) {
		this.type = type;
	}

	public Integer getProcessPeriod() {
		return processPeriod;
	}

	public void setProcessPeriod(Integer processPeriod) {
		this.processPeriod = processPeriod;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public Project getProject() {
		return project;
	}

	void setProject(Project project) {
		this.project = project;
	}

	public void save() {
		throwExceptionIf(name.trim().isEmpty(), "Repository requires name.");
		throwExceptionIf(address.trim().isEmpty(), "Repository requires address.");
		throwExceptionIf(project == null, "Repository is not in any project.");
		throwExceptionIf(configuration == null, "A configuration should be associated with the repository.");
		project.assertSaved();
		configuration.assertSaved();
		id = dao().save(this, project.getId());
	}

	public void process() {
		save();
		dao().process(id);
	}

	public void cancelProcessing() {
		if (hasId())
			dao().cancelProcessing(id);
	}

	public void delete() {
		if (hasId())
			dao().delete(id);
		if (project != null)
			project.removeRepository(this);
		deleted();
	}

	void deleted() {
		id = null;
	}

	private RepositoryDao dao() {
		return DaoFactory.getRepositoryDao();
	}

	@Override
	public String toString() {
		return getCompleteName();
	}
}