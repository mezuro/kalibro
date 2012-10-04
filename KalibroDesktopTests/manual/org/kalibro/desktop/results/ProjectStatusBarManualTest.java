package org.kalibro.desktop.results;

import static org.kalibro.ProjectFixtures.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.kalibro.ProcessState;
import org.kalibro.desktop.ComponentWrapperDialog;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.ChoiceField;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public final class ProjectStatusBarManualTest extends JPanel implements ActionListener {

	public static void main(String[] args) {
		new ComponentWrapperDialog("ProjectStatusBar", new ProjectStatusBarManualTest()).setVisible(true);
		System.exit(0);
	}

	private ChoiceField<ProcessState> stateField;
	private ProjectStatusBar statusBar;
	private Button changeButton;

	private ProjectStatusBarManualTest() {
		stateField = new ChoiceField<ProcessState>("", ProcessState.values());
		statusBar = new ProjectStatusBar(helloWorld());
		changeButton = new Button("", "Change", this);
		buildPanel();
		setMinimumSize(getPreferredSize());
		setSize(getPreferredSize());
	}

	private void buildPanel() {
		setLayout(new BorderLayout());
		add(centerPanel(), BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);
	}

	private JPanel centerPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder();
		builder.addSimpleLine(new Label("Change state:"), stateField, changeButton);
		return builder.getPanel();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		statusBar.setProjectState(stateField.get());
	}
}