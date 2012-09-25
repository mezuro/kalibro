package org.kalibro.desktop.project;

import java.awt.Component;
import java.util.List;

import org.kalibro.Configuration;
import org.kalibro.Project;
import org.kalibro.dao.DaoFactory;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.ChoiceField;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.field.TextField;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class ProjectPanel extends EditPanel<Project> {

	private StringField nameField;
	private StringField licenseField;
	private TextField descriptionField;
	private ChoiceField<Configuration> configurationField;
	private RepositoryPanel repositoryPanel;

	public ProjectPanel() {
		super("project");
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		List<Configuration> configurations = DaoFactory.getConfigurationDao().all();
		nameField = new StringField("name", 20);
		licenseField = new StringField("license", 20);
		descriptionField = new TextField("description", 3, 20);
		configurationField = new ChoiceField<Configuration>("configurations", configurations);
		repositoryPanel = new RepositoryPanel();
	}

	@Override
	protected void buildPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.addSimpleLine(new Label("Name:"), nameField);
		builder.addSimpleLine(new Label("License:"), licenseField);
		builder.addSimpleLine(new Label("Description:"), descriptionField);
		builder.addSimpleLine(new Label("Configuration:"), configurationField);
		builder.add(repositoryPanel, 2);
	}

	@Override
	public Project get() {
		Project project = new Project();
		project.setName(nameField.get());
		project.setLicense(licenseField.get());
		project.setDescription(descriptionField.get());
		project.setConfigurationName(configurationField.get().getName());
		project.setRepository(repositoryPanel.get());
		return project;
	}

	@Override
	public void set(Project project) {
		nameField.set(project.getName());
		licenseField.set(project.getLicense());
		descriptionField.set(project.getDescription());
		Configuration configuration = new Configuration();
		configuration.setName(project.getConfigurationName());
		configurationField.set(configuration);
		repositoryPanel.set(project.getRepository());
	}
}