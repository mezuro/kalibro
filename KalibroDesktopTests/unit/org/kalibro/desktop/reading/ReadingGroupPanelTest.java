package org.kalibro.desktop.reading;

import static org.junit.Assert.*;

import javax.swing.JTable;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Reading;
import org.kalibro.ReadingGroup;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.field.TextField;
import org.kalibro.desktop.swingextension.panel.ConfirmPanel;
import org.kalibro.desktop.swingextension.panel.TablePanel;
import org.kalibro.desktop.tests.ComponentFinder;
import org.kalibro.tests.UnitTest;

public class ReadingGroupPanelTest extends UnitTest {

	private ReadingGroup group;

	private ReadingGroupPanel panel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		group = loadFixture("scholar", ReadingGroup.class);
		panel = new ReadingGroupPanel();
		finder = new ComponentFinder(panel);
	}

	@Test
	public void shouldHaveName() {
		assertEquals("readingGroup", panel.getName());
	}

	@Test
	public void shouldGet() {
		nameField().set(group.getName());
		descriptionField().set(group.getDescription());
		readingTablePanel().set(group.getReadings());
		assertDeepEquals(group, panel.get());
	}

	@Test
	public void shouldSet() {
		panel.set(group);
		assertEquals(group.getName(), nameField().get());
		assertEquals(group.getDescription(), descriptionField().get());
		assertDeepEquals(group.getReadings().toArray(), readingTablePanel().get().toArray());
	}

	@Test
	public void editReadingPanelShouldBeHiddenByDefault() {
		assertFalse(editReadingPanel().isVisible());
	}

	@Test
	public void shouldShowEditReadingPanelOnAddAndEdit() {
		button("add").doClick();
		assertTrue(editReadingPanel().isVisible());

		button("cancel").doClick();
		assertFalse(editReadingPanel().isVisible());

		editFirstReading();
		assertTrue(editReadingPanel().isVisible());
	}

	@Test
	public void shouldGetReadingFromEditPanel() {
		editFirstReading();
		assertSame(group.getReadings().first(), panel.getReading());
	}

	private StringField nameField() {
		return finder.find("name", StringField.class);
	}

	private TextField descriptionField() {
		return finder.find("description", TextField.class);
	}

	private TablePanel<Reading> readingTablePanel() {
		return finder.find("readings", TablePanel.class);
	}

	private void editFirstReading() {
		panel.set(group);
		finder.find("readings", JTable.class).getSelectionModel().setSelectionInterval(0, 0);
		button("edit").doClick();
	}

	private Button button(String name) {
		return finder.find(name, Button.class);
	}

	private ConfirmPanel<Reading> editReadingPanel() {
		return finder.find("reading", ConfirmPanel.class);
	}
}