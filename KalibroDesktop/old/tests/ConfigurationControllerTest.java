package br.usp.ime.ccsl.kalibro.swing.controllers;

import static org.junit.Assert.*;

import br.usp.ime.ccsl.kalibro.core.exceptions.ConflictingMetricException;
import br.usp.ime.ccsl.kalibro.core.exceptions.ConflictingRangeException;
import br.usp.ime.ccsl.kalibro.core.types.Configuration;
import br.usp.ime.ccsl.kalibro.core.types.ConfigurationTest;
import br.usp.ime.ccsl.kalibro.core.types.ConfiguredMetric;
import br.usp.ime.ccsl.kalibro.core.types.Range;
import br.usp.ime.ccsl.kalibro.persistence.ConfigurationDao;
import br.usp.ime.ccsl.kalibro.persistence.Persistence;
import br.usp.ime.ccsl.kalibro.persistence.PersistenceDependentTest;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JTextField;

import org.fest.swing.core.KeyPressInfo;
import org.fest.swing.core.MouseButton;
import org.fest.swing.core.MouseClickInfo;
import org.fest.swing.data.TableCell;
import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.JOptionPaneFixture;
import org.fest.swing.util.Platform;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.desktop.DialogTester;

public class ConfigurationControllerTest extends PersistenceDependentTest {

	public static void main(String[] args) throws Exception {
		createDesktopDialog(ConfigurationTest.configuration()).setVisible(true);
		System.exit(0);
	}

	private static JDialog createDesktopDialog(Configuration configuration) throws Exception {
		JDesktopPane desktopPane = new JDesktopPane();
		new ConfigurationController(desktopPane, configuration);

		DialogTester dialog = new DialogTester(desktopPane);
		dialog.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		return dialog;
	}

	private Configuration configuration;

	private DialogFixture fixture;

	@Before
	public void setUp() throws Exception {
		configuration = ConfigurationTest.configuration();
		JDialog dialog = createDesktopDialog(configuration);
		fixture = new DialogFixture(dialog);
		fixture.show(Toolkit.getDefaultToolkit().getScreenSize());
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		fixture.component().removeAll();
		fixture.cleanUp();
	}

	private void addFirstNative() {
		fixture.panel("configuration").button("add").click();
		JOptionPaneFixture chooseTypeDialog = fixture.optionPane();
		chooseTypeDialog.robot.finder().findByType(JComboBox.class).setSelectedItem("Native");
		chooseTypeDialog.okButton().click();
		fixture.optionPane().okButton().click(); // choose first native listed
	}

	private void editFirstMetric() {
		TableCell cell = TableCell.row(0).column(0);
		MouseClickInfo mouseClickInfo = MouseClickInfo.button(MouseButton.LEFT_BUTTON).times(2);
		fixture.panel("metrics").table("table").click(cell, mouseClickInfo);
	}

	private void removeFirstMetric() {
		TableCell cell = TableCell.row(0).column(0);
		fixture.panel("metrics").table("table").click(cell, MouseButton.LEFT_BUTTON);
		fixture.panel("metrics").table("table").click(cell, MouseButton.LEFT_BUTTON);
		fixture.panel("configuration").button("remove").click();
	}

	@Test
	public void testRemoveAddNativeMetric() {
		ConfiguredMetric first = configuration.metrics().get(0);
		ConfiguredMetric second = configuration.metrics().get(1);
		ConfiguredMetric third = configuration.metrics().get(2);
		TableCell cell = TableCell.row(0).column(0);
		fixture.panel("metrics").table("table").cell(cell).requireValue(first.getMetric().getName());

		removeFirstMetric();
		fixture.panel("metrics").table("table").cell(cell).requireValue(second.getMetric().getName());

		removeFirstMetric();
		fixture.panel("metrics").table("table").cell(cell).requireValue(third.getMetric().getName());

		addFirstNative();
		fixture.panel("metrics").table("table").cell(cell).requireValue(first.getMetric().getName());

		addFirstNative();
		fixture.panel("metrics").table("table").cell(TableCell.row(1).column(0))
			.requireValue(second.getMetric().getName());
	}

	@Test
	public void testAddConflictingNativeMetric() {
		ConfiguredMetric first = configuration.metrics().get(0);
		removeFirstMetric();

		fixture.panel("configuration").click();
		fixture.panel("configuration").button("add").click();
		JOptionPaneFixture chooseTypeDialog = fixture.optionPane();
		chooseTypeDialog.robot.finder().findByType(JComboBox.class).setSelectedItem("Compound");
		chooseTypeDialog.okButton().click();
		fixture.panel("metric").textBox("name").setText(first.getMetric().getName());
		fixture.panel("metric").comboBox("scope").selectItem("" + first.getMetric().getScope());
		fixture.panel("metric").button("ok").click();

		addFirstNative();
		JOptionPaneFixture errorDialog = fixture.optionPane();
		errorDialog.requireErrorMessage();
		errorDialog.requireMessage(new ConflictingMetricException(first).getMessage());
		errorDialog.okButton().click();
	}

	@Test
	public void testAddConflictingCompoundMetric() {
		ConfiguredMetric first = configuration.metrics().get(0);

		fixture.panel("configuration").click();
		fixture.panel("configuration").button("add").click();
		fixture.panel("metric").textBox("name").setText(first.getMetric().getName());
		fixture.panel("metric").comboBox("scope").selectItem("" + first.getMetric().getScope());
		fixture.panel("metric").button("ok").click();

		JOptionPaneFixture errorDialog = fixture.optionPane();
		errorDialog.requireErrorMessage();
		errorDialog.requireMessage(new ConflictingMetricException(first).getMessage());
		errorDialog.okButton().click();
		fixture.panel("metric").button("cancel").click();
	}

	@Test
	public void testAddRemovePreserveDescription() {
		String description = "my new description";
		fixture.panel("configuration").textBox("description").setText(description);
		removeFirstMetric();
		addFirstNative();
		fixture.panel("configuration").textBox("description").requireText(description);
	}

	@Test
	public void testEditMetric() {
		editFirstMetric();
		String newDescription = "another description";
		fixture.panel("metric").textBox("description").setText(newDescription);
		fixture.panel("metric").button("ok").click();
		TableCell cell = TableCell.row(0).column(1);
		fixture.panel("metrics").table("table").requireCellValue(cell, newDescription);
	}

	@Test
	public void testAddEditRemoveRange() {
		editFirstMetric();

		fixture.panel("ranges").button("add").click();
		fixture.panel("range").textBox("beginning").setText("0");
		fixture.panel("range").button("+infinity").click();
		fixture.panel("range").button("ok").click();
		TableCell cell = TableCell.row(0).column(0);
		fixture.panel("ranges").table("table").requireCellValue(cell, "0.00");

		fixture.panel("ranges").table("table").click(cell, MouseButton.LEFT_BUTTON);
		fixture.panel("ranges").button("edit").click();
		fixture.panel("range").button("-infinity").click();
		fixture.panel("range").button("cancel").click();
		fixture.panel("ranges").table("table").requireCellValue(cell, "0.00");

		fixture.panel("ranges").table("table").click(cell, MouseButton.LEFT_BUTTON);
		fixture.panel("ranges").button("edit").click();
		fixture.panel("range").textBox("name").setText("Positive");
		fixture.panel("range").button("ok").click();
		fixture.panel("ranges").table("table").requireCellValue(TableCell.row(0).column(2), "Positive");

		fixture.panel("ranges").table("table").click(cell, MouseButton.LEFT_BUTTON);
		fixture.panel("ranges").button("remove").click();
		fixture.panel("ranges").table("table").requireRowCount(0);
	}

	@Test
	public void testConflictingRange() throws Exception {
		editFirstMetric();

		fixture.panel("ranges").button("add").click();
		fixture.panel("range").textBox("beginning").setText("0");
		fixture.panel("range").textBox("end").setText("10");
		fixture.panel("range").button("ok").click();

		fixture.panel("ranges").button("add").click();
		fixture.panel("range").textBox("beginning").setText("5");
		fixture.panel("range").textBox("end").setText("15");
		fixture.panel("range").button("ok").click();

		JOptionPaneFixture errorDialog = fixture.optionPane();
		errorDialog.requireErrorMessage();
		Range range = new Range(0.0, 10.0);
		Range newRange = new Range(5.0, 15.0);
		errorDialog.requireMessage(new ConflictingRangeException(range, newRange).getMessage());
		errorDialog.okButton().click();
	}

	@Test
	public void testSave() throws Exception {
		ConfigurationDao configurationDao = Persistence.configurationDao();
		assertNull(configurationDao.getByName(configuration.name()));
		fixture.panel("configuration").button("save").click();
		assertNotNull(configurationDao.getByName(configuration.name()));
	}

	@Test
	public void testSaveAs() throws Exception {
		String newName = "Another name";
		ConfigurationDao configurationDao = Persistence.configurationDao();
		assertNull(configurationDao.getByName(newName));

		fixture.panel("configuration").click();
		fixture.panel("configuration").button("saveAs").click();
		JOptionPaneFixture enterNameDialog = fixture.optionPane();
		enterNameDialog.robot.finder().findByType(JTextField.class).setText(newName);
		enterNameDialog.cancelButton().click();
		assertNull(configurationDao.getByName(newName));

		fixture.panel("configuration").button("saveAs").click();
		enterNameDialog = fixture.optionPane();
		enterNameDialog.robot.finder().findByType(JTextField.class).setText(newName);
		enterNameDialog.okButton().click();
		assertNotNull(configurationDao.getByName(newName));

		fixture.panel("configuration").label("name").requireText(newName);
	}

	@Test
	public void testSaveAndClose() throws Exception {
		fixture.panel("configuration").button("save").click();

		KeyPressInfo info = KeyPressInfo.keyCode(KeyEvent.VK_F4).modifiers(Platform.controlOrCommandMask());
		fixture.panel("configuration").pressAndReleaseKey(info);
		try {
			fixture.robot.finder().findByName("configurationFrame");
			fail("Frame should be closed");
		} catch (ComponentLookupException exception) {
			assertNotNull(Persistence.configurationDao().getByName(configuration.name()));
		}
	}

	@Test
	public void testCloseWithoutSaving() throws Exception {
		fixture.panel("configuration").textBox("description").enterText("d");

		fixture.panel("configuration").click();
		KeyPressInfo info = KeyPressInfo.keyCode(KeyEvent.VK_F4).modifiers(Platform.controlOrCommandMask());
		fixture.panel("configuration").pressAndReleaseKey(info);

		JOptionPaneFixture saveChangesDialog = fixture.optionPane();
		saveChangesDialog.requireQuestionMessage();
		saveChangesDialog.noButton().click();
		try {
			fixture.robot.finder().findByName("configurationFrame");
			fail("Frame should be closed");
		} catch (ComponentLookupException exception) {
			assertNull(Persistence.configurationDao().getByName(configuration.name()));
		}
	}

	@Test
	public void testCloseSaving() throws Exception {
		fixture.panel("configuration").textBox("description").enterText("d");

		fixture.panel("configuration").click();
		KeyPressInfo info = KeyPressInfo.keyCode(KeyEvent.VK_F4).modifiers(Platform.controlOrCommandMask());
		fixture.panel("configuration").pressAndReleaseKey(info);

		JOptionPaneFixture saveChangesDialog = fixture.optionPane();
		saveChangesDialog.requireQuestionMessage();
		saveChangesDialog.yesButton().click();
		try {
			fixture.robot.finder().findByName("configurationFrame");
			fail("Frame should be closed");
		} catch (ComponentLookupException exception) {
			assertNotNull(Persistence.configurationDao().getByName(configuration.name()));
		}
	}
}