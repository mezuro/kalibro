package org.kalibro.desktop.swingextension.panel;

import java.awt.Component;

import org.kalibro.core.model.enums.Language;

class LanguagePanelStub extends EditPanel<Language> {

	protected LanguagePanelStub() {
		super("language");
	}

	@Override
	protected void createComponents(Component... innerComponents) {
		return;
	}

	@Override
	protected void buildPanel() {
		return;
	}

	@Override
	public Language get() {
		return null;
	}

	@Override
	public void set(Language language) {
		return;
	}
}