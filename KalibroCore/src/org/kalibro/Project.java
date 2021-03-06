package org.kalibro;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.SortingFields;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProjectDao;

/**
 * Software project.
 * 
 * @author Carlos Morais
 */
@SortingFields("name")
public class Project extends AbstractEntity<Project> {

	public static SortedSet<Project> all() {
		return dao().all();
	}

	private static ProjectDao dao() {
		return DaoFactory.getProjectDao();
	}

	private Long id;

	@IdentityField
	private String name;

	private String description;
	private Set<Repository> repositories;

	public Project() {
		this("New project");
	}

	public Project(String name) {
		setName(name);
		setDescription("");
		setRepositories(new TreeSet<Repository>());
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

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SortedSet<Repository> getRepositories() {
		TreeSet<Repository> myRepositories = new TreeSet<Repository>();
		for (Repository each : repositories) {
			each.setProject(this);
			myRepositories.add(each);
		}
		return myRepositories;
	}

	public void setRepositories(SortedSet<Repository> repositories) {
		this.repositories = repositories;
	}

	public void addRepository(Repository repository) {
		for (Repository each : repositories)
			each.assertNoConflictWith(repository);
		repository.setProject(this);
		repositories.add(repository);
	}

	public void removeRepository(Repository repository) {
		repositories = getRepositories();
		repositories.remove(repository);
		repository.setProject(null);
	}

	void assertSaved() {
		if (!hasId())
			save();
	}

	public void save() {
		throwExceptionIf(name.trim().isEmpty(), "Project requires name.");
		id = dao().save(this);
		for (Repository repository : repositories)
			repository.save();
	}

	public void delete() {
		if (hasId())
			dao().delete(id);
		deleted();
	}

	private void deleted() {
		id = null;
		for (Repository repository : repositories)
			repository.deleted();
	}

	@Override
	public String toString() {
		return name;
	}
}