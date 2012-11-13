package org.kalibro.desktop.reading;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.fest.swing.core.matcher.JButtonMatcher;
import org.fest.swing.core.matcher.JTextComponentMatcher;
import org.junit.Test;
import org.kalibro.ReadingGroup;
import org.kalibro.desktop.swingextension.field.DoubleField;
import org.kalibro.desktop.tests.DesktopAcceptanceTest;

/**
 * Initially, there is no reading groups. The user should be able to create a new reading group from scratch.
 * 
 * @author Carlos Morais
 */
public class FirstReadingGroup extends DesktopAcceptanceTest {

	@Test
	public void firstReadingGroup() {
		newShouldBeTheOnlyOption();

		fixture.menuItem("new").click();
		fixture.tabbedPane().requireTabTitles("New group - Reading group");

		fixture.textBox("name").setText("Black and White");
		fixture.textBox("description").setText("High constrast readings.");
		add("White", -1.0, Color.WHITE);
		add("Black", 1.0, Color.BLACK);
		fixture.table("readings").requireContents(new String[][]{{format(-1.0), "White"}, {format(1.0), "Black"}});

		clickOnReadingGroupItem("save");
		clickOnReadingGroupItem("close");
		fixture.tabbedPane().requireTabTitles();

		assertEquals(set(new ReadingGroup("Black and White")), ReadingGroup.all());
	}

	private void newShouldBeTheOnlyOption() {
		clickOnReadingGroupItem("open");
		verifyMessageForNoReadingGroup("Open");

		clickOnReadingGroupItem("delete");
		verifyMessageForNoReadingGroup("Delete");

		fixture.menuItem("readingGroup").click();
		fixture.menuItem("save").requireDisabled();
		fixture.menuItem("close").requireDisabled();
	}

	private void clickOnReadingGroupItem(String name) {
		fixture.menuItem("readingGroup").click();
		fixture.menuItem(name).click();
	}

	private void verifyMessageForNoReadingGroup(String title) {
		fixture.robot.waitForIdle();
		fixture.optionPane().requirePlainMessage();
		fixture.optionPane().requireTitle(title);
		fixture.optionPane().requireMessage("No reading group found.");
		fixture.optionPane().button().click();
	}

	private void add(String label, Double grade, Color color) {
		fixture.button("add").click();
		fixture.textBox("label").setText(label);
		fixture.textBox("grade").setText(format(grade));
		chooseColor(color);
		fixture.button("ok").click();
	}

	private void chooseColor(Color color) {
		String rgb = Integer.toHexString(color.getRGB()).substring(2);
		fixture.button("color").click();
		fixture.dialog().tabbedPane().selectTab("RGB");
		fixture.textBox(JTextComponentMatcher.withText("FFFFFF").andShowing()).enterText(rgb);
		fixture.dialog().button(JButtonMatcher.withText("OK")).click();
	}

	private String format(Double value) {
		return new DoubleField("").getDecimalFormat().format(value);
	}
}