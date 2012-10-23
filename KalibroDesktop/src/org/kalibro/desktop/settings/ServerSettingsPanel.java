package org.kalibro.desktop.settings;

import java.awt.Component;

import javax.swing.border.TitledBorder;

import org.kalibro.ServerSettings;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.DirectoryField;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class ServerSettingsPanel extends EditPanel<ServerSettings> {

	private DirectoryField loadDirectoryField;
	private DatabaseSettingsPanel databasePanel;

	public ServerSettingsPanel() {
		super("serverSettings");
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		loadDirectoryField = new DirectoryField("loadDirectory");
		databasePanel = new DatabaseSettingsPanel();
	}

	@Override
	protected void buildPanel() {
		setBorder(new TitledBorder("Server settings"));
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.add(new Label("Load directory:"));
		builder.add(loadDirectoryField, 2);
		builder.newLine();
		builder.add(databasePanel, 3);
	}

	@Override
	public ServerSettings get() {
		ServerSettings settings = new ServerSettings();
		settings.setLoadDirectory(loadDirectoryField.get());
		settings.setDatabaseSettings(databasePanel.get());
		return settings;
	}

	@Override
	public void set(ServerSettings settings) {
		loadDirectoryField.set(settings.getLoadDirectory());
		databasePanel.set(settings.getDatabaseSettings());
	}
}