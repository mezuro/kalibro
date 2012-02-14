package org.kalibro.desktop.old.project;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.kalibro.core.model.Repository;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.desktop.old.listeners.ChangeListener;
import org.kalibro.desktop.old.listeners.ProjectPanelListener;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.ChoiceField;
import org.kalibro.desktop.swingextension.field.PasswordField;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class RepositoryPanel extends JPanel implements ChangeListener {

	private ChoiceField<RepositoryType> fieldType;
	private StringField fieldAddress, fieldUsername;
	private PasswordField fieldPassword;

	private JButton buttonFilePath;

	private ProjectPanelListener listener;

	public RepositoryPanel(ProjectPanelListener listener) {
		super();
		setName("repository");
		setBorder(new TitledBorder("Repository"));
		this.listener = listener;

		createFields();
		createButtonFilePath();
		buildPanel();
		setMinimumSize(new Dimension(600, 170));
	}

	public Repository repository() {
		RepositoryType type = fieldType.getValue();
		Repository newRepository = new Repository(type, fieldAddress.getText());
		newRepository.setUsername(fieldUsername.getText());
		newRepository.setPassword(fieldPassword.getValue());
		return newRepository;
	}

	public void repository(Repository repository) {
		if (repository == null)
			clearFields();
		else
			fillFields(repository);
	}

	private void clearFields() {
		fieldType.setSelectedItem(RepositoryType.LOCAL_DIRECTORY);
		fieldAddress.setText("");
		fieldUsername.setText("");
		fieldPassword.setText("");
	}

	private void fillFields(Repository repository) {
		fieldType.setSelectedItem(repository.getType());
		fieldAddress.setText(repository.getAddress());
		fieldUsername.setText(repository.getUsername());
		fieldPassword.setText(repository.getPassword());
	}

	private void createFields() {
		fieldType = new ChoiceField<RepositoryType>("type", RepositoryType.values());
		fieldAddress = new StringField("address", 200);
		fieldUsername = new StringField("username", 30);
		fieldPassword = new PasswordField("password", 30);
	}

	private void buildPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.add(new Label("Type:"));
		builder.add(fieldType);
		builder.newLine();
		builder.add(new Label("Address:"));
		builder.add(fieldAddress, 2);
		builder.add(buttonFilePath);
		builder.newLine();
		builder.add(new Label("Username:"));
		builder.add(fieldUsername);
		builder.add(new Label("Password:"));
		builder.add(fieldPassword);
	}

	@Override
	public void changed() {
		listener.projectChanged();
	}

	private void createButtonFilePath() {
		buttonFilePath = new Button("filepath", "File path", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				fieldAddress.setText(listener.fillFilePath());
			}
		});
		buttonFilePath.setEnabled(true);
	}
}