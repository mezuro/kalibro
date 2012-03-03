package org.kalibro.desktop.settings;

import javax.swing.border.TitledBorder;

import org.kalibro.core.settings.ServerSettings;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.BooleanField;
import org.kalibro.desktop.swingextension.field.DirectoryField;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class ServerSettingsPanel extends EditPanel<ServerSettings> {

	private DirectoryField loadDirectoryField;
	private BooleanField removeSourcesField;
	private DatabaseSettingsPanel databasePanel;

	public ServerSettingsPanel() {
		super("serverSettings");
	}

	@Override
	protected void createComponents() {
		loadDirectoryField = new DirectoryField("loadDirectory");
		removeSourcesField = new BooleanField("removeSources", "Remove source code after analysis");
		databasePanel = new DatabaseSettingsPanel();
	}

	@Override
	protected void buildPanel() {
		setBorder(new TitledBorder("Server settings"));
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.add(new Label("Load directory:"));
		builder.add(loadDirectoryField, 2);
		builder.newLine();
		builder.add(removeSourcesField, 3);
		builder.newLine();
		builder.add(databasePanel, 3);
	}

	@Override
	public void set(ServerSettings settings) {
		loadDirectoryField.setDirectory(settings.getLoadDirectory());
		removeSourcesField.setSelected(settings.shouldRemoveSources());
		databasePanel.set(settings.getDatabaseSettings());
	}

	@Override
	public ServerSettings get() {
		ServerSettings settings = new ServerSettings();
		settings.setLoadDirectory(loadDirectoryField.getDirectory());
		settings.setRemoveSources(removeSourcesField.isSelected());
		settings.setDatabaseSettings(databasePanel.get());
		return settings;
	}
}