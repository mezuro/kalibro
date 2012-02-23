package br.usp.ime.ccsl.kalibro.swing.components.project;

import static org.junit.Assert.*;

import br.usp.ime.ccsl.kalibro.core.SettingsDependentTest;
import br.usp.ime.ccsl.kalibro.core.Utils;
import br.usp.ime.ccsl.kalibro.core.calculators.ConfiguredResultsCalculator;
import br.usp.ime.ccsl.kalibro.core.calculators.ConfiguredResultsCalculatorTest;
import br.usp.ime.ccsl.kalibro.core.types.*;

import java.awt.Toolkit;

import javax.swing.JTextPane;

import org.fest.swing.core.MouseButton;
import org.fest.swing.data.TableCell;
import org.fest.swing.fixture.DialogFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.desktop.DialogTester;
import org.kalibro.desktop.old.listeners.ResultPanelListener;

public class ResultPanelTest extends SettingsDependentTest {

	public static ResultTree configuredResultTree() {
		ProjectResult projectResult = ProjectResultTest.helloWorldResult();
		Configuration configuration = ConfiguredResultsCalculatorTest.configuration();
		return new ConfiguredResultsCalculator(projectResult, configuration).resultsTree();
	}

	private boolean requestAnalysis;
	private ResultTree resultTree;
	private ResultPanel panel;

	private DialogFixture fixture;

	@Before
	public void setUp() throws Exception {
		resultTree = ResultTreeTest.sampleTree();
		panel = new ResultPanel(new ResultPanelTestListener());
		panel.resultTree(resultTree);
		fixture = new DialogFixture(new DialogTester(panel));
		fixture.show(Toolkit.getDefaultToolkit().getScreenSize());
	}

	@After
	public void tearDown() {
		fixture.cleanUp();
	}

	private void checkFirstColumn(String... values) {
		for (int i = 0; i < values.length; i++)
			fixture.table("tableResults").requireCellValue(TableCell.row(i).column(0), values[i]);
	}

	@Test
	public void testModuleSelection() {
		fixture.tree().component().clearSelection();
		fixture.label("moduleName").requireText("");
		fixture.table("tableResults").requireRowCount(0);

		fixture.tree().clickPath("root");
		fixture.label("moduleName").requireText("root");
		checkFirstColumn("classes", "cbo", "lcom4", "loc", "parameters");

		fixture.tree().clickPath("root/package");
		fixture.label("moduleName").requireText("root.package");
		checkFirstColumn("classes", "cbo", "lcom4", "loc");

		fixture.tree().component().clearSelection();
		fixture.tree().clickPath("root/package/Class1");
		fixture.label("moduleName").requireText("root.package.Class1");
		checkFirstColumn("cbo", "lcom4", "loc");

		fixture.tree().clickPath("root/package/Class2");
		fixture.label("moduleName").requireText("root.package.Class2");

		fixture.tree().collapsePath("root/package");
		fixture.label("moduleName").requireText("root.package");

		fixture.tree().clickPath("root/Class3");
		fixture.label("moduleName").requireText("root.Class3");

		fixture.tree().clickPath("root/Class3/method");
		fixture.label("moduleName").requireText("root.Class3.method");
	}

	@Test
	public void testMetricDescription() {
		JTextPane textPaneComments = (JTextPane) fixture.textBox("comments").component();

		fixture.tree().clickPath("root");
		fixture.textBox("comments").selectAll();
		assertNull(textPaneComments.getSelectedText());

		fixture.table("tableResults").click(TableCell.row(0).column(0), MouseButton.LEFT_BUTTON);
		fixture.textBox("comments").selectAll();
		assertEquals("Number of Classes", textPaneComments.getSelectedText().trim());

		fixture.table("tableResults").click(TableCell.row(1).column(1), MouseButton.LEFT_BUTTON);
		fixture.textBox("comments").selectAll();
		assertEquals("Coupling Between Objects", textPaneComments.getSelectedText().trim());

		fixture.table("tableResults").click(TableCell.row(2).column(2), MouseButton.LEFT_BUTTON);
		fixture.textBox("comments").selectAll();
		assertEquals("Lack of Cohesion of Methods", textPaneComments.getSelectedText().trim());

		fixture.table("tableResults").click(TableCell.row(3).column(3), MouseButton.LEFT_BUTTON);
		fixture.textBox("comments").selectAll();
		assertEquals("Lines of Code", textPaneComments.getSelectedText().trim());

		fixture.table("tableResults").component().clearSelection();
		fixture.textBox("comments").selectAll();
		assertNull(textPaneComments.getSelectedText());
	}

	@Test
	public void testProjectResultLabels() {
		String loadDate = Utils.formatDateTime(resultTree.resultInformation().loadDate());
		String loadTime = Utils.formatTime(resultTree.resultInformation().loadTime());
		String analysisTime = Utils.formatTime(resultTree.resultInformation().analysisTime());
		fixture.label("loadDate").requireText(loadDate);
		fixture.label("loadTime").requireText(loadTime);
		fixture.label("analysisTime").requireText(analysisTime);
	}

	@Test
	public void testConfiguredResults() {
		fixture.button("chooseConfiguration").click();
		fixture.label("configuration").requireText(ConfigurationTest.configuration().name());
		fixture.tree().selectPath("HelloWorld-1R1/HelloWorld");
		fixture.label("weightedMean").requireText("Weighted mean: 10");
		TableCell cell = fixture.table("tableResults").cell("amloc");
		fixture.table("tableResults").click(cell, MouseButton.LEFT_BUTTON);
		fixture.textBox("comments").selectAll();
		assertEquals("Keep that way", fixture.textBox("comments").component().getSelectedText().trim());
	}

	@Test
	public void testButtons() {
		requestAnalysis = false;
		fixture.button("requestAnalysis").click();
		assertTrue(requestAnalysis);
	}

	private class ResultPanelTestListener implements ResultPanelListener {

		@Override
		public void chooseConfiguration() {
			try {
				panel.resultTree(configuredResultTree(), ConfiguredResultsCalculatorTest.configuration());
			} catch (Exception exception) {
				// never happens
			}
		}

		@Override
		public void exportResults(ModuleResult moduleResult) {
			// TODO
		}

		@Override
		public void requestAnalysis() {
			requestAnalysis = true;
		}
	}
}