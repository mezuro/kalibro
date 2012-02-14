package org.kalibro.desktop.old.project;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.kalibro.core.model.Project;
import org.kalibro.core.model.enums.Language;
import org.kalibro.desktop.old.listeners.ChangeListener;
import org.kalibro.desktop.old.listeners.ProjectPanelListener;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class ProjectPanel extends JPanel implements ChangeListener {

	private JComboBox fieldLanguage;
	private StringField fieldName, fieldLicense;

	private RepositoryPanel repositoryPanel;

	private ProjectPanelListener listener;

	public ProjectPanel(ProjectPanelListener listener) {
		super();
		setName("project");
		this.listener = listener;

		createFields();
		setLayout(new BorderLayout());
		add(projectPanel(), BorderLayout.CENTER);
		add(southPanel(), BorderLayout.SOUTH);
		setMinimumSize(new Dimension(640, 420));
	}

	public Project project() {
		String name = fieldName.getText();
		Language language = (Language) fieldLanguage.getSelectedItem();
		Project newProject = new Project();
		newProject.setName(name);
		newProject.setLicense(fieldLicense.getText());
		newProject.setRepository(repositoryPanel.repository());
		return newProject;
	}

	public void project(Project project) {
		if (project == null)
			clearFields();
		else
			fillFields(project);
	}

	private void clearFields() {
		fieldName.setText("");
		fieldLanguage.setSelectedIndex(0);
		fieldLicense.setText("");
		repositoryPanel.repository(null);

		fieldName.setEnabled(true);
	}

	private void fillFields(Project project) {
		fieldName.setText(project.getName());
		fieldLicense.setText(project.getLicense());
		repositoryPanel.repository(project.getRepository());

		fieldName.setEnabled(false);
	}

	private void createFields() {
		fieldName = new StringField("name", 50);
		fieldLicense = new StringField("license", 50);
		repositoryPanel = new RepositoryPanel(listener);
	}

	private JPanel projectPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder();
		builder.add(new Label("Name:"));
		builder.add(fieldName, 3);
		builder.newLine();
		builder.add(new Label("License:"));
		builder.add(fieldLicense);
		builder.add(new Label("Language:"));
		builder.add(fieldLanguage);
		builder.newLine();
		builder.add(repositoryPanel, 4);
		return builder.getPanel();
	}

	private JPanel southPanel() {
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southPanel.add(buttonSave());
		return southPanel;
	}

	private JButton buttonSave() {
		JButton buttonSave = new Button("save", "Save", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				listener.projectSave();
			}
		});
		return buttonSave;
	}

	@Override
	public void changed() {
		listener.projectChanged();
	}
}