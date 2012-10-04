package br.usp.ime.ccsl.kalibro.swing.controllers;

import static org.junit.Assert.*;

import br.usp.ime.ccsl.kalibro.Kalibro;
import br.usp.ime.ccsl.kalibro.ProjectPool;
import br.usp.ime.ccsl.kalibro.ProjectStateChangeSupportTest;
import br.usp.ime.ccsl.kalibro.ProjectStateListener;
import br.usp.ime.ccsl.kalibro.core.calculators.ConfiguredResultsCalculatorTest;
import br.usp.ime.ccsl.kalibro.core.types.Configuration;
import br.usp.ime.ccsl.kalibro.core.types.ConfiguredMetric;
import br.usp.ime.ccsl.kalibro.core.types.Project;
import br.usp.ime.ccsl.kalibro.core.types.ProjectTest;
import br.usp.ime.ccsl.kalibro.persistence.ConfigurationDao;
import br.usp.ime.ccsl.kalibro.persistence.Persistence;
import br.usp.ime.ccsl.kalibro.persistence.PersistenceDependentTest;
import br.usp.ime.ccsl.kalibro.persistence.ProjectDao;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JDialog;

import org.fest.swing.core.KeyPressInfo;
import org.fest.swing.core.MouseButton;
import org.fest.swing.data.TableCell;
import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.JOptionPaneFixture;
import org.fest.swing.timing.Timeout;
import org.fest.swing.util.Platform;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.CompoundMetric;
import org.kalibro.Granularity;
import org.kalibro.RepositoryState;
import org.kalibro.desktop.DialogTester;

public class ProjectControllerTest extends PersistenceDependentTest implements ProjectStateListener {

	public static void main(String[] args) throws Exception {
		createDialog().setVisible(true);
		System.exit(0);
	}

	private static JDialog createDialog() throws Exception {
		JDesktopPane desktopPane = new JDesktopPane();
		new ProjectController(desktopPane);
		DialogTester dialog = new DialogTester(desktopPane);
		dialog.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		return dialog;
	}

	private Project project;
	private DialogFixture fixture;

	@Before
	public void setUp() throws Exception {
		project = ProjectTest.helloWorld();
		Kalibro.instance().addProjectStateListener(project, this);
		fixture = new DialogFixture(createDialog());
		fixture.show(Toolkit.getDefaultToolkit().getScreenSize());
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		fixture.cleanUp();
		fixture.component().removeAll();
		restoreTestSettings();
		ProjectPool.instance().restart();
		ProjectStateChangeSupportTest.clearListeners();
	}

	@Override
	public void projectStateChanged(ProjectState newState) {
		if (newState == ProjectState.READY || newState == ProjectState.ERROR)
			synchronized (project) {
				project.notify();
			}
	}

	@Test
	public void testSave() throws Exception {
		String name = project.name();
		String version = project.version();
		ProjectDao projectDao = Persistence.projectDao();
		assertNull(projectDao.getByNameAndVersion(name, version));

		assertFalse(fixture.tabbedPane().component().isEnabledAt(1));

		fixture.panel("project").textBox("name").setText(name);
		fixture.panel("project").textBox("version").setText(version);
		fixture.panel("repository").textBox("address").setText(project.repository().address());

		synchronized (project) {
			fixture.panel("project").button("save").click();
			project.wait();
		}

		fixture.tabbedPane().selectTab(1);
		fixture.panel("result").tree().selectPath("HelloWorld-1R1/HelloWorld");
		fixture.panel("result").label("moduleName").requireText("HelloWorld");

		assertNotNull(projectDao.getByNameAndVersion(name, version));
		fixture.tabbedPane().selectTab(0);
		fixture.panel("project").textBox("name").requireDisabled();
		fixture.panel("project").textBox("version").requireDisabled();

		fixture.panel("project").textBox("license").setText("some license");
		fixture.panel("project").button("save").click();
		assertEquals("some license", projectDao.getByNameAndVersion(name, version).license());
	}

	@Test
	public void testSaveError() throws Exception {
		String name = project.name();
		String version = project.version();
		ProjectDao projectDao = Persistence.projectDao();
		assertNull(projectDao.getByNameAndVersion(name, version));

		fixture.panel("project").button("save").click();
		JOptionPaneFixture errorDialog = fixture.optionPane();
		errorDialog.requireErrorMessage();
		errorDialog.requireMessage("Neither project name nor version can be null");
		errorDialog.okButton().click();

		assertNull(projectDao.getByNameAndVersion(name, version));
	}

	@Test
	public void testSaveAndClose() throws Exception {
		fixture.panel("project").textBox("name").setText(project.name());
		fixture.panel("project").textBox("version").setText(project.version());
		fixture.panel("repository").textBox("address").setText(project.repository().address());
		synchronized (project) {
			fixture.panel("project").button("save").click();
			project.wait();
		}

		fixture.tabbedPane().selectTab(1);
		KeyPressInfo info = KeyPressInfo.keyCode(KeyEvent.VK_F4).modifiers(Platform.controlOrCommandMask());
		fixture.panel("result").pressAndReleaseKey(info);
		try {
			fixture.robot.finder().findByName("projectFrame");
			fail("Frame should be closed");
		} catch (ComponentLookupException exception) {
			String name = project.name();
			String version = project.version();
			assertNotNull(Persistence.projectDao().getByNameAndVersion(name, version));
		}
	}

	@Test
	public void testCloseWithoutSaving() throws Exception {
		fixture.panel("project").textBox("name").setText(project.name());
		fixture.panel("project").textBox("version").enterText(project.version());
		fixture.panel("repository").textBox("address").setText(project.repository().address());

		fixture.panel("project").click();
		KeyPressInfo info = KeyPressInfo.keyCode(KeyEvent.VK_F4).modifiers(Platform.controlOrCommandMask());
		fixture.panel("project").pressAndReleaseKey(info);

		JOptionPaneFixture saveChangesDialog = fixture.optionPane();
		saveChangesDialog.requireQuestionMessage();
		saveChangesDialog.noButton().click();
		try {
			fixture.robot.finder().findByName("projectFrame");
			fail("Frame should be closed");
		} catch (ComponentLookupException exception) {
			String name = project.name();
			String version = project.version();
			assertNull(Persistence.projectDao().getByNameAndVersion(name, version));
		}
	}

	@Test
	public void testCloseSaving() throws Exception {
		fixture.panel("project").textBox("name").setText(project.name());
		fixture.panel("project").textBox("version").enterText(project.version());
		fixture.panel("repository").textBox("address").setText(project.repository().address());

		fixture.panel("project").click();
		KeyPressInfo info = KeyPressInfo.keyCode(KeyEvent.VK_F4).modifiers(Platform.controlOrCommandMask());
		fixture.panel("project").pressAndReleaseKey(info);

		JOptionPaneFixture saveChangesDialog = fixture.optionPane();
		saveChangesDialog.requireQuestionMessage();
		synchronized (project) {
			saveChangesDialog.yesButton().click();
			project.wait();
		}
		try {
			fixture.robot.finder().findByName("projectFrame");
			fail("Frame should be closed");
		} catch (ComponentLookupException exception) {
			String name = project.name();
			String version = project.version();
			assertNotNull(Persistence.projectDao().getByNameAndVersion(name, version));
		}
	}

	@Test
	public void testOpenProject() throws Exception {
		tearDown();
		Kalibro.instance().addProjectStateListener(project, this);
		synchronized (project) {
			Kalibro.instance().loadProject(project);
			project.wait();
		}

		JDesktopPane desktopPane = new JDesktopPane();
		new ProjectController(desktopPane, project);
		fixture = new DialogFixture(new DialogTester(desktopPane));
		fixture.show(Toolkit.getDefaultToolkit().getScreenSize());

		fixture.tabbedPane().selectTab(0);
		fixture.panel("project").textBox("name").requireDisabled();
		fixture.panel("project").textBox("version").requireDisabled();
		fixture.panel("project").textBox("description").enterText("d");
		fixture.label("status").requireText(ProjectState.READY.getMessage("" + project));

		fixture.tabbedPane().selectTab(1);
		String date1 = fixture.panel("result").label("loadDate").text();
		Thread.sleep(1000);
		synchronized (project) {
			fixture.panel("result").button("requestAnalysis").click();
			project.wait();
		}
		Thread.sleep(500);
		String date2 = fixture.panel("result").label("loadDate").text();
		assertFalse(date1.equals(date2));
	}

	@Test
	public void testProjectError() {
		fixture.panel("project").textBox("name").setText(project.name());
		fixture.panel("project").textBox("version").setText(project.version());
		fixture.panel("repository").textBox("address").setText("inexistent/repository");
		fixture.panel("project").button("save").click();
		JOptionPaneFixture errorDialog = fixture.optionPane(Timeout.timeout(5000));
		errorDialog.requireErrorMessage();
		errorDialog.requireMessage("Source 'inexistent/repository' does not exist");
		errorDialog.okButton().click();
		assertEquals(ProjectState.ERROR.getMessage("" + project), fixture.label("status").text().trim());
	}

	@Test
	public void testConfiguredResults() throws Exception {
		Configuration configuration = ConfiguredResultsCalculatorTest.configuration();
		ConfigurationDao configurationDao = Persistence.configurationDao();

		fixture.panel("project").textBox("name").setText(project.name());
		fixture.panel("project").textBox("version").setText(project.version());
		fixture.panel("repository").textBox("address").setText(project.repository().address());
		synchronized (project) {
			fixture.panel("project").button("save").click();
			project.wait();
		}

		configurationDao.save(configuration);
		fixture.tabbedPane().selectTab(1);
		fixture.panel("result").button("chooseConfiguration").click();
		fixture.optionPane().okButton().click(); // choose first configuration

		fixture.panel("result").tree().selectPath("HelloWorld-1R1/HelloWorld");
		TableCell cell = fixture.table("tableResults").cell("amloc");
		fixture.table("tableResults").click(cell, MouseButton.LEFT_BUTTON);
		fixture.textBox("comments").selectAll();
		assertEquals("Keep that way", fixture.textBox("comments").component().getSelectedText().trim());

		synchronized (project) {
			fixture.panel("result").button("requestAnalysis").click();
			project.wait();
		}
		Thread.sleep(500);
		fixture.label("moduleName").requireText("HelloWorld");

		configuration.add(new ConfiguredMetric(new CompoundMetric("invalid", Granularity.CLASS, "", "x = x")));
		configurationDao.save(configuration);
		fixture.tabbedPane().selectTab(1);
		fixture.panel("result").button("chooseConfiguration").click();
		fixture.optionPane().okButton().click(); // choose first configuration

		JOptionPaneFixture errorDialog = fixture.optionPane();
		errorDialog.requireErrorMessage();
		String message = "Invalid script:  ReferenceError: \"x\" is not defined. ";
		message += "(<Unknown source>#19) in <Unknown source> at line number 19";
		errorDialog.requireMessage(message);
		errorDialog.okButton().click();
	}
}