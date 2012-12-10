package org.kalibro.service.xml;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Project;
import org.kalibro.dao.RepositoryDao;
import org.kalibro.dto.DaoLazyLoader;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoLazyLoader.class)
public class ProjectXmlTest extends XmlTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		Project project = (Project) entity;
		mockStatic(DaoLazyLoader.class);
		when(DaoLazyLoader.createProxy(RepositoryDao.class, "repositoriesOf", project.getId()))
			.thenReturn(project.getRepositories());
	}

	@Override
	public void verifyElements() {
		assertElement("id", Long.class);
		assertElement("name", String.class, true);
		assertElement("description", String.class);
	}

	@Test
	public void repositoriesBeEmptyIfHasNoId() {
		assertTrue(new ProjectXml().repositories().isEmpty());
	}
}