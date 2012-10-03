package org.kalibro.desktop.project;

import java.awt.Component;

import javax.swing.border.TitledBorder;

import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.dao.DaoFactory;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.ChoiceField;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class RepositoryPanel extends EditPanel<Repository> {

	private ChoiceField<RepositoryType> typeField;
	private StringField addressField;

	public RepositoryPanel() {
		super("repository");
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		typeField = new ChoiceField<RepositoryType>("type", DaoFactory.getRepositoryDao().supportedTypes());
		addressField = new StringField("address", 20);
	}

	@Override
	protected void buildPanel() {
		setBorder(new TitledBorder("Repository"));
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.addSimpleLine(new Label("Type:"), typeField);
		builder.add(new Label("Address:"));
		builder.add(addressField, 3);
	}

	@Override
	public Repository get() {
		return new Repository(typeField.get(), addressField.get());
	}

	@Override
	public void set(Repository repository) {
		typeField.set(repository.getType());
		addressField.set(repository.getAddress());
	}
}