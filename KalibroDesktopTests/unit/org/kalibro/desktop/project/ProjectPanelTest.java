package org.kalibro.desktop.project;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.ProjectFixtures.helloWorld;

import java.util.Arrays;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.Kalibro;
import org.kalibro.core.dao.ConfigurationDao;
import org.kalibro.core.dao.DaoFactory;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.field.ChoiceField;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.field.TextField;
import org.kalibro.service.entities.RawProjectXml;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareForTest({DaoFactory.class, Kalibro.class})
public class ProjectPanelTest extends TestCase {

	private Project project;

	private ProjectPanel panel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		project = helloWorld();
		mockKalibro();
		mockDaoFactory();
		panel = new ProjectPanel();
		finder = new ComponentFinder(panel);
	}

	private void mockKalibro() {
		TreeSet<RepositoryType> types = new TreeSet<RepositoryType>(Arrays.asList(RepositoryType.values()));
		mockStatic(Kalibro.class);
		when(Kalibro.getSupportedRepositoryTypes()).thenReturn(types);
	}

	private void mockDaoFactory() {
		ConfigurationDao configurationDao = mock(ConfigurationDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getConfigurationDao()).thenReturn(configurationDao);
		when(configurationDao.getConfigurationNames()).thenReturn(Arrays.asList(project.getConfigurationName()));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGet() {
		stringField("name").set(project.getName());
		stringField("license").set(project.getLicense());
		descriptionField().set(project.getDescription());
		configurationField().set(project.getConfigurationName());
		repositoryPanel().set(project.getRepository());
		assertDeepEquals(new RawProjectXml(project).convert(), panel.get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSet() {
		panel.set(project);
		assertEquals(project.getName(), stringField("name").get());
		assertEquals(project.getLicense(), stringField("license").get());
		assertEquals(project.getDescription(), descriptionField().get());
		assertEquals(project.getConfigurationName(), configurationField().get());
		assertDeepEquals(project.getRepository(), repositoryPanel().get());
	}

	private StringField stringField(String name) {
		return finder.find(name, StringField.class);
	}

	private TextField descriptionField() {
		return finder.find("description", TextField.class);
	}

	private ChoiceField<String> configurationField() {
		return finder.find("configuration", ChoiceField.class);
	}

	private RepositoryPanel repositoryPanel() {
		return finder.find("repository", RepositoryPanel.class);
	}
}