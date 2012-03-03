package org.kalibro.desktop.settings;

import javax.swing.border.TitledBorder;

import org.kalibro.core.settings.ClientSettings;
import org.kalibro.desktop.swingextension.Label;
import org.kalibro.desktop.swingextension.field.LongField;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class ClientSettingsPanel extends EditPanel<ClientSettings> {

	private StringField serviceAddressField;
	private LongField pollingIntervalField;

	public ClientSettingsPanel() {
		super("clientSettings");
	}

	@Override
	protected void createComponents() {
		serviceAddressField = new StringField("serviceAddress", 35);
		pollingIntervalField = new LongField("pollingInterval");
	}

	@Override
	protected void buildPanel() {
		setBorder(new TitledBorder("Client settings"));
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.add(new Label("Service address:"));
		builder.add(serviceAddressField, 2);
		builder.newLine();
		builder.addSimpleLine(new Label("Polling interval:"), pollingIntervalField);
	}

	@Override
	public void set(ClientSettings settings) {
		serviceAddressField.setText(settings.getServiceAddress());
		pollingIntervalField.set(settings.getPollingInterval());
	}

	@Override
	public ClientSettings get() {
		ClientSettings settings = new ClientSettings();
		settings.setServiceAddress(serviceAddressField.getText());
		settings.setPollingInterval(pollingIntervalField.get().longValue());
		return settings;
	}
}