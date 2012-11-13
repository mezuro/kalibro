package org.kalibro.desktop.reading;

import static org.junit.Assert.assertTrue;

import org.fest.swing.data.TableCell;
import org.junit.Test;
import org.kalibro.ReadingGroup;
import org.kalibro.desktop.tests.DesktopAcceptanceTest;

/**
 * If there is already a reading group, the user should be able to edit and delete it.
 * 
 * @author Carlos Morais
 */
public class EditAndDeleteReadingGroup extends DesktopAcceptanceTest {

	private ReadingGroup group;

	@Test
	public void firstReadingGroup() {
		group = loadFixture("scholar", ReadingGroup.class);
		group.save();

		clickOnReadingGroupItem("open");
		chooseFirstGroup("Open");
		fixture.tabbedPane().requireTabTitles("Scholar - Reading group");

		editGroup();

		clickOnReadingGroupItem("save");
		fixture.tabbedPane().requireTabTitles("Scholar edited - Reading group");
		assertDeepEquals(set(group), ReadingGroup.all());

		clickOnReadingGroupItem("delete");
		chooseFirstGroup("Delete");
		assertTrue(ReadingGroup.all().isEmpty());
	}

	private void clickOnReadingGroupItem(String name) {
		fixture.menuItem("readingGroup").click();
		fixture.menuItem(name).click();
	}

	private void chooseFirstGroup(String title) {
		fixture.robot.waitForIdle();
		fixture.optionPane().requirePlainMessage();
		fixture.optionPane().requireTitle(title);
		fixture.optionPane().requireMessage("Select reading group:");
		fixture.optionPane().buttonWithText("OK").click();
	}

	private void editGroup() {
		group.setName("Scholar edited");
		group.getReadings().first().setLabel("Awful");

		fixture.textBox("name").setText("Scholar edited");
		fixture.table("readings").selectCell(TableCell.row(0).column(0));
		fixture.button("edit").click();
		fixture.textBox("label").setText("Awful");
		fixture.button("ok").click();
	}
}