package org.kalibro.core.persistence.record;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.kalibro.Configuration;
import org.kalibro.Project;
import org.kalibro.Repository;
import org.powermock.reflect.Whitebox;

public class ProjectRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		shouldHaveId();
		assertColumn("name", String.class).isRequired().isUnique();
		assertColumn("description", String.class).isNullable();
		assertOneToMany("repositories").mappedBy("project");
	}

	@Test
	public void shouldConstructWithRepositories() {
		Repository repository = new Repository();
		repository.setConfiguration(new Configuration());

		Project project = new Project();
		project.addRepository(repository);
		ProjectRecord record = new ProjectRecord(project);
		assertEquals(1, Whitebox.getInternalState(record, Collection.class).size());
	}
}