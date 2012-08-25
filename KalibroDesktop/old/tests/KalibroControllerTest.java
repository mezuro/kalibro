package br.usp.ime.ccsl.kalibro.swing.controllers;

import static org.junit.Assert.*;

import br.usp.ime.ccsl.kalibro.Kalibro;
import br.usp.ime.ccsl.kalibro.ProjectPool;
import br.usp.ime.ccsl.kalibro.ProjectStateChangeSupportTest;
import br.usp.ime.ccsl.kalibro.core.NullCollector;
import br.usp.ime.ccsl.kalibro.core.SettingsDependentTest;
import br.usp.ime.ccsl.kalibro.core.loaders.SettingsLoader;
import br.usp.ime.ccsl.kalibro.core.types.Configuration;
import br.usp.ime.ccsl.kalibro.core.types.ConfigurationTest;
import br.usp.ime.ccsl.kalibro.core.types.Project;
import br.usp.ime.ccsl.kalibro.core.types.ProjectTest;
import br.usp.ime.ccsl.kalibro.persistence.ConfigurationDao;
import br.usp.ime.ccsl.kalibro.persistence.Persistence;
import br.usp.ime.ccsl.kalibro.persistence.PersistenceDependentTest;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

import org.fest.swing.core.KeyPressInfo;
import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JOptionPaneFixture;
import org.fest.swing.util.Platform;
import org.junit.Before;
import org.junit.Test;

public class KalibroControllerTest extends PersistenceDependentTest {

	private FrameFixture fixture;

	@Before
	public void setUp() throws Exception {
		KalibroController.main(null);
		ProjectPool.instance().restart();
		fixture = new FrameFixture(KalibroController.frame());
		fixture.show(Toolkit.getDefaultToolkit().getScreenSize());
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		fixture.component().removeAll();
		fixture.cleanUp();
		SettingsDependentTest.restoreTestSettings();
		ProjectStateChangeSupportTest.clearListeners();
	}

	@Test
	public void testNewConfiguration() throws Exception {
		String name = "My new configuration";
		fixture.menuItem("configuration").click();
		fixture.menuItem("newConfiguration").click();
		JOptionPaneFixture configurationNameDialog = fixture.optionPane();
		configurationNameDialog.robot.finder().findByType(JTextField.class).setText("");
		configurationNameDialog.okButton().click();
		try {
			fixture.panel("configuration");
			fail("Should not open configuration frame for empty name");
		} catch (ComponentLookupException exception) {
			// ok
		}

		fixture.menuItem("configuration").click();
		fixture.menuItem("newConfiguration").click();
		configurationNameDialog = fixture.optionPane();
		configurationNameDialog.robot.finder().findByType(JTextField.class).setText(name);
		configurationNameDialog.okButton().click();
		fixture.panel("configuration").label("name").requireText(name);

		fixture.panel("configuration").click();
		KeyPressInfo info = KeyPressInfo.keyCode(KeyEvent.VK_F4).modifiers(Platform.controlOrCommandMask());
		fixture.panel("configuration").pressAndReleaseKey(info);

		SettingsLoader.currentSettings().metricCollector(NullCollector.class);
		fixture.menuItem("configuration").click();
		fixture.menuItem("newConfiguration").click();
		configurationNameDialog = fixture.optionPane();
		configurationNameDialog.robot.finder().findByType(JTextField.class).setText(name);
		configurationNameDialog.okButton().click();
		JOptionPaneFixture errorDialog = fixture.optionPane();
		errorDialog.requireErrorMessage();
		errorDialog.requireMessage("Metric collector not set");
		errorDialog.okButton().click();
	}

	@Test
	public void testOpenConfiguration() throws Exception {
		Configuration configuration = ConfigurationTest.configuration();
		Persistence.configurationDao().save(configuration);

		fixture.menuItem("configuration").click();
		fixture.menuItem("openConfiguration").click();
		fixture.optionPane().cancelButton().click();
		try {
			fixture.panel("configuration");
			fail("Should not open configuration frame when cancel is choosen");
		} catch (ComponentLookupException exception) {
			// ok
		}

		fixture.menuItem("configuration").click();
		fixture.menuItem("openConfiguration").click();
		fixture.optionPane().okButton().click(); // choose first configuration
		fixture.panel("configuration").label("name").requireText(configuration.name());
	}

	@Test
	public void testRemoveConfiguration() throws Exception {
		Configuration configuration = ConfigurationTest.configuration();
		ConfigurationDao configurationDao = Persistence.configurationDao();
		configurationDao.save(configuration);

		fixture.menuItem("configuration").click();
		fixture.menuItem("removeConfiguration").click();
		fixture.optionPane().okButton().click(); // choose first configuration
		assertNull(configurationDao.getByName(configuration.name()));
	}

	@Test
	public void testNoConfigurations() {
		fixture.menuItem("configuration").click();
		fixture.menuItem("openConfiguration").click();
		JOptionPaneFixture noConfigurationDialog = fixture.optionPane();
		noConfigurationDialog.requirePlainMessage();
		noConfigurationDialog.requireMessage("No configurations found");
		noConfigurationDialog.okButton().click();

		fixture.menuItem("configuration").click();
		fixture.menuItem("removeConfiguration").click();
		noConfigurationDialog = fixture.optionPane();
		noConfigurationDialog.requirePlainMessage();
		noConfigurationDialog.requireMessage("No configurations found");
		noConfigurationDialog.okButton().click();
	}

	@Test
	public void testNewProject() {
		fixture.menuItem("project").click();
		fixture.menuItem("newProject").click();
		fixture.panel("project").textBox("name").requireEnabled();
		fixture.panel("project").textBox("version").requireEnabled();
	}

	@Test
	public void testOpenProject() throws Exception {
		Project project = ProjectTest.helloWorld();
		Kalibro.instance().loadProject(project);

		fixture.menuItem("project").click();
		fixture.menuItem("openProject").click();
		fixture.optionPane().cancelButton().click();
		try {
			fixture.panel("project");
			fail("Should not open project frame when cancel is choosen");
		} catch (ComponentLookupException exception) {
			// ok
		}

		fixture.menuItem("project").click();
		fixture.menuItem("openProject").click();
		fixture.optionPane().okButton().click(); // choose first project

		fixture.tabbedPane().selectTab(0);
		fixture.panel("project").textBox("name").requireText(project.name());
	}

	@Test
	public void testRemoveProject() throws Exception {
		Project project = ProjectTest.helloWorld();
		Kalibro.instance().loadProject(project);

		fixture.menuItem("project").click();
		fixture.menuItem("removeProject").click();
		fixture.optionPane().okButton().click(); // choose first project
		assertNull(Persistence.projectDao().getByNameAndVersion(project.name(), project.version()));
	}

	@Test
	public void testNoProjects() {
		fixture.menuItem("project").click();
		fixture.menuItem("openProject").click();
		JOptionPaneFixture noProjectDialog = fixture.optionPane();
		noProjectDialog.requirePlainMessage();
		noProjectDialog.requireMessage("No projects found");
		noProjectDialog.okButton().click();

		fixture.menuItem("project").click();
		fixture.menuItem("removeProject").click();
		noProjectDialog = fixture.optionPane();
		noProjectDialog.requirePlainMessage();
		noProjectDialog.requireMessage("No projects found");
		noProjectDialog.okButton().click();
	}

	@Test
	public void testCollectorErrorNewProject() throws Exception {
		SettingsLoader.currentSettings().metricCollector(NullCollector.class);

		fixture.menuItem("project").click();
		fixture.menuItem("newProject").click();
		JOptionPaneFixture errorDialog = fixture.optionPane();
		errorDialog.requireErrorMessage();
		errorDialog.requireMessage("Metric collector not set");
		errorDialog.okButton().click();
	}

	@Test
	public void testCollectorErrorOpenProject() throws Exception {
		SettingsLoader.currentSettings().metricCollector(NullCollector.class);

		Project project = ProjectTest.helloWorld();
		Kalibro.instance().loadProject(project);
		fixture.menuItem("project").click();
		fixture.menuItem("openProject").click();
		fixture.optionPane().okButton().click(); // choose first project
		JOptionPaneFixture errorDialog = fixture.optionPane();
		errorDialog.requireErrorMessage();
		errorDialog.requireMessage("Metric collector not set");
		errorDialog.okButton().click();
	}

	@Test
	public void testEditSettingsWithoutClosing() {
		fixture.menuItem("configuration").click();
		fixture.menuItem("newConfiguration").click();
		JOptionPaneFixture optionPane = fixture.optionPane();
		optionPane.robot.finder().findByType(JTextField.class).setText("My configuration");
		optionPane.okButton().click();

		fixture.menuItem("settings").click();
		fixture.menuItem("editSettings").click();

		optionPane = fixture.optionPane();
		optionPane.requirePlainMessage();
		String message = "All projects and configurations should be closed before editing settings.";
		optionPane.requireMessage(message);
		optionPane.okButton().click();
	}

	@Test
	public void testSettings() throws Exception {
		fixture.menuItem("settings").click();
		fixture.menuItem("editSettings").click();

		DialogFixture settingsDialog = fixture.dialog("settings");
		settingsDialog.textBox("metricCollector").setText("invalid.Collector");
		settingsDialog.button("ok").click();

		JOptionPaneFixture errorDialog = fixture.optionPane();
		errorDialog.requireErrorMessage();
		errorDialog.requireMessage("invalid.Collector");
		errorDialog.okButton().click();

		settingsDialog.button("cancel").click();

		fixture.menuItem("settings").click();
		fixture.menuItem("editSettings").click();

		settingsDialog = fixture.dialog("settings");
		settingsDialog.checkBox("checkClient").check();
		settingsDialog.button("ok").click();

		errorDialog = fixture.optionPane();
		errorDialog.requireErrorMessage();
		String wsdl = Kalibro.instance().settings().serviceEndpoint() + "?wsdl";
		String cause = "Connection refused.";
		String message = "Failed to access the WSDL at: " + wsdl + ". It failed with: \n\t" + cause;
		errorDialog.requireMessage(message);
		errorDialog.okButton().click();

		settingsDialog.checkBox("checkClient").uncheck();
		settingsDialog.button("ok").click();
	}
}