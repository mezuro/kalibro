package org.kalibro.desktop.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.kalibro.core.settings.KalibroSettings;
import org.kalibro.desktop.swingextension.dialog.AbstractDialog;
import org.kalibro.desktop.swingextension.field.BooleanField;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.desktop.swingextension.panel.GridBagPanelBuilder;

public class KalibroSettingsPanel extends EditPanel<KalibroSettings> implements ActionListener {

	private BooleanField clientField;
	private ClientSettingsPanel clientSettingsPanel;
	private ServerSettingsPanel serverSettingsPanel;

	private AbstractDialog parent;

	public KalibroSettingsPanel(AbstractDialog parent) {
		super("settings");
		this.parent = parent;
	}

	@Override
	protected void createComponents() {
		clientField = new BooleanField("client", "Use as client to Kalibro Service");
		clientField.addActionListener(this);
		clientSettingsPanel = new ClientSettingsPanel();
		serverSettingsPanel = new ServerSettingsPanel();
		alignPanelWidths();
	}

	private void alignPanelWidths() {
		int maxPanelWidth = getMaxPanelWidth();
		serverSettingsPanel.setWidth(maxPanelWidth);
		clientSettingsPanel.setWidth(maxPanelWidth);
	}

	private int getMaxPanelWidth() {
		int clientPanelWidth = clientSettingsPanel.getSize().width;
		int standalonePanelWidth = serverSettingsPanel.getPreferredSize().width;
		return Math.max(clientPanelWidth, standalonePanelWidth);
	}

	@Override
	protected void buildPanel() {
		GridBagPanelBuilder builder = new GridBagPanelBuilder(this);
		builder.addSimpleLine(clientField);
		builder.addSimpleLine(clientSettingsPanel);
		builder.addSimpleLine(serverSettingsPanel);
	}

	@Override
	public KalibroSettings get() {
		KalibroSettings settings = new KalibroSettings();
		settings.setClient(clientField.isSelected());
		settings.setClientSettings(clientSettingsPanel.get());
		settings.setServerSettings(serverSettingsPanel.get());
		return settings;
	}

	@Override
	public void set(KalibroSettings settings) {
		clientSettingsPanel.set(settings.getClientSettings());
		serverSettingsPanel.set(settings.getServerSettings());
		switchPanels(settings.isClient());
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		switchPanels(clientField.isSelected());
	}

	private void switchPanels(boolean client) {
		clientField.set(client);
		clientSettingsPanel.setVisible(client);
		serverSettingsPanel.setVisible(!client);
		adjustSize();
		parent.adjustSize();
	}
}