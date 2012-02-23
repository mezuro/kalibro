package org.kalibro.desktop.swingextension.panel;

import org.kalibro.core.model.enums.Language;

class LanguagePanelStub extends EditPanel<Language> {

	protected LanguagePanelStub() {
		super("language");
	}

	@Override
	protected void createComponents() {
		return;
	}

	@Override
	protected void buildPanel() {
		return;
	}

	@Override
	public Language retrieve() {
		return null;
	}

	@Override
	public void show(Language language) {
		return;
	}
}