package org.kalibro.desktop.settings;

import java.awt.Component;

import javax.swing.border.TitledBorder;

import org.kalibro.core.settings.ClientSettings;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class ClientSettingsPanel extends EditPanel<ClientSettings> {

	private StringField serviceAddressField;

	public ClientSettingsPanel() {
		super("clientSettings");
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		serviceAddressField = new StringField("serviceAddress", 35);
	}

	@Override
	protected void buildPanel() {
		setBorder(new TitledBorder("Client settings"));
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.add(new Label("Service address:"));
		builder.add(serviceAddressField, 2);
	}

	@Override
	public ClientSettings get() {
		ClientSettings settings = new ClientSettings();
		settings.setServiceAddress(serviceAddressField.getText());
		return settings;
	}

	@Override
	public void set(ClientSettings settings) {
		serviceAddressField.setText(settings.getServiceAddress());
	}
}